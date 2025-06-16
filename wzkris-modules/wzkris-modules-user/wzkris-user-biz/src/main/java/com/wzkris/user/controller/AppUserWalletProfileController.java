package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.AppUserWalletRecord;
import com.wzkris.user.domain.req.AppUserWalletRecordQueryReq;
import com.wzkris.user.domain.vo.AppUserWalletVO;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.mapper.AppUserWalletRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户钱包信息
 *
 * @author wzkris
 */
@Tag(name = "app钱包")
@Slf4j
@Validated
@RestController
@RequestMapping("/app/user_wallet")
@RequiredArgsConstructor
public class AppUserWalletProfileController extends BaseController {

    private final AppUserWalletMapper appUserWalletMapper;

    private final AppUserWalletRecordMapper appUserWalletRecordMapper;

    @Operation(summary = "余额信息")
    @GetMapping
    public Result<AppUserWalletVO> walletInfo() {
        return ok(appUserWalletMapper.selectById2VO(ClientUserUtil.getUserId(), AppUserWalletVO.class));
    }

    @Operation(summary = "钱包记录")
    @GetMapping("/record")
    public Result<Page<AppUserWalletRecord>> listWalletPage(AppUserWalletRecordQueryReq queryReq) {
        startPage();
        List<AppUserWalletRecord> recordList =
                appUserWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<AppUserWalletRecord> buildWalletQueryWrapper(AppUserWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<AppUserWalletRecord>()
                .like(
                        StringUtil.isNotBlank(queryReq.getRecordType()),
                        AppUserWalletRecord::getRecordType,
                        queryReq.getRecordType())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        AppUserWalletRecord::getCreateAt,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(AppUserWalletRecord::getRecordId);
    }
}
