package com.wzkris.usercenter.mapper.serviceimpl;

import com.wzkris.usercenter.domain.MenuInfoDO;
import com.wzkris.usercenter.service.impl.MenuInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "菜单构建树节点正确性测试")
public class MenuInfoServiceImpl_CorrectnessTest {

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

    @Test
    @DisplayName("测试正常树形结构构建")
    void testBuildTree_NormalTreeStructure() {
        // Given: 构建一个完整的树结构
        List<MenuInfoDO> menus = Arrays.asList(
                createMenu(1L, 0L, "根节点"),
                createMenu(2L, 1L, "子节点1"),
                createMenu(3L, 1L, "子节点2"),
                createMenu(4L, 2L, "孙子节点1"),
                createMenu(5L, 2L, "孙子节点2"),
                createMenu(6L, 3L, "孙子节点3")
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size(), "应该只有一个根节点");

        MenuInfoDO root = result.get(0);
        assertEquals(1L, root.getMenuId());
        assertNotNull(root.getChildren());
        assertEquals(2, root.getChildren().size(), "根节点应该有2个直接子节点");

        // 验证第一层子节点
        MenuInfoDO child1 = findChildById(root, 2L);
        MenuInfoDO child2 = findChildById(root, 3L);
        assertNotNull(child1);
        assertNotNull(child2);

        // 验证第二层子节点
        assertEquals(2, child1.getChildren().size(), "节点2应该有2个子节点");
        assertEquals(1, child2.getChildren().size(), "节点3应该有1个子节点");

        // 验证孙子节点
        assertTrue(hasChildWithId(child1, 4L));
        assertTrue(hasChildWithId(child1, 5L));
        assertTrue(hasChildWithId(child2, 6L));
    }

    @Test
    @DisplayName("测试多个根节点")
    void testBuildTree_MultipleRoots() {
        // Given: 多个独立的树
        List<MenuInfoDO> menus = Arrays.asList(
                createMenu(1L, 0L, "根节点1"),
                createMenu(2L, 0L, "根节点2"),
                createMenu(3L, 1L, "子节点1-1"),
                createMenu(4L, 2L, "子节点2-1"),
                createMenu(5L, 0L, "根节点3")  // 独立的根节点
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size(), "应该有3个根节点");

        // 验证每个根节点
        assertTrue(result.stream().anyMatch(node -> node.getMenuId() == 1L));
        assertTrue(result.stream().anyMatch(node -> node.getMenuId() == 2L));
        assertTrue(result.stream().anyMatch(node -> node.getMenuId() == 5L));

        // 验证根节点的子节点
        MenuInfoDO root1 = findNodeById(result, 1L);
        MenuInfoDO root2 = findNodeById(result, 2L);
        MenuInfoDO root3 = findNodeById(result, 5L);

        assertEquals(1, root1.getChildren().size());
        assertEquals(1, root2.getChildren().size());
        assertTrue(root3.getChildren() == null || root3.getChildren().isEmpty());
    }

    @Test
    @DisplayName("测试空列表输入")
    void testBuildTree_EmptyList() {
        // Given
        List<MenuInfoDO> emptyList = Collections.emptyList();

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(emptyList);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty(), "空列表输入应该返回空列表");
    }

    @Test
    @DisplayName("测试null输入")
    void testBuildTree_NullInput() {
        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty(), "null输入应该返回空列表");
    }

    @Test
    @DisplayName("测试单节点树")
    void testBuildTree_SingleNode() {
        // Given
        List<MenuInfoDO> singleNode = Collections.singletonList(
                createMenu(1L, 0L, "单节点")
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(singleNode);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        MenuInfoDO root = result.get(0);
        assertEquals(1L, root.getMenuId());
        assertTrue(root.getChildren() == null || root.getChildren().isEmpty(),
                "单节点应该没有子节点");
    }

    @Test
    @DisplayName("测试所有节点都有父节点（无根节点）")
    void testBuildTree_NoRootNodes() {
        // Given: 所有节点都有有效的父节点，但没有真正的根节点
        List<MenuInfoDO> menus = Arrays.asList(
                createMenu(2L, 1L, "节点2"),  // parentId=1不存在
                createMenu(3L, 2L, "节点3"),
                createMenu(4L, 3L, "节点4")
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty(), "即使没有真正的根节点，也不应该返回空列表");

        // 根据fallback逻辑，应该返回原始列表或包含节点2的列表
        if (result.size() == 1) {
            // 如果节点2被识别为根节点
            assertEquals(2L, result.get(0).getMenuId());
        } else {
            // 否则应该返回原始列表
            assertEquals(3, result.size());
        }
    }

    @Test
    @DisplayName("测试深度嵌套结构")
    void testBuildTree_DeepNesting() {
        // Given: 深度嵌套的链式结构
        List<MenuInfoDO> menus = new ArrayList<>();
        menus.add(createMenu(1L, 0L, "根节点"));
        for (long i = 2; i <= 10; i++) {
            menus.add(createMenu(i, i - 1, "节点" + i));
        }

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        // 验证深度
        MenuInfoDO current = result.get(0);
        int depth = 1;
        while (current.getChildren() != null && !current.getChildren().isEmpty()) {
            current = current.getChildren().get(0);
            depth++;
        }

        assertEquals(10, depth, "应该有10层深度");
    }

    @Test
    @DisplayName("测试复杂分支结构")
    void testBuildTree_ComplexBranches() {
        // Given: 复杂的分支结构
        List<MenuInfoDO> menus = Arrays.asList(
                createMenu(1L, 0L, "根节点"),
                createMenu(2L, 1L, "分支1"),
                createMenu(3L, 1L, "分支2"),
                createMenu(4L, 2L, "分支1-子1"),
                createMenu(5L, 2L, "分支1-子2"),
                createMenu(6L, 3L, "分支2-子1"),
                createMenu(7L, 3L, "分支2-子2"),
                createMenu(8L, 4L, "分支1-子1-孙1"),
                createMenu(9L, 4L, "分支1-子1-孙2"),
                createMenu(10L, 5L, "分支1-子2-孙1")
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        MenuInfoDO root = result.get(0);
        assertEquals(2, root.getChildren().size());

        // 验证分支1的结构
        MenuInfoDO branch1 = findChildById(root, 2L);
        assertEquals(2, branch1.getChildren().size());

        MenuInfoDO branch1Child1 = findChildById(branch1, 4L);
        assertEquals(2, branch1Child1.getChildren().size());

        MenuInfoDO branch1Child2 = findChildById(branch1, 5L);
        assertEquals(1, branch1Child2.getChildren().size());

        // 验证分支2的结构
        MenuInfoDO branch2 = findChildById(root, 3L);
        assertEquals(2, branch2.getChildren().size());
        assertTrue(branch2.getChildren().stream().allMatch(
                child -> child.getChildren() == null || child.getChildren().isEmpty()));
    }

    @Test
    @DisplayName("测试重复菜单ID")
    void testBuildTree_DuplicateMenuIds() {
        // Given: 包含重复menuId的数据
        List<MenuInfoDO> menus = Arrays.asList(
                createMenu(1L, 0L, "根节点1"),
                createMenu(1L, 0L, "根节点2"),  // 重复ID
                createMenu(2L, 1L, "子节点")
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then: 行为取决于具体实现，但不应崩溃
        assertNotNull(result);
        // 可能的结果：去重、报错、或正常处理
    }

    @Test
    @DisplayName("测试父子关系一致性")
    void testBuildTree_ParentChildConsistency() {
        // Given
        List<MenuInfoDO> menus = Arrays.asList(
                createMenu(1L, 0L, "根节点"),
                createMenu(2L, 1L, "子节点1"),
                createMenu(3L, 1L, "子节点2"),
                createMenu(4L, 2L, "孙子节点")
        );

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then: 验证所有父子关系都正确建立
        MenuInfoDO root = result.get(0);
        assertTrue(verifyTreeStructure(root));
    }

    @Test
    @DisplayName("测试大数据量正确性")
    void testBuildTree_LargeDataCorrectness() {
        // Given: 生成1000个节点的测试数据
        List<MenuInfoDO> menus = generateTestData(1000, 5, 10);

        // When
        List<MenuInfoDO> result = menuInfoService.buildTree(menus);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // 验证所有节点都被正确处理
        Set<Long> allMenuIds = menus.stream().map(MenuInfoDO::getMenuId).collect(Collectors.toSet());
        Set<Long> processedIds = collectAllMenuIds(result);

        assertEquals(allMenuIds.size(), processedIds.size(), "所有节点都应该被处理");
        assertTrue(processedIds.containsAll(allMenuIds));
    }

    // 辅助方法
    private MenuInfoDO findNodeById(List<MenuInfoDO> nodes, Long menuId) {
        return nodes.stream()
                .filter(node -> node.getMenuId().equals(menuId))
                .findFirst()
                .orElse(null);
    }

    private MenuInfoDO findChildById(MenuInfoDO parent, Long childId) {
        if (parent.getChildren() == null) return null;
        return parent.getChildren().stream()
                .filter(child -> child.getMenuId().equals(childId))
                .findFirst()
                .orElse(null);
    }

    private boolean hasChildWithId(MenuInfoDO parent, Long childId) {
        return findChildById(parent, childId) != null;
    }

    private boolean verifyTreeStructure(MenuInfoDO node) {
        if (node.getChildren() != null) {
            for (MenuInfoDO child : node.getChildren()) {
                if (!child.getParentId().equals(node.getMenuId())) {
                    return false;
                }
                if (!verifyTreeStructure(child)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<Long> collectAllMenuIds(List<MenuInfoDO> nodes) {
        Set<Long> ids = new HashSet<>();
        for (MenuInfoDO node : nodes) {
            ids.add(node.getMenuId());
            if (node.getChildren() != null) {
                ids.addAll(collectAllMenuIds(node.getChildren()));
            }
        }
        return ids;
    }

    private List<MenuInfoDO> generateTestData(int totalNodes, int maxDepth, int maxChildren) {
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

}