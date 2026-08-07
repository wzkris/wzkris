package com.wzkris.payment.impl.notify;

/**
 * 回调业务处理结果：{@link Ok} 流转成功（含已终态幂等），{@link Reject} 拒绝并携带原因。
 *
 * <p>替代 boolean：避免「true=刚成功/已终态、false=不存在/金额不符/未支付」语义过载，
 * 拒绝原因上浮，模板不再用笼统一句错误串。
 *
 * @author wzkris
 */
public sealed interface ProcessResult permits ProcessResult.Ok, ProcessResult.Reject {

    static Ok ok() {
        return new Ok();
    }

    static Reject reject(String reason) {
        return new Reject(reason);
    }

    /** 处理成功（含已终态幂等） */
    record Ok() implements ProcessResult {
    }

    /** 拒绝处理，携带具体原因 */
    record Reject(String reason) implements ProcessResult {
    }

}
