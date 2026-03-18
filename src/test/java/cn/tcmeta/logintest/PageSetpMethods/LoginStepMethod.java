package cn.tcmeta.logintest.PageSetpMethods;

import cn.tcmeta.logintest.Tools.AboutElemnet.ElementAssert;
import cn.tcmeta.logintest.Tools.AboutElemnet.ElementInteraction;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class LoginStepMethod {
    private final WebDriver webdriver;
    private final ElementAssert elementAssert;
    private final ElementInteraction elementInteraction;
    //关键元素定位
    public static final By usernameInputBy=By.id("txtUName");//账号输入框
    public static final By passwordInputBy=By.id("txtPassword");//密码输入框
    public static final By loginButtonBy=By.id("btnLogin");//登录按钮
    public static final By registerButtonBy=By.cssSelector("a[href*='register'][class='btn_ora_white']");//注册按钮
    public static final By loginH3By=By.cssSelector("h3");//登录标题
    //非关键元素定位
    public static final By noRegisterPBy=By.cssSelector("p.tit");//没有账号提示
    public static final By autoLoginLabelBy=By.cssSelector("label.fl");//自动登录复选框，即input元素+em元素
    public static final By autoLoginInputBy=By.id("autoLogin");//自动登录复选框中的input
    public static final By errorSpanBy=By.cssSelector("span[id='LabErr']");//错误提示
    //创建元素定位-名称字典，ImmutableMap的put()为每个元素定位匹配对应的元素名称
    public static final Map<By, String> elementNameMAP = ImmutableMap.<By, String>builder()
            .put(usernameInputBy, "账号输入框")
            .put(passwordInputBy, "密码输入框")
            .put(loginButtonBy, "登录按钮")
            .put(registerButtonBy, "注册按钮")
            .put(loginH3By, "登录标题")
            .put(noRegisterPBy, "没有账号提示")
            .put(autoLoginLabelBy, "自动登录复选框")
            .put(autoLoginInputBy, "自动登录复选框")
            .put(errorSpanBy, "错误提示")
            .build();
    //创建元素定位-预期内容字典，ImmutableMap的put()为每个元素定位匹配对应的预期内容
    public static final Map<By, String> elementContentMAP = ImmutableMap.<By, String>builder()
            .put(usernameInputBy, "手机号码")
            .put(passwordInputBy, "密码")
            .put(loginButtonBy, "登录")
            .put(registerButtonBy, "立即注册")
            .put(loginH3By, "登录小说精品屋")
            .put(noRegisterPBy, "还没有注册账号？")
            .put(autoLoginLabelBy, "下次自动登录")
            .build();

    public LoginStepMethod(WebDriver webdriver){
        this.webdriver=webdriver;
        this.elementAssert=new ElementAssert(webdriver);
        this.elementInteraction=new ElementInteraction(webdriver);
    }
    @Step("打开登录界面")
    public void openLoginPage(){
        webdriver.get("http://117.72.165.13:8888/user/login.html");
    }
    //验证登录页面的页面标题
    @Step("验证登录页面的页面标题")
    public void loginPageTitleAssert(){
        elementAssert.assertPageTitle("会员登录_小说精品屋");
    }

    //验证usernameInput
    @Step("验证usernameInput可见性")
    public void usernameInputIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(usernameInputBy,elementNameMAP.get(usernameInputBy));
    }
    @Step("验证usernameInput可用性")
    public void usernameInputIsEnabledAssert(){
        elementAssert.assertElementIsEnabled(usernameInputBy,elementNameMAP.get(usernameInputBy));
    }
    @Step("验证usernameInput的placeholder属性")
    public void usernameInputPlaceholderAssert(){
        elementAssert.assertPlaceholder(usernameInputBy,elementContentMAP.get(usernameInputBy));
    }
    //usernameInput输入
    @Step("输入账号")
    public void inputUsername(String username){
        elementInteraction.SendKeys(usernameInputBy,username,elementNameMAP.get(usernameInputBy));
    }
    
    //验证passwordInput
    @Step("验证passwordInput可见性")
    public void passwordInputIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(passwordInputBy,elementNameMAP.get(passwordInputBy));
    }
    @Step("验证passwordInput可用性")
    public void passwordInputIsEnabledAssert(){
        elementAssert.assertElementIsEnabled(passwordInputBy,elementNameMAP.get(passwordInputBy));
    }
    @Step("验证passwordInput的placeholder属性")
    public void passwordInputPlaceholderAssert(){
        elementAssert.assertPlaceholder(passwordInputBy,elementContentMAP.get(passwordInputBy));
    }
    //passwordInput输入
    @Step("输入密码")
    public void inputPassword(String password){
        elementInteraction.SendKeys(passwordInputBy,password,elementNameMAP.get(passwordInputBy));
    }
    
    //验证loginButton
    @Step("验证loginButton可见性")
    public void loginButtonIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(loginButtonBy,elementNameMAP.get(loginButtonBy));
    }
    @Step("验证loginButton可用性")
    public void loginButtonIsEnabledAssert(){
        elementAssert.assertElementIsEnabled(loginButtonBy,elementNameMAP.get(loginButtonBy));
    }
    @Step("验证loginButton的value属性")
    public void loginButtonValueAssert(){
        elementAssert.assertValue(loginButtonBy,elementContentMAP.get(loginButtonBy));
    }
    //loginButton点击
    @Step("点击登录按钮")
    public void clickLoginButton(){
        elementInteraction.Click(loginButtonBy,elementNameMAP.get(loginButtonBy));
    }
    
    //验证registerButton
    @Step("验证registerButton可见性")
    public void registerButtonIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(registerButtonBy,elementNameMAP.get(registerButtonBy));
    }
    @Step("验证registerButton可用性")
    public void registerButtonIsEnabledAssert(){
        elementAssert.assertElementIsEnabled(registerButtonBy,elementNameMAP.get(registerButtonBy));
    }
    @Step("验证registerButton的text属性")
    public void registerButtonTextAssert(){
        elementAssert.assertText(registerButtonBy,elementContentMAP.get(registerButtonBy));
    }
    //点击立即注册按钮
    @Step("点击立即注册按钮")
    public void clickRegisterButton(){
        elementInteraction.Click(registerButtonBy,elementNameMAP.get(registerButtonBy));
        Allure.step("验证是否已经进入注册界面",()-> elementAssert.assertPageTitle("会员注册_小说精品屋"));
    }
    
    //验证loginH3
    @Step("验证loginH3可见性")
    public void loginH3IsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(loginH3By,elementNameMAP.get(loginH3By));
    }
    @Step("验证loginH3的text属性")
    public void loginH3TextAssert(){
        elementAssert.assertText(loginH3By,elementContentMAP.get(loginH3By));
    }
    
    //验证noRegisterP
    @Step("验证noRegisterP可见性")
    public void noRegisterPIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(noRegisterPBy,elementNameMAP.get(noRegisterPBy));
    }
    @Step("验证noRegisterP的text属性")
    public void noRegisterPTextAssert(){
        elementAssert.assertText(noRegisterPBy,elementContentMAP.get(noRegisterPBy));
    }

    //验证autoLoginLabel
    @Step("验证autoLoginLabel可见性")
    public void autoLoginLabelIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(autoLoginLabelBy,elementNameMAP.get(autoLoginLabelBy));
    }
    @Step("验证autoLoginLabel可用性")
    public void autoLoginLabelIsEnabledAssert(){
        elementAssert.assertElementIsEnabled(autoLoginInputBy,elementNameMAP.get(autoLoginInputBy));
    }//由于autoLoginLabel是由input和<em></em>组成的，所以无法使用autoLoginLabelBy进行可用性验证要用input(By.id("autoLogin")进行
    @Step("验证autoLoginLabel的text属性")
    public void autoLoginLabelTextAssert(){
        elementAssert.assertText(autoLoginLabelBy,elementContentMAP.get(autoLoginLabelBy));
    }
    //勾选autoLoginLabel自动登录复选框
    @Step("勾选autoLoginLabel自动登录复选框")
    public void clickAutoLoginLabel(){
        elementInteraction.Click(autoLoginLabelBy,elementNameMAP.get(autoLoginLabelBy));
    }

    //验证errorSpan
    @Step("验证errorSpan可见性")
    public void errorSpanIsDisplayedAssert(){
        elementAssert.assertElementIsDisplayed(errorSpanBy,elementNameMAP.get(errorSpanBy));
    }
    @Step("验证errorSpan的text属性")
    public void errorSpanTextAssert(String correctText){
        elementAssert.assertText(errorSpanBy,correctText);
    }


}
