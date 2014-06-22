package com.realaicy.pg.test.selenium;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * 在出错时截屏的规则.
 * <p/>
 * 在出错时将屏幕保存为png格式的文件，默认路径为项目的target/screensnapshot/测试类名_测试方法名.png
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class SeleniumSnapshotRule extends TestWatcher {

    private final Selenium2 s;

    public SeleniumSnapshotRule(Selenium2 s) {
        this.s = s;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        String basePath = "target/screenshot/";
        String outputFileName = description.getClassName() + "_" + description.getMethodName() + ".png";
        s.snapshot(basePath, outputFileName);
    }
}
