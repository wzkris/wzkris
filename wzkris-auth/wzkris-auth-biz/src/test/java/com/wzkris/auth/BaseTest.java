package com.wzkris.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/8/28 16:47
 */
public class BaseTest implements Controller {

    @Test
    public void sliceReadFile() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(new File("C:\\Users\\wz\\Downloads\\log"), "r")) {
            // 定位到文件的起始位置
            long startPosition = 0;
            raf.seek(startPosition);

            // 创建一个缓冲区来读取数据
            byte[] buffer = new byte[1024];// 1 KB
            int bytesRead;

            Map<Character, Integer> charFrequencyMap = new HashMap<>(1024);

            while ((bytesRead = raf.read(buffer)) != -1) {
                // 在这里处理读取到的数据，可以将数据写入另一个文件或进行其他操作
                for (int i = 0; i < bytesRead; i++) {
                    char c = (char) buffer[i];
                    charFrequencyMap.put(c, charFrequencyMap.getOrDefault(c, 0) + 1);
                }
            }
            System.out.println("数量：" + charFrequencyMap.size());

            // 找到前100个最高频率的字符
            List<Map.Entry<Character, Integer>> sortedEntries = new ArrayList<>(charFrequencyMap.entrySet());
            sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            List<Map.Entry<Character, Integer>> top100 = sortedEntries.subList(0, Math.min(100, sortedEntries.size()));

            for (Map.Entry<Character, Integer> entry : top100) {
                char character = entry.getKey();
                int frequency = entry.getValue();
                System.out.println(character + ": " + frequency);
            }
        }
    }

    @Test
    public void palindromeTest() {
        BigDecimal totalElecMoney = BigDecimal.valueOf(0.6987);
        BigDecimal totalSerMoney = BigDecimal.valueOf(0.5678);
        totalElecMoney = totalElecMoney.add(totalElecMoney.multiply(BigDecimal.valueOf(0))).setScale(2, RoundingMode.HALF_UP);
        totalSerMoney = totalSerMoney.add(totalSerMoney.multiply(BigDecimal.valueOf(0))).setScale(2, RoundingMode.HALF_UP);
        System.out.println(totalElecMoney);
        System.out.println(totalSerMoney);
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> lists = new ArrayList<>();
        int i = 0;
        int j = 1;
        int k = 2;
        int tmp;
        for (tmp = 2; tmp < nums.length; tmp++) {
            if (nums[i] + nums[j] + nums[k] == 0) {
                lists.add(Arrays.asList(nums[i], nums[j], nums[k]));
            }
            if (tmp + 1 == nums.length) {
                k = tmp;
                tmp = j;
            }
            if (tmp + 2 == nums.length) {
                j = tmp;
                tmp = i;
            }
            if (tmp + 3 == nums.length) {
                break;
            }
        }
        return lists;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
