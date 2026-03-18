package cn.tcmeta.logintest.Tools.GetInformation;

import cn.tcmeta.logintest.Tools.AutoScreenshot.WebdriverExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetRealWebDriver {
    private static final Logger log = LoggerFactory.getLogger(GetRealWebDriver.class);

    public static WebDriver getRealWebDriver(WebDriver webdriver) {
        // 解包代理对象，获取真实的 WebDriver
        WebDriver webdriverReal = WebdriverExtension.unwrap(webdriver);
        if (webdriverReal==null) {
            log.error("webdriverReal为空");
            throw new RuntimeException("webdriverReal为空");
        }
        return webdriverReal;
    }
}
