package com.thingslink.auth.controller.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class OAuth2TestController {

    @GetMapping("/oauth2/authorization_code_callback")
    public ResponseEntity<?> callback(String code) {
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

}
