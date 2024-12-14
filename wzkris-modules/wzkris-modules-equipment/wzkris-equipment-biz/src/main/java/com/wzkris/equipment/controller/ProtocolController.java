package com.wzkris.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.Protocol;
import com.wzkris.equipment.domain.req.EditStatusReq;
import com.wzkris.equipment.domain.req.ProtocolQueryReq;
import com.wzkris.equipment.mapper.ProtocolMapper;
import com.wzkris.equipment.service.ProtocolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 协议接口
 *
 * @author wzkris
 * @since 2024-11-19 16:31:40
 */
@Tag(name = "协议管理")
@RestController
@RequestMapping("/protocol")
@RequiredArgsConstructor
public class ProtocolController extends BaseController {
    private final ProtocolMapper protocolMapper;
    private final ProtocolService protocolService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('protocol:list')")
    public Result<Page<Protocol>> listPage(ProtocolQueryReq queryReq) {
        startPage();
        List<Protocol> list = protocolMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<Protocol> buildQueryWrapper(ProtocolQueryReq queryReq) {
        return new LambdaQueryWrapper<Protocol>()
                .select(Protocol.class, fieldInfo -> !fieldInfo.getColumn().equals("content") && !fieldInfo.getColumn().equals("parameter")) // 不查协议内容
                .like(StringUtil.isNotBlank(queryReq.getPtcName()), Protocol::getPtcName, queryReq.getPtcName())
                .like(StringUtil.isNotBlank(queryReq.getPtcVersion()), Protocol::getPtcVersion, queryReq.getPtcVersion())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), Protocol::getStatus, queryReq.getStatus());
    }

    @Operation(summary = "协议选择列表(不带分页)")
    @GetMapping("/selectlist")
    @PreAuthorize("@ps.hasPermsOr('product:add', 'product:edit')")// 产品添加修改时使用
    public Result<List<Protocol>> selectList(String ptcName) {
        LambdaQueryWrapper<Protocol> lqw = new LambdaQueryWrapper<Protocol>()
                .select(Protocol::getPtcId, Protocol::getPtcName)
                .eq(Protocol::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(ptcName), Protocol::getPtcName, ptcName);
        List<Protocol> list = protocolMapper.selectList(lqw);
        return ok(list);
    }

    @Operation(summary = "详情")
    @GetMapping("/{ptcId}")
    @PreAuthorize("@ps.hasPerms('protocol:query')")
    public Result<Protocol> queryById(@PathVariable Long ptcId) {
        return ok(protocolMapper.selectById(ptcId));
    }

    @Operation(summary = "添加协议")
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('protocol:add')")
    public Result<?> add(@RequestBody Protocol protocol) {
        return toRes(protocolMapper.insert(protocol));
    }

    @Operation(summary = "修改协议")
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('protocol:edit')")
    public Result<?> edit(@RequestBody Protocol protocol) {
        return toRes(protocolMapper.updateById(protocol));
    }

    @Operation(summary = "修改协议状态")
    @PostMapping("/edit_status")
    @PreAuthorize("@ps.hasPerms('protocol:edit')")
    public Result<?> editStatus(@RequestBody EditStatusReq statusReq) {
        Protocol update = new Protocol(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(protocolMapper.updateById(update));
    }

    @Operation(summary = "删除协议")
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('protocol:remove')")
    public Result<?> deleteById(@RequestBody Long ptcId) {
        if (protocolService.checkProtocolUsed(ptcId)) {
            return fail("删除失败，该协议被产品使用中");
        }
        return toRes(protocolMapper.deleteById(ptcId));
    }

}
