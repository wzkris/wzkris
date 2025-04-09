package com.wzkris.user;

import com.wzkris.common.orm.utils.DynamicTenantUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class PatternTest {

    @Test
    public void test() {
        try {
            DynamicTenantUtil.enableIgnore();
            try {
                DynamicTenantUtil.enableIgnore();
            } finally {
                DynamicTenantUtil.disableIgnore();
            }
        } finally {
            DynamicTenantUtil.disableIgnore();
        }
    }

}
