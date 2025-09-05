package com.wzkris.user.feign.domain.resp;

import com.wzkris.user.domain.OAuth2ClientDO;
import com.wzkris.user.domain.OAuth2ClientDOToOAuth2ClientExportMapper;
import com.wzkris.user.domain.OAuth2ClientDOToOAuth2ClientReqMapper;
import com.wzkris.user.domain.OAuth2ClientDOToOAuth2ClientRespMapper;
import com.wzkris.user.domain.export.OAuth2ClientExportToOAuth2ClientDOMapper;
import com.wzkris.user.domain.req.OAuth2ClientReqToOAuth2ClientDOMapper;
import com.wzkris.user.feign.oauth2.resp.OAuth2ClientResp;
import io.github.linpeilie.AutoMapperConfig__169;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__169.class,
    uses = {OAuth2ClientExportToOAuth2ClientDOMapper.class,OAuth2ClientReqToOAuth2ClientDOMapper.class,OAuth2ClientDOToOAuth2ClientExportMapper.class,OAuth2ClientDOToOAuth2ClientReqMapper.class,OAuth2ClientDOToOAuth2ClientRespMapper.class},
    imports = {}
)
public interface OAuth2ClientRespToOAuth2ClientDOMapper extends BaseMapper<OAuth2ClientResp, OAuth2ClientDO> {
}
