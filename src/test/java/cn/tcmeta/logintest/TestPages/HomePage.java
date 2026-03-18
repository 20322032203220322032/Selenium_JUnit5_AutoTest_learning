package cn.tcmeta.logintest.TestPages;

import cn.tcmeta.logintest.PageSetpMethods.HomeStepMethod;
import cn.tcmeta.logintest.Tools.*;
import cn.tcmeta.logintest.Tools.AutoScreenshot.FullPageScreenshot;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
/**
 * 采用首页辅助登录功能模块测试
 */
/**其实个人中心按钮a_userfo、退出按钮a_logout及登录按钮a_login都和在哪个界面无关，
它们是处于导航栏的，不会随着页面跳转而改变，但是登录成功后跳转的页面是首页，所以为了贴合真实场景采用首页了**/
public class HomePage {
    private WebDriver webdriver;
    private WindowHandle windowHandle;
    private HomeStepMethod homeStepMethod;
    private boolean loginStatus;

    public HomePage(WebDriver webdriver,Boolean loginStatus){
        this.webdriver=webdriver;
        this.windowHandle=new WindowHandle(webdriver);
        this.homeStepMethod=new HomeStepMethod(webdriver);
        this.loginStatus=loginStatus;
        windowHandle.saveWindowHandle();
        Allure.step("进入首页并验证是否处于登录状态",()-> {
            homeStepMethod.openHomePage();
                    if (loginStatus) {
                        homeStepMethod.homePageTitleAssert();
                        homeStepMethod.userInfoButtonIsDisplayedhomeAssert();
                        homeStepMethod.logoutButtonIsDisplayedAssert();
                        homeStepMethod.logoutButtonIsEnabledAssert();
                    } else {
                        homeStepMethod.loginButtonIsDisplayedAssert();
                    }
        });
        FullPageScreenshot.fullPageScreenshot(webdriver,"首页");
        windowHandle.saveWindowHandle();;
    }

    //点击LogoutButton退出登录按钮
    @Step("点击LogoutButton退出按钮并验证是否退出成功")
    public HomePage clickLogoutButton(){
        homeStepMethod.clickLogoutButton();
        windowHandle.switchToNewWindHandle();
        loginStatus=false;
        homeStepMethod.loginButtonIsDisplayedAssert();
        FullPageScreenshot.fullPageScreenshot(webdriver,"退出成功");
        return new HomePage(webdriver,loginStatus);
    }
}
