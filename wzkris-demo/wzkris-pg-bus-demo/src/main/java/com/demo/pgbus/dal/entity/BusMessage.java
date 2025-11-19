package com.demo.pgbus.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.demo.pgbus.dal.enums.MessageStatus;
import com.wzkris.common.orm.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demo_bus_message")
public class BusMessage extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String channel;

    private String title;

    private String payload;

    private MessageStatus status;

}

