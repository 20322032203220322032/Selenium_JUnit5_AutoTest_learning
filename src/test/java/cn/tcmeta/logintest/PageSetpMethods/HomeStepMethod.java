package cn.tcmeta.logintest.PageSetpMethods;

import cn.tcmeta.logintest.Tools.AboutElemnet.ElementAssert;
import cn.tcmeta.logintest.Tools.AboutElemnet.ElementInteraction;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class HomeStepMethod {
    private final WebDriver webdriver;
    private final ElementAssert elementAssert;
    private final ElementInteraction elementInteraction;

    public final static By a_userfoBy= By.cssSelector("a[href*='userinfo']");
    public final static By a_logoutBy= By.cssSelector("a[href*='logout']");
    public final static By a_loginBy= By.cssSelector("a[href*='login']");

    public final static Map<By, String> elementNameMAP = ImmutableMap.<By, String>builder()
            .put(a_userfoBy, "个人中心")
            .put(a_logoutBy, "退出")
            .put(a_loginBy, "登录")
            .build();
    public HomeStepMethod(WebDriver webdriver){
        this.webdriver=webdriver;
        this.elementAssert=new ElementAssert(webdriver);
        this.elementInteraction=new ElementInteraction(webdriver);
    }

    @Step("打开首页")
    public void openHomePage(){
        webdriver.get("http://117.72.165.13:8888/");
    }
    @Step("验证首页标题")
    public void homePageTitleAssert(){
        elementAssert.assertPageTitle("小说精品屋_原创小说网站");
    }
    @Step("验证个人中心按钮的可见性")
    public void userInfoButtonIsDisplayedhomeAssert(){
        elementAssert.assertElementIsDisplayed(a_userfoBy,elementNameMAP.get(a_userfoBy));
    }

    @Step("验证退出按钮的可见性")
    public void logoutButtonIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(a_logoutBy,elementNameMAP.get(a_logoutBy));
    }
    @Step("验证退出按钮的可用性")
    public void logoutButtonIsEnabledAssert(){
        elementAssert.assertElementIsEnabled(a_logoutBy,elementNameMAP.get(a_logoutBy));
    }
    @Step("点击退出按钮")
    public void clickLogoutButton(){
        elementInteraction.Click(a_logoutBy,elementNameMAP.get(a_logoutBy));
    }

    @Step("验证登录按钮的可见性")
    public void loginButtonIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(a_loginBy,elementNameMAP.get(a_loginBy));
    }

}
