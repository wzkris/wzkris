package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.LoginCustomerUtil;
import com.wzkris.user.domain.CustomerWalletRecordDO;
import com.wzkris.user.domain.req.CustomerWalletRecordQueryReq;
import com.wzkris.user.domain.vo.CustomerWalletInfoVO;
import com.wzkris.user.mapper.CustomerWalletInfoMapper;
import com.wzkris.user.mapper.CustomerWalletRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户钱包信息
 *
 * @author wzkris
 */
@Tag(name = "客户钱包信息")
@Slf4j
@Validated
@RestController
@RequestMapping("/customer-wallet-info")
@RequiredArgsConstructor
public class CustomerWalletInfoController extends BaseController {

    private final CustomerWalletInfoMapper customerWalletInfoMapper;

    private final CustomerWalletRecordMapper customerWalletRecordMapper;

    @Operation(summary = "余额信息")
    @GetMapping
    public Result<CustomerWalletInfoVO> walletInfo() {
        return ok(customerWalletInfoMapper.selectById2VO(LoginCustomerUtil.getId(), CustomerWalletInfoVO.class));
    }

    @Operation(summary = "钱包记录")
    @GetMapping("/record")
    public Result<Page<CustomerWalletRecordDO>> listWalletPage(CustomerWalletRecordQueryReq queryReq) {
        startPage();
        List<CustomerWalletRecordDO> recordList =
                customerWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<CustomerWalletRecordDO> buildWalletQueryWrapper(CustomerWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<CustomerWalletRecordDO>()
                .like(
                        StringUtil.isNotBlank(queryReq.getRecordType()),
                        CustomerWalletRecordDO::getRecordType,
                        queryReq.getRecordType())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        CustomerWalletRecordDO::getCreateAt,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(CustomerWalletRecordDO::getRecordId);
    }

}
