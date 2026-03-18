package cn.tcmeta.logintest.Tools;

import cn.tcmeta.logintest.Tools.AutoScreenshot.AutoScreenshotExtension;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {
    //构造函数
    public static WebDriver setBrowser(String browserType){
        WebDriver webdriver;
//        switch(browserType){
//            case"chrome":
//                webdriver=createChromeDriver();
//                return webdriver;
//            case"firefox":
//                webdriver=createFirefoxDriver();
//                return webdriver;
//            case"edge":
//                webdriver=createEdgeDriver();
//                return webdriver;
//            default:
//                throw new IllegalArgumentException("不支持的浏览器类型"+browserType);
//        }这是我原先的写法，下面是IDEA的推荐写法
        return switch (browserType) {
            case "chrome" -> {
                webdriver = createChromeDriver();
                yield webdriver;
            }
            case "firefox" -> {
                webdriver = createFirefoxDriver();
                yield webdriver;
            }
            case "edge" -> {
                webdriver = createEdgeDriver();
                yield webdriver;
            }
            default -> throw new IllegalArgumentException("不支持的浏览器类型" + browserType);
        };
//        return webdriver;原先是单线程的写法，判断完后返回webdriver，在多线程中，
//        多个测试用例同时使用一个webdriver,会报并发错误，所以改成多线程写法
    }
    private static WebDriver createChromeDriver(){
        WebDriverManager.chromedriver().cachePath("D:/selenium-driver").setup();
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return new ChromeDriver(options);
    }
    private static WebDriver createFirefoxDriver(){
        System.setProperty("webdriver.gecko.driver","D:\\selenium_driver\\geckodriver.exe");
        FirefoxOptions options=new FirefoxOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return new FirefoxDriver(options);
    }
    private static WebDriver createEdgeDriver(){
        WebDriverManager.edgedriver().cachePath("D:/selenium-driver").setup();
        EdgeOptions options=new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return new EdgeDriver(options);
    }
}
