package com.wzkris.usercenter.impl.customerwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.customerwallet.CustomerWalletInfoApi;
import com.wzkris.usercenter.api.customerwallet.request.CustomerWalletRecordPageRequest;
import com.wzkris.usercenter.api.customerwallet.response.CustomerWalletInfoQueryResponse;
import com.wzkris.usercenter.api.customerwallet.response.CustomerWalletRecordInfoPageResponse;
import com.wzkris.usercenter.domain.CustomerWalletRecordDO;
import com.wzkris.usercenter.mapper.CustomerWalletRecordMapper;
import com.wzkris.usercenter.service.CustomerWalletInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerWalletInfoApiImpl extends AbstractApi implements CustomerWalletInfoApi {

    private final CustomerWalletInfoService customerWalletInfoService;

    private final CustomerWalletRecordMapper customerWalletRecordMapper;

    @Override
    public Result<CustomerWalletInfoQueryResponse> query() {
        return ok(customerWalletInfoService.getById2VO(SecurityUtil.getUid(), CustomerWalletInfoQueryResponse.class));
    }

    @Override
    public Result<Page<CustomerWalletRecordInfoPageResponse>> queryRecordPage(CustomerWalletRecordPageRequest request) {
        IPage<CustomerWalletRecordDO> page = customerWalletRecordMapper.selectPage(request.buildPage(), this.buildWalletQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), CustomerWalletRecordInfoPageResponse.class)));
    }

    private LambdaQueryWrapper<CustomerWalletRecordDO> buildWalletQueryWrapper(CustomerWalletRecordPageRequest request) {
        return new LambdaQueryWrapper<CustomerWalletRecordDO>()
                .like(Objects.nonNull(request.getRecordType()), CustomerWalletRecordDO::getRecordType, request.getRecordType())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerWalletRecordDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(CustomerWalletRecordDO::getId);
    }

}
