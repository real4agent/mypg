package com.realaicy.pg.core.test.selenium;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.AssertJUnit.assertTrue;

@PrepareForTest({WebDriverFactory.class, FirefoxDriver.class, InternetExplorerDriver.class, ChromeDriver.class,
        RemoteWebDriver.class})
public class WebDriverFactoryNGTest extends PowerMockTestCase {
    @Mock
    FirefoxDriver firefoxDriver;
    @Mock
    InternetExplorerDriver internetExplorerDriver;
    @Mock
    ChromeDriver chromerDriver;
    @Mock
    RemoteWebDriver remoteWebDriver;

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void buildWebDriver() throws Exception {
        MockitoAnnotations.initMocks(this);

        PowerMockito.whenNew(FirefoxDriver.class).withNoArguments().thenReturn(firefoxDriver);
        WebDriver driver = WebDriverFactory.createDriver("firefox");
        assertTrue(driver instanceof FirefoxDriver);

        PowerMockito.whenNew(InternetExplorerDriver.class).withNoArguments().thenReturn(internetExplorerDriver);
        driver = WebDriverFactory.createDriver("ie");
        assertTrue(driver instanceof InternetExplorerDriver);

        PowerMockito.whenNew(ChromeDriver.class).withNoArguments().thenReturn(chromerDriver);
        driver = WebDriverFactory.createDriver("chrome");
        assertTrue(driver instanceof ChromeDriver);

        PowerMockito.whenNew(RemoteWebDriver.class)
                .withArguments(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox())
                .thenReturn(remoteWebDriver);
        driver = WebDriverFactory.createDriver("remote:localhost:4444:firefox");
        assertTrue(driver instanceof RemoteWebDriver);
    }
}
