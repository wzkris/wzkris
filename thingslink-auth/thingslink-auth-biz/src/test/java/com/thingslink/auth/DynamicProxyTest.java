package com.thingslink.auth;

import com.thingslink.auth.domain.SysUser;
import com.thingslink.auth.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 动态代理
 * @date : 2023/9/25 16:19
 */
@SpringBootTest
public class DynamicProxyTest {

    @Autowired
    private SysUserMapper userMapper;

    @Test
    public void test() {
        List<SysUser> list = new ArrayList<>();
        SysUser user1 = new SysUser().setUsername("test111");
        SysUser user2 = new SysUser().setUsername("test222");
        list.add(user1);
        list.add(user2);
        int rows = userMapper.insertBatch(list);
        System.out.println(rows);
        userMapper.deleteBatchIds(List.of(user1.getUserId(), user2.getUserId()));
    }

    @Test
    public void test1() {
        List<SysUser> users = userMapper.selectList(null);
        System.out.println(users);
    }

}
