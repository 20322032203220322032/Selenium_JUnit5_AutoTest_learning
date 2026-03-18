package cn.tcmeta.logintest.Tools.GetInformation;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 获取浏览器名+版本
 * 兼容性：Selenium 4.x+
 * 性能：单次调用0.05-0.1ms
 */
public class GetBrowserInfo {
    //这里导入的webdriver不是真实的WebDriver，而是代理对象，需要解包
    public static String[] getBrowserInfo(WebDriver webdriver) {
        WebDriver webdriverReal=GetRealWebDriver.getRealWebDriver(webdriver);
        // 1. 获取基础Capabilities（所有浏览器通用）
        //RemoteWebDriver要求webdriver是真实的WebDriver，所以使用realDriver
        Capabilities capabilities = ((RemoteWebDriver) webdriverReal).getCapabilities();
        // 2. 获取浏览器名（标准化，避免返回值不一致），由toLowerCase()同一转化为小写
        String browserName = capabilities.getBrowserName().toLowerCase();
        // 3. 获取浏览器版本（分浏览器适配）
        String browserVersion = "Unknown";
        switch (browserName) {
            case "chrome":
                browserVersion = capabilities.getBrowserVersion().split("\\.")[0];
            case "microsoftedge":
                // Chrome/Edge：Capabilities直接获取版本（4.x+原生支持）
                browserVersion = capabilities.getBrowserVersion().split("\\.")[0]; //plit("\\.")按.分割字符，只保留主版本（如120）
                break;
            case "firefox":
                // Firefox：Capabilities直接获取版本，格式更纯净
                browserVersion = capabilities.getBrowserVersion().split("\\.")[0];
                break;
        }
        return new String[]{browserName, browserVersion};
    }
}
