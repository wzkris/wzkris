package com.wzkris.auth.oauth2.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.auth.oauth2.model.OAuth2AuthorizationModel;
import com.wzkris.auth.oauth2.redis.deserializer.OAuth2AuthorizationModelDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OAuth2AuthorizationModel反序列化测试")
public class OAuth2AuthorizationModelDeserializerTest {

    static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OAuth2AuthorizationModel.class, new OAuth2AuthorizationModelDeserializer());
        objectMapper.registerModules(simpleModule);
    }

    @Test
    public void test() throws JsonProcessingException {
        String STR = """
                {
                  "@class": "com.wzkris.auth.oauth2.model.OAuth2AuthorizationModel",
                  "attributes": {
                    "@class": "java.util.Collections$UnmodifiableMap",
                    "java.security.Principal": {
                      "@class": "com.wzkris.common.security.oauth2.authentication.WkAuthenticationToken",
                      "authenticated": true,
                      "authorities": [
                        "java.util.Collections$UnmodifiableRandomAccessList",
                        []
                      ],
                      "clientId": "server",
                      "principal": {
                        "@class": "com.wzkris.common.security.oauth2.domain.OAuth2User",
                        "authorities": [
                          "java.util.Collections$UnmodifiableSet",
                          [
                            {
                              "role": "*"
                            }
                          ]
                        ],
                        "oauth2Type": "sys_user",
                        "principal": {
                          "@class": "com.wzkris.common.security.oauth2.domain.model.LoginSyser",
                          "administrator": true,
                          "deptId": [
                            "java.lang.Long",
                            100
                          ],
                          "deptScopes": [
                            "java.util.ArrayList",
                            []
                          ],
                          "password": "{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2",
                          "tenantId": [
                            "java.lang.Long",
                            0
                          ],
                          "userId": [
                            "java.lang.Long",
                            1
                          ],
                          "username": "admin"
                        },
                        "principalName": "admin"
                      }
                    }
                  },
                  "authorizationGrantType": "password",
                  "authorizedScopes": [
                    "java.util.Collections$UnmodifiableSet",
                    []
                  ],
                  "id": "5a423b1f-20e5-48b2-9abc-ad0a2d8a19a8",
                  "principalName": "admin",
                  "registeredClientId": "server"
                }
                """;
        OAuth2AuthorizationModel authorizationModel = objectMapper.readValue(STR, OAuth2AuthorizationModel.class);
        System.out.println(authorizationModel);
    }
}
