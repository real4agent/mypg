package com.realaicy.pg.core.utils.forbidden;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class ForbiddenWordUtilsNGTest {

    @Test
    public void testReplaceWithDefaultMask() {

        String input = "12职称英语买答案32";
        String expected = "12***32";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testReplaceWithDefaultMask2() {

        String input = "1264.*学生运动123";
        String expected = "12***123";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testReplaceWithDefaultMask3() {

        String input = "freenet123";
        String expected = "***123";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testReplaceWithDefaultMask4() {

        String input = " 海峰 ";
        String expected = "***";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testReplaceWithCustomMask() {
        String input = "12职称英语买答案32";
        String expected = "12###32";
        String actual = ForbiddenWordUtils.replace(input, "###");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsForbiddenWord() {
        String input = "12职称英语买答案32";
        Assert.assertTrue(ForbiddenWordUtils.containsForbiddenWord(input));
    }

    @Test(enabled = false)
    public void testFetch() throws Exception {
        String input = "12test32";
        Assert.assertFalse(ForbiddenWordUtils.containsForbiddenWord(input));

        ForbiddenWordUtils.setForbiddenWordFetchURL("http://localhost:10090/forbidden-test.txt");
        ForbiddenWordUtils.setReloadInterval(500);
        ForbiddenWordUtils.initRemoteFetch();

        Server server = new Server(10090);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setBaseResource(Resource.newClassPathResource("."));
        server.setHandler(resourceHandler);
        server.start();

        Thread.sleep(1500);

        Assert.assertTrue(ForbiddenWordUtils.containsForbiddenWord(input));

        server.stop();
    }

}
