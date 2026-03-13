param(
    [string]$BrokerHost = "127.0.0.1",
    [int]$Port = 1883
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Assert-Command([string]$name) {
    $command = Get-Command $name -ErrorAction SilentlyContinue
    if (-not $command) {
        throw "Command '$name' not found. Please install mosquitto clients first."
    }
}

function Wait-Subscriber([System.Diagnostics.Process]$proc, [int]$timeoutSeconds, [string]$label) {
    if (-not $proc.WaitForExit($timeoutSeconds * 1000)) {
        try { $proc.Kill() } catch {}
        throw "Subscriber '$label' timed out after ${timeoutSeconds}s."
    }
}

function Start-Subscriber([string]$clientId, [string]$topic, [string]$outputFile, [int]$count = 1) {
    $mqttArgs = @(
        "-h", $BrokerHost,
        "-p", "$Port",
        "-i", $clientId,
        "-t", $topic,
        "-v",
        "-C", "$count"
    )
    return Start-Process -FilePath "mosquitto_sub" -ArgumentList $mqttArgs -NoNewWindow -PassThru -RedirectStandardOutput $outputFile
}

function Publish-Message([string]$clientId, [string]$topic, [string]$payload) {
    & mosquitto_pub -h $BrokerHost -p $Port -i $clientId -t $topic -m $payload
}

function Assert-Contains([string]$path, [string]$expected, [string]$label) {
    $content = Get-Content -Path $path -Raw
    if ($content -notmatch [regex]::Escape($expected)) {
        throw "Assertion failed for '$label'. Expected content '$expected' not found in '$path'."
    }
}

Assert-Command "mosquitto_sub"
Assert-Command "mosquitto_pub"

$tempDir = Join-Path $env:TEMP "mqtt-shared-topic-regression"
if (-not (Test-Path $tempDir)) {
    New-Item -ItemType Directory -Path $tempDir | Out-Null
}

Write-Host "Using temp directory: $tempDir"
Write-Host "Target broker: $BrokerHost`:$Port"

# Case 1: normal topic broadcast (both should receive 1 message)
$normalA = Join-Path $tempDir "normal-a.log"
$normalB = Join-Path $tempDir "normal-b.log"
$procNormalA = Start-Subscriber -clientId "normal-a" -topic "device/123" -outputFile $normalA -count 1
$procNormalB = Start-Subscriber -clientId "normal-b" -topic "device/123" -outputFile $normalB -count 1
Start-Sleep -Milliseconds 400
Publish-Message -clientId "publisher-normal" -topic "device/123" -payload "normal-1"
Wait-Subscriber -proc $procNormalA -timeoutSeconds 6 -label "normal-a"
Wait-Subscriber -proc $procNormalB -timeoutSeconds 6 -label "normal-b"
Assert-Contains -path $normalA -expected "device/123 normal-1" -label "normal-a receive"
Assert-Contains -path $normalB -expected "device/123 normal-1" -label "normal-b receive"
Write-Host "[OK] normal broadcast"

# Case 2: shared topic round-robin (2 subscribers consume total 4 messages)
$sharedA = Join-Path $tempDir "shared-a.log"
$sharedB = Join-Path $tempDir "shared-b.log"
$procSharedA = Start-Subscriber -clientId "shared-a" -topic '$share/device/123' -outputFile $sharedA -count 2
$procSharedB = Start-Subscriber -clientId "shared-b" -topic '$share/device/123' -outputFile $sharedB -count 2
Start-Sleep -Milliseconds 600
Publish-Message -clientId "publisher-shared" -topic "device/123" -payload "shared-1"
Publish-Message -clientId "publisher-shared" -topic "device/123" -payload "shared-2"
Publish-Message -clientId "publisher-shared" -topic "device/123" -payload "shared-3"
Publish-Message -clientId "publisher-shared" -topic "device/123" -payload "shared-4"
Wait-Subscriber -proc $procSharedA -timeoutSeconds 8 -label "shared-a"
Wait-Subscriber -proc $procSharedB -timeoutSeconds 8 -label "shared-b"
Write-Host "[OK] shared round-robin total delivery reached"

# Case 3: coexist normal + shared (normal gets all, shared gets one per message)
$mixNormal = Join-Path $tempDir "mix-normal.log"
$mixSharedA = Join-Path $tempDir "mix-shared-a.log"
$mixSharedB = Join-Path $tempDir "mix-shared-b.log"
$procMixNormal = Start-Subscriber -clientId "mix-normal" -topic "device/321" -outputFile $mixNormal -count 2
$procMixSharedA = Start-Subscriber -clientId "mix-shared-a" -topic '$share/device/321' -outputFile $mixSharedA -count 1
$procMixSharedB = Start-Subscriber -clientId "mix-shared-b" -topic '$share/device/321' -outputFile $mixSharedB -count 1
Start-Sleep -Milliseconds 600
Publish-Message -clientId "publisher-mix" -topic "device/321" -payload "mix-1"
Publish-Message -clientId "publisher-mix" -topic "device/321" -payload "mix-2"
Wait-Subscriber -proc $procMixNormal -timeoutSeconds 8 -label "mix-normal"
Wait-Subscriber -proc $procMixSharedA -timeoutSeconds 8 -label "mix-shared-a"
Wait-Subscriber -proc $procMixSharedB -timeoutSeconds 8 -label "mix-shared-b"
Assert-Contains -path $mixNormal -expected "device/321 mix-1" -label "mix-normal message1"
Assert-Contains -path $mixNormal -expected "device/321 mix-2" -label "mix-normal message2"
Write-Host "[OK] normal+shared coexist"

# Case 4: disconnect stability (shared-a offline, shared-b should still receive)
$disconnectA = Join-Path $tempDir "disconnect-a.log"
$disconnectB = Join-Path $tempDir "disconnect-b.log"
$procDisconnectA = Start-Subscriber -clientId "disconnect-a" -topic '$share/device/456' -outputFile $disconnectA -count 1
Start-Sleep -Milliseconds 500
if (-not $procDisconnectA.HasExited) {
    $procDisconnectA.Kill()
}
$procDisconnectB = Start-Subscriber -clientId "disconnect-b" -topic '$share/device/456' -outputFile $disconnectB -count 1
Start-Sleep -Milliseconds 500
Publish-Message -clientId "publisher-disconnect" -topic "device/456" -payload "disconnect-1"
Wait-Subscriber -proc $procDisconnectB -timeoutSeconds 6 -label "disconnect-b"
Assert-Contains -path $disconnectB -expected "device/456 disconnect-1" -label "disconnect scenario"
Write-Host "[OK] disconnect stability"

Write-Host "All shared-topic regression checks passed."
