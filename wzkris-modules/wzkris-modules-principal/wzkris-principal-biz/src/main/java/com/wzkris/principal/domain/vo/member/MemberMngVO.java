package com.wzkris.principal.domain.vo.member;

import com.wzkris.principal.domain.MemberInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 租户成员展示层对象
 * @date : 2025/10/9 14:00
 */
@Data
@AutoMapper(target = MemberInfoDO.class)
@NoArgsConstructor
public class MemberMngVO extends MemberInfoDO {

    @Schema(description = "职位名称")
    private String postName;

}
