package cn.tcmeta.logintest.TestPages;


import cn.tcmeta.logintest.PageSetpMethods.LoginStepMethod;
import cn.tcmeta.logintest.Tools.*;
import cn.tcmeta.logintest.Tools.AboutElemnet.ElementAssert;
import cn.tcmeta.logintest.Tools.AutoScreenshot.FullPageScreenshot;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录页面对象
 * 只放置构造函数和实现登录逻辑的方法
 * 登录页面对象负责打开登录页面，并验证关键元素，处理登录逻辑
 * 其余基本方法(例如：验证登录页面标题loginPageTitleAssert())放置在LoginStepMethod类中
 */
public class LoginPage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    private WebDriver webdriver;
    private WindowHandle windowHandle;//窗口句柄
    public LoginStepMethod loginStepMethod;
    private ElementAssert elementAssert;

    public LoginPage(WebDriver webdriver){
        this.webdriver=webdriver;
        this.loginStepMethod=new LoginStepMethod(webdriver);
        this.windowHandle=new WindowHandle(webdriver);
        this.elementAssert=new ElementAssert(webdriver);
        //由于构造函数无法使用@Step注解，这里使用Allure.step()记录步骤
        log.info("打开登录页面，并验证关键元素");
        Allure.step("打开登录页面，并验证关键元素", ()-> {
                    //打开登录页面
                    loginStepMethod.openLoginPage();
                    //验证关键元素
                    loginStepMethod.loginPageTitleAssert();//验证登录页面标题
                    /*验证UsernameInput账号输入框的可见性、可用性*/
                    loginStepMethod.usernameInputIsDisplayedAssert();
                    loginStepMethod.usernameInputIsEnabledAssert();
                    /*验证PasswordInput密码输入框的可见性、可用性*/
                    loginStepMethod.passwordInputIsDisplayedAssert();
                    loginStepMethod.passwordInputIsEnabledAssert();
                    /*验证LoginButton登录按钮的可见性、可用性*/
                    loginStepMethod.loginButtonIsDisplayedAssert();
                    loginStepMethod.loginButtonIsEnabledAssert();
                });
        FullPageScreenshot.fullPageScreenshot(webdriver,"登录页面");
        windowHandle.saveWindowHandle();
    }
    @Step("进行登录操作并验证是否登录成功")
    public HomePage login(String username, String password){
        loginStepMethod.inputUsername( username);
        loginStepMethod.inputPassword(password);
        loginStepMethod.clickLoginButton();
        if(isLoginSuccess()){
            windowHandle.switchToNewWindHandle();
            Allure.step("验证登录成功页面标题", ()-> {
                elementAssert.assertPageTitle("小说精品屋_原创小说网站");
            });
            log.info("登录成功,页面跳转到首页，login方法返回(webdriver, true)HomePage值");
            FullPageScreenshot.fullPageScreenshot(webdriver,"登录成功");
            return new HomePage(webdriver, true);
        }
        //确保登录失败页面不会跳转
        loginStepMethod.loginPageTitleAssert();
        log.info("登录失败，页面不会跳转，login方法返回空值null");
        FullPageScreenshot.fullPageScreenshot(webdriver,"登录失败");
        return null;//返回不能是HomePage对象,因为HomePage构造函数带有首页打开的逻辑
    }
    @Step("判断登录是否成功")
    private boolean isLoginSuccess(){
        boolean isLoginSuccess;
        try{
            ExpectedCondition<Boolean> errorMsgShowCondition= webdriver-> {
                WebElement errorSpan = elementAssert.shortWait.until(
                        ExpectedConditions.presenceOfElementLocated(LoginStepMethod.errorSpanBy)
                );
                log.info("判断登录是否成功,返回错误提示是否不为空{}",!errorSpan.getText().isEmpty());
                return !errorSpan.getText().isEmpty();
            };
            elementAssert.shortWait.until(errorMsgShowCondition);
            FullPageScreenshot.fullPageScreenshot(webdriver,"判断登录是否成功,返回错误提示是否不为空");
            isLoginSuccess=false;
        } catch (TimeoutException | StaleElementReferenceException e){
            log.info("判断登录是否成功," +
                    "shortWait.until(errorMsgShowCondition)抛出超时异常TimeoutException" +
                    "或者元素过期异常StaleElementReferenceException，证明登录成功了");
            isLoginSuccess=true;
        }
        return isLoginSuccess;
    }
}
