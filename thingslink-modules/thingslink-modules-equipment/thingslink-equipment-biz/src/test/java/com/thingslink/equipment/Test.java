package com.thingslink.equipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.thingslink.equipment.Thingslink_EquipmentApplication;
import com.thingslink.equipment.mapper.DeviceMapper;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/6/10 13:56
 */
@SpringBootTest(classes = Thingslink_EquipmentApplication.class)
public class Test {
    @Autowired
    private DeviceMapper deviceMapper;

    @org.junit.jupiter.api.Test
    public void test() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        System.out.println(cpuNum);
    }
}
