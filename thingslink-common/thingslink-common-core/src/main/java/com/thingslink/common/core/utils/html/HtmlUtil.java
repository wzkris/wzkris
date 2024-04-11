package com.thingslink.common.core.utils.html;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.util.HtmlUtils;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : html工具
 * @date : 2023/9/8 15:35
 */
public class HtmlUtil extends HtmlUtils {
    public static final String NBSP = StrUtil.HTML_NBSP;
    public static final String AMP = StrUtil.HTML_AMP;
    public static final String QUOTE = StrUtil.HTML_QUOTE;
    public static final String APOS = StrUtil.HTML_APOS;
    public static final String LT = StrUtil.HTML_LT;
    public static final String GT = StrUtil.HTML_GT;

    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";
    public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

    private static final char[][] TEXT = new char[256][];

    static {
        // ascii码值最大的是【0x7f=127】，扩展ascii码值最大的是【0xFF=255】，因为ASCII码使用指定的7位或8位二进制数组合来表示128或256种可能的字符，标准ASCII码也叫基础ASCII码。
        for (int i = 0; i < 256; i++) {
            TEXT[i] = new char[]{(char) i};
        }

        // special HTML characters
        TEXT['\''] = "&#039;".toCharArray(); // 单引号 ('&apos;' doesn't work - it is not by the w3 specs)
        TEXT['"'] = QUOTE.toCharArray(); // 双引号
        TEXT['&'] = AMP.toCharArray(); // &符
        TEXT['<'] = LT.toCharArray(); // 小于号
        TEXT['>'] = GT.toCharArray(); // 大于号
        TEXT[' '] = NBSP.toCharArray(); // 不断开空格（non-breaking space，缩写nbsp。ASCII值是32：是用键盘输入的空格；ASCII值是160：不间断空格，即 &nbsp，所产生的空格，作用是在页面换行时不被打断）
    }


    // ---------------------------------------------------------------- encode text

    /**
     * 清除所有HTML标签，但是不删除标签内的内容
     *
     * @param content 文本
     * @return 清除标签后的文本
     */
    public static String cleanHtmlTag(String content) {
        return content.replaceAll(RE_HTML_MARK, "");
    }

    /**
     * 清除指定HTML标签和被标签包围的内容<br>
     * 不区分大小写
     *
     * @param content  文本
     * @param tagNames 要清除的标签
     * @return 去除标签后的文本
     */
    public static String removeHtmlTag(String content, String... tagNames) {
        return removeHtmlTag(content, true, tagNames);
    }

    /**
     * 清除指定HTML标签，不包括内容<br>
     * 不区分大小写
     *
     * @param content  文本
     * @param tagNames 要清除的标签
     * @return 去除标签后的文本
     */
    public static String unwrapHtmlTag(String content, String... tagNames) {
        return removeHtmlTag(content, false, tagNames);
    }

    /**
     * 清除指定HTML标签<br>
     * 不区分大小写
     *
     * @param content        文本
     * @param withTagContent 是否去掉被包含在标签中的内容
     * @param tagNames       要清除的标签
     * @return 去除标签后的文本
     */
    public static String removeHtmlTag(String content, boolean withTagContent, String... tagNames) {
        String regex;
        for (String tagName : tagNames) {
            if (StrUtil.isBlank(tagName)) {
                continue;
            }
            tagName = tagName.trim();
            // (?i)表示其后面的表达式忽略大小写
            if (withTagContent) {
                // 标签及其包含内容
                regex = StrUtil.format("(?i)<{}(\\s+[^>]*?)?/?>(.*?</{}>)?", tagName, tagName);
            } else {
                // 标签不包含内容
                regex = StrUtil.format("(?i)<{}(\\s+[^>]*?)?/?>|</?{}>", tagName, tagName);
            }

            content = ReUtil.delAll(regex, content); // 非自闭标签小写
        }
        return content;
    }

    /**
     * 去除HTML标签中的属性，如果多个标签有相同属性，都去除
     *
     * @param content 文本
     * @param attrs   属性名（不区分大小写）
     * @return 处理后的文本
     */
    public static String removeHtmlAttr(String content, String... attrs) {
        String regex;
        for (String attr : attrs) {
            // (?i)     表示忽略大小写
            // \s*      属性名前后的空白符去除
            // [^>]+?   属性值，至少有一个非>的字符，>表示标签结束
            // \s+(?=>) 表示属性值后跟空格加>，即末尾的属性，此时去掉空格
            // (?=\s|>) 表示属性值后跟空格（属性后还有别的属性）或者跟>（最后一个属性）
            regex = StrUtil.format("(?i)(\\s*{}\\s*=[^>]+?\\s+(?=>))|(\\s*{}\\s*=[^>]+?(?=\\s|>))", attr, attr);
            content = content.replaceAll(regex, StrUtil.EMPTY);
        }
        return content;
    }

    /**
     * 去除指定标签的所有属性
     *
     * @param content  内容
     * @param tagNames 指定标签
     * @return 处理后的文本
     */
    public static String removeAllHtmlAttr(String content, String... tagNames) {
        String regex;
        for (String tagName : tagNames) {
            regex = StrUtil.format("(?i)<{}[^>]*?>", tagName);
            content = content.replaceAll(regex, StrUtil.format("<{}>", tagName));
        }
        return content;
    }

    /**
     * Encoder
     *
     * @param text 被编码的文本
     * @return 编码后的字符
     */
    private static String encode(String text) {
        int len;
        if ((text == null) || ((len = text.length()) == 0)) {
            return StrUtil.EMPTY;
        }
        StringBuilder buffer = new StringBuilder(len + (len >> 2));
        char c;
        for (int i = 0; i < len; i++) {
            c = text.charAt(i);
            if (c < 256) {
                buffer.append(TEXT[c]);
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
