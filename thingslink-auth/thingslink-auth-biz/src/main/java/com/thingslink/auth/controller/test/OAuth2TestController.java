package com.thingslink.auth.controller.test;

import com.thingslink.system.api.RemoteLogApi;
import com.thingslink.system.api.domain.LoginLogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OAuth2TestController {

    @Autowired
    private RemoteLogApi remoteLogApi;

    @GetMapping("/oauth2/authorization_code_callback")
    public ResponseEntity<?> callback(String code) {
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @PostMapping("testing")
    public ResponseEntity<?> test() {
        remoteLogApi.insertLoginlog(new LoginLogDTO());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
