package com.wzkris.auth;

import cn.hutool.core.io.checksum.CRC16;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@DisplayName("密码测试")
public class CryptoTest {

    static String str = "wzsss";

    @Test
    public void encrypt01() {
        BigDecimal total_sevice_money = new BigDecimal("764.1700");
        BigDecimal total_elec_money = new BigDecimal("2308.7200");
        BigDecimal total = total_sevice_money.add(total_elec_money);

        BigDecimal total_settlement = new BigDecimal("3051.9900");

        BigDecimal serv = total.subtract(total_settlement).divide(total, 30, RoundingMode.HALF_UP).multiply(total_sevice_money);
        BigDecimal elec = total.subtract(total_settlement).divide(total, 30, RoundingMode.HALF_UP).multiply(total_elec_money);
        System.out.println(serv);
        System.out.println(elec);
    }

    @Test
    @DisplayName("redis_slot算法")
    public void encrypt() {
        CRC16 crc16 = new CRC16();
        crc16.update("user:inf2".getBytes(StandardCharsets.UTF_8));
        long slot = crc16.getValue() & 16383;
        System.out.println(slot);
    }

    public List<List<Integer>> threeSum(int[] nums) {
        HashSet<List<Integer>> list = new HashSet<>();
        Arrays.sort(nums);

        for (int n = 0; n < nums.length; n++) {
            if (nums[n] > 0) {
                break;
            }

            if (n > 0 && nums[n] == nums[n - 1]) {
                continue;
            }

            int l = n + 1;
            int r = nums.length - 1;

            while (l < r) {
                if (nums[n] + nums[l] + nums[r] == 0) {
                    list.add(List.of(nums[n], nums[l], nums[r]));
                    l++;
                    r--;
                }
                else if (nums[n] + nums[l] + nums[r] > 0) {
                    r--;
                }
                else if (nums[n] + nums[l] + nums[r] < 0) {
                    l++;
                }
            }
        }
        return list.stream().toList();
    }

    public int maxArea(int[] height) {
        int max = 0;
        for (int i = 0, j = height.length - 1; i < height.length && i < j; ) {
            if (height[i] < height[j]) {
                int tmp = Math.abs(i - j) * height[i];
                if (tmp > max) {
                    max = tmp;
                }
                i++;
            }
            else {
                int tmp = Math.abs(i - j) * height[j];
                if (tmp > max) {
                    max = tmp;
                }
                j--;
            }
        }
        return max;
    }
}
