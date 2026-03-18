package cn.tcmeta.logintest.Tools.AboutElemnet;

import cn.tcmeta.logintest.Tools.AutoScreenshot.FullPageScreenshot;
import cn.tcmeta.logintest.Tools.GetInformation.GetRealWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
/**
 * 元素交互同一放置
 */
public class ElementInteraction {
    private static final Logger log = LoggerFactory.getLogger(ElementInteraction.class);
    private Wait<WebDriver> wait;
    private WebDriver webdriver;
    public ElementInteraction(WebDriver webdriver) {
        this.webdriver = webdriver;
        WebDriver webdriverReal= GetRealWebDriver.getRealWebDriver(webdriver);
        wait = new FluentWait<WebDriver>(webdriverReal)
                .withTimeout(java.time.Duration.ofSeconds(15))
                .pollingEvery(java.time.Duration.ofMillis(500))
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
    }
    //click点击事件
    public void Click(By by,String elementName){
        try {
            wait.until(
                    ExpectedConditions.elementToBeClickable(by)
            ).click();
        } catch (ElementNotInteractableException e) {
            log.error("按钮{}元素不点击", elementName);
            FullPageScreenshot.fullPageScreenshot(webdriver,elementName+"元素不可用");
            throw new ElementNotInteractableException(elementName+"元素不可用");
        }
    }
    //SendKeys输入事件
    public void SendKeys(By by, String value, String elementName){
        try {
            wait.until(
                    ExpectedConditions.elementToBeClickable(by)
            ).sendKeys(value);
        } catch (ElementNotInteractableException e) {
            log.error("输入框{}元素不输入", elementName);
            FullPageScreenshot.fullPageScreenshot(webdriver,elementName+"元素不可用");
            throw new ElementNotInteractableException(elementName+"元素不可用");
        }
    }
}
