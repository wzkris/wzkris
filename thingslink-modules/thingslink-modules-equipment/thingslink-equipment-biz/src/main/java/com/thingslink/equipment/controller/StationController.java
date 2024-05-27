package com.thingslink.equipment.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.equipment.domain.Station;
import com.thingslink.equipment.domain.dto.LocationDTO;
import com.thingslink.equipment.domain.vo.StationVO;
import com.thingslink.equipment.mapper.StationMapper;
import com.thingslink.equipment.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 站点
 * @date : 2023/6/5 15:45
 */
@Tag(name = "站点管理")
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController extends BaseController {

    private final StationMapper stationMapper;
    private final StationService stationService;

    @Operation(summary = "(匿名接口)根据经纬度定位最近电站")
    @GetMapping("/open/list")
    public Result<Page<StationVO>> stationList(@Valid LocationDTO locationDto) {
        startPage();
        List<StationVO> list = stationMapper.listVOByLocation(locationDto);
        return getDataTable(list);
    }

    /**
     * 分页查询
     *
     * @param station 筛选条件
     * @return 查询结果
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('station:list')")
    public Result<Page<Station>> listPage(Station station) {
        startPage();
        List<Station> list = stationService.list(station);
        return getDataTable(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param stationId 主键
     * @return 单条数据
     */
    @GetMapping("/{stationId}")
    @PreAuthorize("@ps.hasPerms('station:query')")
    public Result<Station> query(@PathVariable Long stationId) {
        return success(stationMapper.selectById(stationId));
    }

    /**
     * 新增数据
     *
     * @param station 实体
     * @return 新增结果
     */
    @PostMapping
    @PreAuthorize("@ps.hasPerms('station:add')")
    public Result<?> add(@RequestBody Station station) {
        return toRes(stationMapper.insert(station));
    }

    /**
     * 编辑数据
     *
     * @param station 实体
     * @return 编辑结果
     */
    @PutMapping
    @PreAuthorize("@ps.hasPerms('station:edit')")
    public Result<?> edit(@RequestBody Station station) {
        return toRes(stationMapper.updateById(station));
    }

    /**
     * 删除数据
     *
     * @param stationId 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{stationId}")
    @PreAuthorize("@ps.hasPerms('station:remove')")
    public Result<?> deleteById(@PathVariable Long stationId) {
        return toRes(stationMapper.deleteById(stationId));
    }
}
