package com.thingslink.auth.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.auth.domain.OAuth2Client;
import com.thingslink.auth.mapper.OAuth2ClientMapper;
import com.thingslink.auth.service.OAuth2ClientService;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.utils.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * OAuth2服务
 *
 * @author wzkris
 */
@Service
@AllArgsConstructor
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    private final OAuth2ClientMapper oauth2ClientMapper;

    /**
     * 分页查询
     *
     * @param oAuth2Client 查询条件
     */
    @Override
    public Page<OAuth2Client> listPage(OAuth2Client oAuth2Client) {
        PageUtil.startPage();
        // 查询
        LambdaQueryWrapper<OAuth2Client> lqw = this.buildQueryWrapper(oAuth2Client);
        List<OAuth2Client> packages = oauth2ClientMapper.selectList(lqw);

        // 先获取分页信息，否则总数会有问题
        Page page = PageUtil.getPage();
        page.setRows(packages);
        return page;
    }

    private LambdaQueryWrapper<OAuth2Client> buildQueryWrapper(OAuth2Client oAuth2Client) {
        return new LambdaQueryWrapper<OAuth2Client>()
                .eq(ObjUtil.isNotEmpty(oAuth2Client.getStatus()), OAuth2Client::getStatus, oAuth2Client.getStatus())
                .like(ObjUtil.isNotEmpty(oAuth2Client.getClientId()), OAuth2Client::getClientId, oAuth2Client.getClientId());
    }
}
