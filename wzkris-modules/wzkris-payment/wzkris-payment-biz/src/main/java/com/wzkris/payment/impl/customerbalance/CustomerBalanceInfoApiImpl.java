package com.wzkris.payment.impl.customerbalance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.customerbalance.CustomerBalanceInfoApi;
import com.wzkris.payment.api.customerbalance.request.CustomerBalanceTransactionLogPageRequest;
import com.wzkris.payment.api.customerbalance.response.CustomerBalanceInfoQueryResponse;
import com.wzkris.payment.api.customerbalance.response.CustomerBalanceTransactionLogInfoPageResponse;
import com.wzkris.payment.domain.CustomerBalanceInfoDO;
import com.wzkris.payment.domain.CustomerBalanceTransactionLogDO;
import com.wzkris.payment.mapper.CustomerBalanceTransactionLogMapper;
import com.wzkris.payment.service.CustomerBalanceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerBalanceInfoApiImpl extends AbstractApi implements CustomerBalanceInfoApi {

    private final CustomerBalanceInfoService customerBalanceInfoService;

    private final CustomerBalanceTransactionLogMapper customerBalanceTransactionLogMapper;

    @Override
    public Result<CustomerBalanceInfoQueryResponse> query() {
        CustomerBalanceInfoDO balance = customerBalanceInfoService.getOrCreate(SecurityUtil.getUid());
        return ok(BeanCopierUtil.copy(balance, CustomerBalanceInfoQueryResponse.class));
    }

    @Override
    public Result<Page<CustomerBalanceTransactionLogInfoPageResponse>> queryTransactionPage(CustomerBalanceTransactionLogPageRequest request) {
        IPage<CustomerBalanceTransactionLogDO> page = customerBalanceTransactionLogMapper.selectPage(request.buildPage(), this.buildBalanceQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), CustomerBalanceTransactionLogInfoPageResponse.class)));
    }

    private LambdaQueryWrapper<CustomerBalanceTransactionLogDO> buildBalanceQueryWrapper(CustomerBalanceTransactionLogPageRequest request) {
        return new LambdaQueryWrapper<CustomerBalanceTransactionLogDO>()
                .eq(CustomerBalanceTransactionLogDO::getCustomerId, SecurityUtil.getUid())
                .like(Objects.nonNull(request.getRecordType()), CustomerBalanceTransactionLogDO::getRecordType, request.getRecordType())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerBalanceTransactionLogDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(CustomerBalanceTransactionLogDO::getId);
    }

}