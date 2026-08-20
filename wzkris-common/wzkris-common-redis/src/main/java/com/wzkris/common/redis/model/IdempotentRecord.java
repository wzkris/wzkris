package com.wzkris.common.redis.model;

import com.wzkris.common.redis.enums.IdempotentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 幂等缓存记录
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdempotentRecord implements Serializable {

    private IdempotentStatusEnum status;

    private Object result;

    public static IdempotentRecord processing() {
        return new IdempotentRecord(IdempotentStatusEnum.PROCESSING, null);
    }

    public static IdempotentRecord done(Object result) {
        return new IdempotentRecord(IdempotentStatusEnum.DONE, result);
    }

}
