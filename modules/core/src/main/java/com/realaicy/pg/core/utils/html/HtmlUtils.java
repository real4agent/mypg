package com.realaicy.pg.core.utils.html;

import com.realaicy.pg.core.utils.html.jsoup.HtmlCleaner;
import com.realaicy.pg.core.utils.html.jsoup.HtmlWhitelist;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * html工具类
 * 主要作用：标签转义、提取摘要、删除不安全的tag（比如<script></script>）
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class HtmlUtils {

    /**
     * 获取html文档中的文本
     *
     * @return 处理过的html文档中的文本
     */
    public static String text(String html) {
        if (StringUtils.isEmpty(html)) {
            return html;
        }
        return Jsoup.parse(removeUnSafeTag(html).replace("&lt;", "<").replace("&gt;", ">")).text();
    }

    /**
     * 获取html文档中的文本 并仅提取文本中的前maxLength个 超出部分使用……补充
     *
     * @param html 待处理的html字符串
     * @param maxLength 显示的长度
     * @return 处理过的html文档中的文本
     */
    public static String text(String html, int maxLength) {
        String text = text(html);
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "……";
    }

    /**
     * 从html中移除不安全tag
     *
     * @param html 待处理的html字符串
     * @return 从html中移除不安全tag之后的字符串
     */
    public static String removeUnSafeTag(String html) {
        HtmlWhitelist whitelist = new HtmlWhitelist();
        whitelist.addTags("embed", "object", "param", "span", "div", "img", "font", "del");
        whitelist.addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup");
        whitelist.addTags("dd", "dl", "dt", "em", "hr", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img");
        whitelist.addTags("li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub", "sup", "table");
        whitelist.addTags("tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul");

        //增加以on开头的（事件）
        whitelist.addAttributes(":all", "on");
        Document dirty = Jsoup.parseBodyFragment(html, "");
        HtmlCleaner cleaner = new HtmlCleaner(whitelist);
        Document clean = cleaner.clean(dirty);

        return clean.body().html();
    }

    /**
     * 删除指定标签
     *
     * @param html 待处理的html字符串
     * @param tagName 需要移除的tag名称
     * @return 处理之后的html字符串
     */
    public static String removeTag(String html, String tagName) {
        Element bodyElement = Jsoup.parse(html).body();
        bodyElement.getElementsByTag(tagName).remove();
        return bodyElement.html();
    }
}
