package com.wzkris.user.feign.domain.resp;

import com.wzkris.user.domain.OAuth2ClientDO;
import java.util.Arrays;
import javax.annotation.processing.Generated;

import com.wzkris.user.feign.oauth2.resp.OAuth2ClientResp;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T17:10:48+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Oracle Corporation)"
)
@Component
public class OAuth2ClientRespToOAuth2ClientDOMapperImpl implements OAuth2ClientRespToOAuth2ClientDOMapper {

    @Override
    public OAuth2ClientDO convert(OAuth2ClientResp arg0) {
        if ( arg0 == null ) {
            return null;
        }

        OAuth2ClientDO oAuth2ClientDO = new OAuth2ClientDO();

        oAuth2ClientDO.setStatus( arg0.getStatus() );
        oAuth2ClientDO.setClientId( arg0.getClientId() );
        oAuth2ClientDO.setClientSecret( arg0.getClientSecret() );
        String[] scopes = arg0.getScopes();
        if ( scopes != null ) {
            oAuth2ClientDO.setScopes( Arrays.copyOf( scopes, scopes.length ) );
        }
        String[] authorizationGrantTypes = arg0.getAuthorizationGrantTypes();
        if ( authorizationGrantTypes != null ) {
            oAuth2ClientDO.setAuthorizationGrantTypes( Arrays.copyOf( authorizationGrantTypes, authorizationGrantTypes.length ) );
        }
        String[] redirectUris = arg0.getRedirectUris();
        if ( redirectUris != null ) {
            oAuth2ClientDO.setRedirectUris( Arrays.copyOf( redirectUris, redirectUris.length ) );
        }
        oAuth2ClientDO.setAutoApprove( arg0.getAutoApprove() );

        return oAuth2ClientDO;
    }

    @Override
    public OAuth2ClientDO convert(OAuth2ClientResp arg0, OAuth2ClientDO arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setStatus( arg0.getStatus() );
        arg1.setClientId( arg0.getClientId() );
        arg1.setClientSecret( arg0.getClientSecret() );
        String[] scopes = arg0.getScopes();
        if ( scopes != null ) {
            arg1.setScopes( Arrays.copyOf( scopes, scopes.length ) );
        }
        else {
            arg1.setScopes( null );
        }
        String[] authorizationGrantTypes = arg0.getAuthorizationGrantTypes();
        if ( authorizationGrantTypes != null ) {
            arg1.setAuthorizationGrantTypes( Arrays.copyOf( authorizationGrantTypes, authorizationGrantTypes.length ) );
        }
        else {
            arg1.setAuthorizationGrantTypes( null );
        }
        String[] redirectUris = arg0.getRedirectUris();
        if ( redirectUris != null ) {
            arg1.setRedirectUris( Arrays.copyOf( redirectUris, redirectUris.length ) );
        }
        else {
            arg1.setRedirectUris( null );
        }
        arg1.setAutoApprove( arg0.getAutoApprove() );

        return arg1;
    }
}
