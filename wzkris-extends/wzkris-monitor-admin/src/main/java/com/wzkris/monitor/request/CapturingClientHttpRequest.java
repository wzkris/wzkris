package com.wzkris.monitor.request;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * 用于在 BodyInserter.insert(...) 时捕获写入的 DataBuffer（缓存到内存）
 * <p>
 * 仅用于捕获小请求体（JSON 等），不适合大文件。
 *
 * @author wzkris
 */
public class CapturingClientHttpRequest implements ClientHttpRequest {

    private final HttpMethod method;

    private final URI uri;

    private final HttpHeaders headers = new HttpHeaders();

    private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

    // cookie / attributes 支持
    private final MultiValueMap<String, HttpCookie> cookies = new LinkedMultiValueMap<>();

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    // beforeCommit actions
    private final List<Supplier<? extends Mono<Void>>> beforeCommitActions = new CopyOnWriteArrayList<>();

    private final AtomicBoolean committed = new AtomicBoolean(false);

    // 捕获 body 的缓冲区
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public CapturingClientHttpRequest(HttpMethod method, URI uri) {
        this.method = method;
        this.uri = uri;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public URI getURI() {
        return this.uri;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }

    @Override
    public MultiValueMap<String, HttpCookie> getCookies() {
        return this.cookies;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public DataBufferFactory bufferFactory() {
        return this.bufferFactory;
    }

    @Override
    public <T> T getNativeRequest() {
        // 没有底层客户端请求对象，返回 null（调用方若需要可改为保存实际 native request）
        return null;
    }

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
        // 保存动作，setComplete() 时会执行
        if (action != null) {
            this.beforeCommitActions.add(action);
        }
    }

    @Override
    public boolean isCommitted() {
        return this.committed.get();
    }

    @Override
    public Mono<Void> setComplete() {
        // 只执行一次 beforeCommit 动作序列
        if (this.committed.compareAndSet(false, true)) {
            return Flux.fromIterable(this.beforeCommitActions)
                    .concatMap(supplier -> Mono.defer(supplier)) // 依次执行并等候
                    .then();
        }
        return Mono.empty();
    }

    @Override
    public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
        // 将 writeWith 的 DataBuffer 都读入 baos（捕获）
        return Flux.from(body)
                .doOnNext(dataBuffer -> {
                    try {
                        int len = dataBuffer.readableByteCount();
                        byte[] bytes = new byte[len];
                        dataBuffer.read(bytes);
                        synchronized (baos) {
                            baos.write(bytes);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        DataBufferUtils.release(dataBuffer);
                    }
                })
                .then();
    }

    @Override
    public Mono<Void> writeAndFlushWith(org.reactivestreams.Publisher<? extends org.reactivestreams.Publisher<? extends DataBuffer>> body) {
        return Flux.from(body)
                .flatMap(inner -> Flux.from(inner))
                .doOnNext(dataBuffer -> {
                    try {
                        int len = dataBuffer.readableByteCount();
                        byte[] bytes = new byte[len];
                        dataBuffer.read(bytes);
                        synchronized (baos) {
                            baos.write(bytes);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        DataBufferUtils.release(dataBuffer);
                    }
                })
                .then();
    }

    /**
     * 返回捕获到的请求体字符串（UTF-8），如果没有 body 返回 null
     */
    public String getBodyAsString() {
        synchronized (baos) {
            if (baos.size() == 0) {
                return null;
            }
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    @Override
    public String toString() {
        return "CapturingClientHttpRequest{" +
                "method=" + method +
                ", uri=" + uri +
                '}';
    }

}
