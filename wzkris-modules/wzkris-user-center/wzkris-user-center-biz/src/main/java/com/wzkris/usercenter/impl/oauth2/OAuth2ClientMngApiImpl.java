package com.wzkris.usercenter.impl.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.oauth2.OAuth2ClientMngApi;
import com.wzkris.usercenter.api.oauth2.request.ClientSecretUpdateRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngPageRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngSaveRequest;
import com.wzkris.usercenter.api.oauth2.request.OAuth2ClientMngUpdateRequest;
import com.wzkris.usercenter.api.oauth2.response.OAuth2ClientMngResponse;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import com.wzkris.usercenter.service.OAuth2ClientService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ClientMngApiImpl extends AbstractApi implements OAuth2ClientMngApi {

    private final OAuth2ClientMapper oauth2ClientMapper;

    private final OAuth2ClientService oAuth2ClientService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<Page<OAuth2ClientMngResponse>> queryPage(OAuth2ClientMngPageRequest request) {
        IPage<OAuth2ClientDO> page = oAuth2ClientService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanUtil.convert(page.getRecords(), OAuth2ClientMngResponse.class)));
    }

    private LambdaQueryWrapper<OAuth2ClientDO> buildQueryWrapper(OAuth2ClientMngPageRequest request) {
        return new LambdaQueryWrapper<OAuth2ClientDO>()
                .eq(request.getStatus() != null, OAuth2ClientDO::getStatus, request.getStatus())
                .like(StringUtil.isNotEmpty(request.getClientId()), OAuth2ClientDO::getClientId, request.getClientId());
    }

    @Override
    public Result<OAuth2ClientMngResponse> queryInfo(IdRequest request) {
        return ok(BeanUtil.convert(oAuth2ClientService.getById(request.getId()), OAuth2ClientMngResponse.class));
    }

    @Override
    public Result<Void> update(OAuth2ClientMngUpdateRequest request) {
        OAuth2ClientDO oauth2ClientDO = BeanUtil.convert(request, OAuth2ClientDO.class);
        oauth2ClientDO.setStatus(request.getStatus());
        return toRes(oAuth2ClientService.updateById(oauth2ClientDO));
    }

    @Override
    public Result<Void> updateSecret(ClientSecretUpdateRequest request) {
        OAuth2ClientDO update = new OAuth2ClientDO();
        update.setId(request.getId());
        update.setClientSecret(passwordEncoder.encode(request.getSecret()));
        return toRes(oAuth2ClientService.updateById(update));
    }

    @Override
    public Result<String> save(OAuth2ClientMngSaveRequest request) {
        OAuth2ClientDO client = BeanUtil.convert(request, OAuth2ClientDO.class);
        client.setStatus(request.getStatus());
        String secret = RandomStringUtils.secure().nextAlphabetic(16);
        client.setClientSecret(passwordEncoder.encode(secret));
        String result = oAuth2ClientService.saveAndGet(client, c -> secret);
        return result != null ? ok(secret) : requestFail("操作失败");
    }

    @Override
    public Result<Void> remove(IdRequest request) {
        return toRes(oAuth2ClientService.removeById(request.getId()));
    }

}
