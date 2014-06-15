package com.realaicy.pg.core.utils.html;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class HtmlUtilsTest {

    @Test
    public void testHtml2Text() {
        String html = "<a>你好</a>&lt;a&gt;你好&lt;/a&gt;";

        Assert.assertEquals("你好你好", HtmlUtils.text(html));
    }

    @Test
    public void testHtml2TextWithMaxLength() {
        String html = "<a>你好</a>&lt;a&gt;你好&lt;/a&gt;";

        Assert.assertEquals("你好……", HtmlUtils.text(html, 2));
    }

    @Test
    public void testRemoveUnSafeTag() {
        String html = "<a onclick='alert(1)' onBlur='alert(1)'>你好</a><script>alert(1)</script><Script>alert(1)</SCRIPT>";
        Assert.assertEquals("<a>你好</a>", HtmlUtils.removeUnSafeTag(html));
    }

    @Test
    public void testRemoveTag() {
        String html = "<a onclick='alert(1)' onBlur='alert(1)'>你好</a><A>1</a>";
        Assert.assertEquals("", HtmlUtils.removeTag(html, "a"));
    }

}
