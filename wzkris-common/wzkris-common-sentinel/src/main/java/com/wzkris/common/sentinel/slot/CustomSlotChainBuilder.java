package com.wzkris.common.sentinel.slot;

import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.SlotChainBuilder;
import com.alibaba.csp.sentinel.slots.DefaultSlotChainBuilder;
import com.wzkris.common.sentinel.slot.processor.FlowWarningProcessorSlot;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义插槽配置
 * @date : 2023/5/19 13:42
 */
public class CustomSlotChainBuilder implements SlotChainBuilder {

    @Override
    public ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultSlotChainBuilder().build();
        chain.addLast(new FlowWarningProcessorSlot());
        return chain;
    }

}
