package cn.tcmeta.logintest.Tools.AboutElemnet;

import cn.tcmeta.logintest.Tools.AutoScreenshot.FullPageScreenshot;
import cn.tcmeta.logintest.Tools.GetInformation.GetRealWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
/**
 * 验证方法同一放置
 */
public class ElementAssert {
    private static final Logger log = LoggerFactory.getLogger(ElementAssert.class);
    private Wait<WebDriver> wait;//正常的验证等待，时间一般为10秒，用于初加载页面的验证
    public Wait<WebDriver> shortWait;//短等待，时间一般为3秒，用于已加载页面的验证，常用在页面逻辑判断中
    private WebDriver webdriver;
    public ElementAssert(WebDriver webdriver) {
        this.webdriver = webdriver;
        WebDriver webdriverReal= GetRealWebDriver.getRealWebDriver(webdriver);
        wait = new FluentWait<WebDriver>(webdriverReal)
                .withTimeout(java.time.Duration.ofSeconds(10))
                .pollingEvery(java.time.Duration.ofMillis(500))
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
        shortWait = new FluentWait<WebDriver>(webdriverReal)
                .withTimeout(java.time.Duration.ofSeconds(3))
                .pollingEvery(java.time.Duration.ofMillis(500))
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
    }
    //验证页面标题
    public void assertPageTitle(String title){
        wait.until(
                ExpectedConditions.titleIs(title)
        );
        if(!webdriver.getTitle().equals(title)){
            log.error("当前页面不是登录页面！当前界面是{}|{}", webdriver.getTitle(), webdriver.getCurrentUrl());
            FullPageScreenshot.fullPageScreenshot(webdriver,"页面标题错误");
            throw new IllegalStateException("当前页面不是登录页面！当前界面是"+webdriver.getTitle()+"|"+webdriver.getCurrentUrl());
        }
    }

    //验证元素是否可见
    public void assertElementIsDisplayed(By by, String elementName){
        WebElement element=wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
        if(!element.isDisplayed()){
            log.error("{}元素不可见！", elementName);
            FullPageScreenshot.fullPageScreenshot(webdriver,elementName+"不可见");
            throw new org.openqa.selenium.NoSuchElementException(elementName + "不可见");
        }
    }

    //验证元素是否可用
    public void assertElementIsEnabled(By by,String elementName){
        WebElement element=wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
        if(!element.isEnabled()){
            log.error("{}元素不可用！", elementName);
            FullPageScreenshot.fullPageScreenshot(webdriver,elementName+"不可用");
            throw new ElementNotInteractableException(elementName + "不可用");
        }
    }

    //验证placeholder
    public void assertPlaceholder(By by,String correctText){
        WebElement element=wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
        String placeholderThis=element.getAttribute("placeholder");
        if(!placeholderThis.equals(correctText)){
            log.error("{}元素placeholder属性错误！", element);
            FullPageScreenshot.fullPageScreenshot(webdriver,correctText+"placeholder属性错误");
            throw new IllegalStateException(element+"placeholder属性错误！当前："+placeholderThis+"正确的placeholder是"+correctText);
        }
    }

    //验证value
    public void assertValue(By by,String correctText){
        WebElement element=wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
        String valueThis=element.getAttribute("value");
        if(!valueThis.equals(correctText)){
            log.error("{}元素value属性错误！", element);
            FullPageScreenshot.fullPageScreenshot(webdriver,correctText+"value属性错误");
            throw new IllegalStateException(element+"value属性错误！当前："+valueThis+"正确的value是"+correctText);
        }
    }

    //验证text
    public void assertText(By by,String correctText){
        WebElement element=wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
        String textThis=element.getText();
        if(!textThis.equals(correctText)){
            log.error("{}元素text属性错误！", element);
            FullPageScreenshot.fullPageScreenshot(webdriver,correctText+"text属性错误");
            throw new IllegalStateException(element+"text属性"+element.getText()+"错误！当前："+textThis+"正确的text是"+correctText);
        }
    }

}
