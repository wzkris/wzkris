package com.wzkris.principal.serviceimpl;

import com.wzkris.principal.domain.MenuInfoDO;
import com.wzkris.principal.service.impl.MenuInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled(value = "性能测试构建时跳过")
@DisplayName(value = "菜单构建树节点性能测试")
public class MenuInfoServiceImpl_PerformanceTest {

    private MenuInfoServiceImpl menuInfoService;

    @BeforeEach
    void setUp() {
        menuInfoService = new MenuInfoServiceImpl(null, null, null,
                null, null, null, null);
    }

    private MenuInfoDO createMenu(Long menuId, Long parentId, String menuName) {
        MenuInfoDO menu = new MenuInfoDO();
        menu.setMenuId(menuId);
        menu.setParentId(parentId);
        menu.setMenuName(menuName);
        return menu;
    }

    /**
     * 生成性能测试数据
     *
     * @param totalNodes  总节点数量
     * @param maxDepth    最大深度
     * @param maxChildren 每个节点最大子节点数
     */
    private List<MenuInfoDO> generatePerformanceData(int totalNodes, int maxDepth, int maxChildren) {
        List<MenuInfoDO> menus = new ArrayList<>();
        Random random = new Random(42); // 固定种子保证可重复测试

        // 生成根节点
        int rootCount = Math.max(1, totalNodes / 100);
        for (int i = 1; i <= rootCount; i++) {
            menus.add(createMenu((long) i, 0L, "根节点" + i));
        }

        // 生成子节点
        long nextId = rootCount + 1;
        int currentLevel = 1;
        List<Long> parentIds = new ArrayList<>();
        for (int i = 1; i <= rootCount; i++) {
            parentIds.add((long) i);
        }

        while (nextId <= totalNodes && currentLevel <= maxDepth) {
            List<Long> newParentIds = new ArrayList<>();
            for (Long parentId : parentIds) {
                if (nextId > totalNodes) break;

                int childrenCount = random.nextInt(maxChildren) + 1;
                for (int j = 0; j < childrenCount; j++) {
                    if (nextId > totalNodes) break;

                    menus.add(createMenu(nextId, parentId, "菜单" + nextId));
                    newParentIds.add(nextId);
                    nextId++;
                }
            }
            parentIds = newParentIds;
            currentLevel++;
        }

        return menus;
    }

    /**
     * 生成线性链式数据（最坏情况）
     */
    private List<MenuInfoDO> generateWorstCaseData(int totalNodes) {
        List<MenuInfoDO> menus = new ArrayList<>();
        menus.add(createMenu(1L, 0L, "根节点"));
        for (long i = 2; i <= totalNodes; i++) {
            menus.add(createMenu(i, i - 1, "菜单" + i));
        }
        return menus;
    }

    /**
     * 生成平衡树数据
     */
    private List<MenuInfoDO> generateBalancedTreeData(int totalNodes, int childrenPerNode) {
        List<MenuInfoDO> menus = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();

        // 根节点
        menus.add(createMenu(1L, 0L, "根节点"));
        queue.offer(1L);

        long nextId = 2;
        while (!queue.isEmpty() && nextId <= totalNodes) {
            Long parentId = queue.poll();
            for (int i = 0; i < childrenPerNode; i++) {
                if (nextId > totalNodes) break;
                menus.add(createMenu(nextId, parentId, "菜单" + nextId));
                queue.offer(nextId);
                nextId++;
            }
        }

        return menus;
    }

    @Test
    @DisplayName("性能测试 - 小数据量 (5000节点)")
    void testPerformance_SmallData() {
        // Given
        List<MenuInfoDO> menus = generatePerformanceData(5000, 4, 5);
        System.out.println("测试数据: 5000节点");

        // When & Then
        long startTime = System.nanoTime();
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);
        long endTime = System.nanoTime();

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("执行时间: " + durationMs + " ms");
        System.out.println("构建节点数: " + result.size());

        assertNotNull(result);
        assertTrue(durationMs < 1000, "小数据量应该在1秒内完成");
    }

    @Test
    @DisplayName("性能测试 - 中等数据量 (50,000节点)")
    void testPerformance_MediumData() {
        // Given
        List<MenuInfoDO> menus = generatePerformanceData(50_000, 5, 8);
        System.out.println("测试数据: 50,000节点");

        // When & Then
        long startTime = System.nanoTime();
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);
        long endTime = System.nanoTime();

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("执行时间: " + durationMs + " ms");
        System.out.println("构建节点数: " + result.size());

        assertNotNull(result);
        assertTrue(durationMs < 1000, "中等数据量应该在1秒内完成");
    }

    @Test
    @DisplayName("性能测试 - 大数据量 (1,000,000节点)")
    void testPerformance_LargeData() {
        // Given
        List<MenuInfoDO> menus = generatePerformanceData(1_000_000, 6, 10);
        System.out.println("测试数据: 1,000,000节点");

        // When & Then
        long startTime = System.nanoTime();
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);
        long endTime = System.nanoTime();

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("执行时间: " + durationMs + " ms");
        System.out.println("构建节点数: " + result.size());

        assertNotNull(result);
        assertTrue(durationMs < 1000, "大数据量应该在1秒内完成");
    }

    @Test
    @DisplayName("性能测试 - 最坏情况 (线性链式)")
    void testPerformance_WorstCase() {
        // Given
        List<MenuInfoDO> menus = generateWorstCaseData(5000);
        System.out.println("测试数据: 5000节点线性链式(最坏情况)");

        // When & Then
        long startTime = System.nanoTime();
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);
        long endTime = System.nanoTime();

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("执行时间: " + durationMs + " ms");
        System.out.println("构建节点数: " + result.size());

        assertNotNull(result);
    }

    @Test
    @DisplayName("性能测试 - 平衡树")
    void testPerformance_BalancedTree() {
        // Given
        List<MenuInfoDO> menus = generateBalancedTreeData(5000, 4);
        System.out.println("测试数据: 5000节点平衡树");

        // When & Then
        long startTime = System.nanoTime();
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);
        long endTime = System.nanoTime();

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("执行时间: " + durationMs + " ms");
        System.out.println("构建节点数: " + result.size());

        assertNotNull(result);
    }

    @Test
    @DisplayName("性能对比测试 - 优化前后对比")
    void testPerformance_Comparison() {
        // 测试不同数据量下的性能
        int[] dataSizes = {10000, 50000, 100000, 200000, 500000};

        System.out.println("数据量\t执行时间(ms)");
        System.out.println("-------------------");

        for (int size : dataSizes) {
            List<MenuInfoDO> menus = generatePerformanceData(size, 5, 5);

            long startTime = System.nanoTime();
            List<MenuInfoDO> result = menuInfoService.buildTree(menus);
            long endTime = System.nanoTime();

            long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            System.out.println(size + "\t" + durationMs);

            assertNotNull(result);
        }
    }

    @Test
    @DisplayName("压力测试 - 超大数据量")
    void testStress_Performance() {
        // 谨慎使用，根据机器性能调整
        int[] stressSizes = {5_000_000, 1_000_000};

        for (int size : stressSizes) {
            System.out.println("压力测试: " + size + " 节点");

            List<MenuInfoDO> menus = generatePerformanceData(size, 7, 5);

            long startTime = System.nanoTime();
            List<MenuInfoDO> result = menuInfoService.buildTree(menus);
            long endTime = System.nanoTime();

            long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            System.out.println("数据量: " + size + ", 执行时间: " + durationMs + " ms");

            assertNotNull(result);
            System.out.println("内存使用: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + " MB");
        }
    }

}