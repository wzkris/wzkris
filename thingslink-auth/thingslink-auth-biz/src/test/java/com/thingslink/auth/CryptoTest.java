package com.thingslink.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@DisplayName("密码测试")
public class CryptoTest {

    @Test
    @DisplayName("加密测试")
    public void encrypt() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin123"));
    }

    static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Test
    public void test1() {
        threadLocal.set("123");
        threadLocal.set("321");
        System.out.println(threadLocal.get());
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
