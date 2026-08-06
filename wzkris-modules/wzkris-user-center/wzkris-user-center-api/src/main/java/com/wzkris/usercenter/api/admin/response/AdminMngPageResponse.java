package com.wzkris.usercenter.api.admin.response;

import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 管理员分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link AdminMngQueryResponse}，补充列表展示所需的部门跨表字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminMngPageResponse extends AdminMngQueryResponse {

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门状态")
    private DeptStatusEnum deptStatus;

}
