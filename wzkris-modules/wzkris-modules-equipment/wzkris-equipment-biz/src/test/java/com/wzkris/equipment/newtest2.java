package com.wzkris.equipment;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.orm.annotation.CheckFieldPerms;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.mapper.DeviceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SpringBootTest
public class newtest2 {

    @Autowired
    private DeviceMapper deviceMapper;

    @Test
    public void test() {
        System.out.println(SpringUtil.getFactory().getBean(this.getClass()).list());
    }

    @CheckFieldPerms(value = "@ps.hasPerms('123')", rw = CheckFieldPerms.Perms.READ)
    public List<Device> list() {
        return deviceMapper.selectList(null);
    }

}
