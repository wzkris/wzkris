package com.wzkris.usercenter.impl.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.oauth2.OAuth2ClientMngApi;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.oauth2.ClientSecretUpdateRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngPageRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.request.oauth2.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.response.oauth2.OAuth2ClientMngResponse;
import com.wzkris.usercenter.service.OAuth2ClientService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2ClientMngApiImpl extends AbstractApi implements OAuth2ClientMngApi {

    private final PasswordEncoder passwordEncoder;

    private final OAuth2ClientMapper oauth2ClientMapper;

    private final OAuth2ClientService oAuth2ClientService;

    @Override
    public Result<Page<OAuth2ClientMngResponse>> queryPage(OAuth2ClientMngPageRequest request) {
        startPage();
        List<OAuth2ClientDO> list = oauth2ClientMapper.selectList(this.buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, OAuth2ClientMngResponse.class));
    }

    private LambdaQueryWrapper<OAuth2ClientDO> buildQueryWrapper(OAuth2ClientMngPageRequest request) {
        return new LambdaQueryWrapper<OAuth2ClientDO>()
                .eq(request.getStatus() != null, OAuth2ClientDO::getStatus, request.getStatus())
                .like(StringUtil.isNotEmpty(request.getClientId()), OAuth2ClientDO::getClientId, request.getClientId());
    }

    @Override
    public Result<OAuth2ClientMngResponse> queryInfo(IdRequest request) {
        return ok(BeanUtil.convert(oauth2ClientMapper.selectById(request.getId()), OAuth2ClientMngResponse.class));
    }

    @Override
    public Result<Void> update(OAuth2ClientMngUpdateRequest request) {
        OAuth2ClientDO oauth2ClientDO = BeanUtil.convert(request, OAuth2ClientDO.class);
        oauth2ClientDO.setStatus(request.getStatus());
        return toRes(oauth2ClientMapper.updateById(oauth2ClientDO));
    }

    @Override
    public Result<Void> updateSecret(ClientSecretUpdateRequest request) {
        OAuth2ClientDO update = new OAuth2ClientDO();
        update.setId(request.getId());
        update.setClientSecret(passwordEncoder.encode(request.getSecret()));
        return toRes(oauth2ClientMapper.updateById(update));
    }

    @Override
    public Result<String> save(OAuth2ClientMngSaveRequest request) {
        OAuth2ClientDO client = BeanUtil.convert(request, OAuth2ClientDO.class);
        client.setStatus(request.getStatus());
        String secret = RandomStringUtils.secure().nextAlphabetic(16);
        client.setClientSecret(passwordEncoder.encode(secret));
        oauth2ClientMapper.insert(client);
        return ok(secret);
    }

    @Override
    public Result<Void> remove(IdRequest request) {
        return toRes(oauth2ClientMapper.deleteById(request.getId()));
    }

}
