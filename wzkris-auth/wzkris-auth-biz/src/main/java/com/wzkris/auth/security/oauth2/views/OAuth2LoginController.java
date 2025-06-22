package com.wzkris.auth.security.oauth2.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wzkris
 */
@Controller
public class OAuth2LoginController {

    @RequestMapping(value = "/oauth2/login")
    public String login() {
        return "login";
    }

}
