package com.wzkris.payment.impl.tenantbalance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.tenantbalance.TenantBalanceInfoApi;
import com.wzkris.payment.api.tenantbalance.request.BalanceWithdrawalRequest;
import com.wzkris.payment.api.tenantbalance.request.SetPayPasswordRequest;
import com.wzkris.payment.api.tenantbalance.request.TenantBalanceTransactionLogInfoPageRequest;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceInfoQueryResponse;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceTransactionLogInfoPageResponse;
import com.wzkris.payment.domain.TenantBalanceInfoDO;
import com.wzkris.payment.domain.TenantBalanceTransactionLogDO;
import com.wzkris.payment.domain.TenantBalanceWithdrawalLogDO;
import com.wzkris.payment.enums.tenantbalance.TenantBalanceWithdrawalStatusEnum;
import com.wzkris.payment.mapper.TenantBalanceTransactionLogMapper;
import com.wzkris.payment.mapper.TenantBalanceWithdrawalLogMapper;
import com.wzkris.payment.service.TenantBalanceInfoService;
import com.wzkris.payment.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantBalanceInfoApiImpl extends AbstractApi implements TenantBalanceInfoApi {

    private final TenantBalanceInfoService tenantBalanceInfoService;

    private final TenantBalanceTransactionLogMapper tenantBalanceTransactionLogMapper;

    private final TenantBalanceWithdrawalLogMapper tenantBalanceWithdrawalLogMapper;

    private final PasswordEncoder passwordEncoder;

    private final OrderNoGenerator orderNoGenerator;

    @Override
    public Result<TenantBalanceInfoQueryResponse> query() {
        TenantBalanceInfoDO balance = tenantBalanceInfoService.getOrCreate(SecurityUtil.getTenantId());
        return ok(BeanCopierUtil.copy(balance, TenantBalanceInfoQueryResponse.class));
    }

    @Override
    public Result<Page<TenantBalanceTransactionLogInfoPageResponse>> queryTransactionPage(TenantBalanceTransactionLogInfoPageRequest request) {
        IPage<TenantBalanceTransactionLogDO> page = tenantBalanceTransactionLogMapper.selectPage(request.buildPage(), this.buildBalanceQueryWrapper(request));
        List<TenantBalanceTransactionLogInfoPageResponse> list = page.getRecords().stream().map(this::toResponse).toList();
        return ok(Page.of(page, list));
    }

    @Override
    public Result<Void> setPayPassword(SetPayPasswordRequest request) {
        TenantBalanceInfoDO balance = tenantBalanceInfoService.getOrCreate(SecurityUtil.getTenantId());
        // 已设置且与旧密码一致则拒绝，避免无意义重复提交
        if (StringUtil.isNotBlank(balance.getPayPassword())
                && passwordEncoder.matches(request.getPayPwd(), balance.getPayPassword())) {
            return requestFail("新支付密码不能与当前密码一致");
        }
        balance.setPayPassword(passwordEncoder.encode(request.getPayPwd()));
        return toRes(tenantBalanceInfoService.updateById(balance));
    }

    @Override
    public Result<Void> withdrawal(BalanceWithdrawalRequest request) {
        TenantBalanceInfoDO balance = tenantBalanceInfoService.getOrCreate(SecurityUtil.getTenantId());
        if (StringUtil.isBlank(balance.getPayPassword())) {
            return requestFail("请先设置提现支付密码");
        }
        if (!passwordEncoder.matches(request.getPayPwd(), balance.getPayPassword())) {
            return requestFail("支付密码错误");
        }
        // 本期仅记账登记提现意图（打款渠道对接后续），不扣减余额
        TenantBalanceWithdrawalLogDO withdrawalLogDO = new TenantBalanceWithdrawalLogDO();
        withdrawalLogDO.setTenantId(SecurityUtil.getTenantId());
        withdrawalLogDO.setWithdrawalNo(orderNoGenerator.nextWithdrawalNo());
        withdrawalLogDO.setStatus(TenantBalanceWithdrawalStatusEnum.PROCESSING);
        withdrawalLogDO.setWithdrawalAmount(request.getAmount());
        return toRes(tenantBalanceWithdrawalLogMapper.insert(withdrawalLogDO) > 0);
    }

    private LambdaQueryWrapper<TenantBalanceTransactionLogDO> buildBalanceQueryWrapper(TenantBalanceTransactionLogInfoPageRequest request) {
        return new LambdaQueryWrapper<TenantBalanceTransactionLogDO>()
                .eq(TenantBalanceTransactionLogDO::getTenantId, SecurityUtil.getTenantId())
                .like(Objects.nonNull(request.getRecordType()), TenantBalanceTransactionLogDO::getRecordType, request.getRecordType())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantBalanceTransactionLogDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantBalanceTransactionLogDO::getId);
    }

    private TenantBalanceTransactionLogInfoPageResponse toResponse(TenantBalanceTransactionLogDO recordDO) {
        TenantBalanceTransactionLogInfoPageResponse response = new TenantBalanceTransactionLogInfoPageResponse();
        response.setId(recordDO.getId());
        response.setTenantId(recordDO.getTenantId());
        response.setAmount(recordDO.getAmount());
        response.setRecordType(recordDO.getRecordType());
        response.setBizType(recordDO.getBizType());
        response.setBizNo(recordDO.getBizNo());
        response.setCreateAt(recordDO.getCreateAt());
        response.setRemark(recordDO.getRemark());
        return response;
    }

}