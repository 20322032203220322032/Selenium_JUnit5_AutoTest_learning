package cn.tcmeta.logintest;

import cn.tcmeta.logintest.TestPages.HomePage;
import cn.tcmeta.logintest.TestPages.LoginPage;

import cn.tcmeta.logintest.Tools.AutoScreenshot.AutoScreenshotExtension;
import cn.tcmeta.logintest.Tools.AutoScreenshot.WebdriverExtension;
import cn.tcmeta.logintest.Tools.BrowserFactory;
import cn.tcmeta.logintest.Tools.CookieUtils;
import cn.tcmeta.logintest.Tools.AutoScreenshot.FullPageScreenshot;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * 登录功能测试类
 * 统一层级：
 * Epic：精品小说网站UI功能测试
 * Feature：按测试类型拆分（登录UI/登录功能/兼容性测试）
 * Story：具体业务场景
 */
@Epic("精品小说网站UI功能测试")//应该放在测试类上，否则非类定义前，注解不生效
class LoginTestApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(LoginTestApplicationTests.class);
    //@RegisterExtension
    @RegisterExtension
    WebdriverExtension webDriverManager = new WebdriverExtension();
    //Lambda 表达式 c -> this.webDriverManager.getWebDriver(c)：输入参数：ExtensionContext c  返回值：WebDriver
    private final Function<ExtensionContext, WebDriver> driverSupplier=c -> this.webDriverManager.getWebDriver(c);
    @RegisterExtension
    AutoScreenshotExtension screenshotManager = new AutoScreenshotExtension(driverSupplier);

    //「大业务域 Epic→ 核心功能模块 Feature→ 具体业务场景Story」
//    @Epic("精品小说网站UI功能测试")
    @Feature("登录功能模块UI测试")
    @Story("登录页面元素显示验证")
    // 测试用例在报告中显示的友好名称（替代默认的方法名）
    @DisplayName("界面UI_登录页面元素显示[TC_LOGIN_001T]")
    // 测试用例的详细说明（描述测试目的、范围、预期结果）
    @Description("验证登录页面所有核心元素（账号/密码输入框、登录按钮、注册链接等）的可见性、可用性及文本内容是否符合预期")
    // 自定义标签（用于报告中筛选用例，比如按模块/类型）
    @Tag("登录界面UI")
    // 严重级别（Allure官方定义5级，优先级从低到高）：
    // TRIVIAL（微不足道的）、MINOR（次要的）、NORMAL（正常的）、CRITICAL（严重的）、BLOCKER（阻碍性的）
    // 枚举值为大写，字符串匹配时不区分大小写（如"critical"也可识别）
    @Severity(SeverityLevel.CRITICAL)
    // 测试用例负责人（报告中可按所有者筛选）
    @Owner("汪俊杰") // 实际使用时替换为真实负责人姓名
    // 关联外部链接（如需求文档、设计稿、接口文档等）
//    @Link(name = "Website", url = "https://dev.example.com/")
    // 关联缺陷管理系统的缺陷ID（如Jira的BUG编号）
    @Issue("UI-001") // 实际使用时替换为真实缺陷ID
    // 关联测试用例管理系统的用例ID（如TestLink、Zephyr的用例编号）
    @TmsLink("TC_LOGIN_001T") // 实际使用时替换为真实测试用例ID
    // 执行模式：CONCURRENT（允许并发执行）、SAME_THREAD（仅同一线程执行）
    
    @Test
    void TC_LOGIN_001T(WebDriver webdriver){
        //LoginPage构造函数自带了usernameInput、passwordInput、loginButton、registerButton的可见及可用验证，还有loginTitle的可见验证
        LoginPage loginPage=new LoginPage(webdriver);
        //断言非关键元素的显示
        loginPage.loginStepMethod.loginH3IsDisplayedAssert();
        loginPage.loginStepMethod.noRegisterPIsDisplayedAssert();
        loginPage.loginStepMethod.autoLoginLabelIsDisplayedAssert();
        loginPage.loginStepMethod.errorSpanIsDisplayedAssert();
        loginPage.loginStepMethod.registerButtonIsDisplayedAssert();
        //断言非关键元素可用性
        loginPage.loginStepMethod.autoLoginLabelIsEnabledAssert();
        loginPage.loginStepMethod.registerButtonIsEnabledAssert();
        //断言元素文本内容
        loginPage.loginStepMethod.loginH3TextAssert();
        loginPage.loginStepMethod.usernameInputPlaceholderAssert();
        loginPage.loginStepMethod.passwordInputPlaceholderAssert();
        loginPage.loginStepMethod.loginButtonValueAssert();
        loginPage.loginStepMethod.loginPageTitleAssert();
        loginPage.loginStepMethod.noRegisterPTextAssert();
        loginPage.loginStepMethod.autoLoginLabelTextAssert();
        loginPage.loginStepMethod.registerButtonTextAssert();
        
    }
    /**
     * 正向流程_正常登录
     * 账号：13512345678
     * 密码:12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("正常登录流程验证")
    @DisplayName("正向流程_正常登录[TC_LOGIN_002T]")
    @Description("验证注册账号(账号：13512345678,密码:12345678901)能否正常登录，并成功跳转带有个人中心按钮和退出按钮的首页")
    @Tag("登录功能")
    @Tag("正向用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-001")//Function（功能）
    @TmsLink("TC_LOGIN_002T")
    
    @Test
    void TC_LOGIN_002T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        //HomePage构造函数自带了个人中心按钮和退出按钮的验证方法，只有登录成功后首页才会有个人中心按钮和退出按钮
        loginPage.login("13512345678","12345678901");
    }
    /**
     * 手机号校验_手机号为空
     * 账号为空
     * 密码12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("手机号格式验证")
    @DisplayName("手机号校验_手机号为空[TC_LOGIN_003T]")
    @Description("验证手机号为空(手机号为空，密码：12345678901)能否停留在页面不动并返回正确的错误提示'手机号不能为空！'")
    @Tag("登录功能")
    @Tag("手机号校验")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-002")
    @TmsLink("TC_LOGIN_003T")
    
    @Test
    void TC_LOGIN_003T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("","12345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号不能为空！");

    }
    /**
     * 手机号校验_手机号不足11位
     * 账号：1351234567
     * 密码12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("手机号格式验证")
    @DisplayName("手机号校验_手机号不足11位[TC_LOGIN_004T]")
    @Description("验证手机号不足11位(手机号：1351234567，密码：12345678901)能否停留在页面不动并返回正确的错误提示'手机号格式不正确！'")
    @Tag("登录功能")
    @Tag("手机号校验")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-003")
    @TmsLink("TC_LOGIN_004T")
    
    @Test
    void TC_LOGIN_004T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("1351234567","12345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号格式不正确！");

    }
    /**
     * 手机号校验_手机号超过11位
     * 账号：135123456789
     * 密码12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("手机号格式验证")
    @DisplayName("手机号校验_手机号超过11位[TC_LOGIN_005T]")
    @Description("验证手机号超过11位(手机号：135123456789，密码：12345678901)能否停留在页面不动并返回正确的错误提示'手机号格式不正确！'")
    @Tag("登录功能")
    @Tag("手机号校验")
    @Tag("异常用例")
    @Issue("FUNC-004")
    @TmsLink("TC_LOGIN_005T")
    
    @Test
    void TC_LOGIN_005T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("135123456789","12345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号格式不正确！");

    }
    /**
     * 手机号校验_手机号包含非数字字符
     * 账号：1351234abcd
     * 密码12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("手机号格式验证")
    @DisplayName("手机号校验_手机号包含非数字字符[TC_LOGIN_006T]")
    @Description("验证手机号包含非数字字符(手机号：1351234abc，密码：12345678901)" +
            "能否停留在页面不动并返回正确的错误提示'手机号格式不正确！'")
    @Tag("登录功能")
    @Tag("手机号校验")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-005")
    @TmsLink("TC_LOGIN_006T")
    
    @Test
    void TC_LOGIN_006T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("1351234abcd","12345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号格式不正确！");

    }
    /**
     * 手机号校验_手机号格式非法（非标准号段）
     * 账号：12345678901
     * 密码12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("手机号格式验证")
    @DisplayName("手机号校验_手机号格式非法（非标准号段）[TC_LOGIN_007T]")
    @Description("验证手机号格式非法（非标准号段）(手机号：12345678901，密码：12345678901)" +
            "能否停留在页面不动并返回正确的错误提示'手机号格式不正确！'")
    @Tag("登录功能")
    @Tag("手机号校验")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-006")
    @TmsLink("TC_LOGIN_007T")
    
    @Test
    void TC_LOGIN_007T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("12345678901","12345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号格式不正确！");

    }

    /**
     * 手机号校验_手机号未注册
     * 账号：13800001111
     * 密码12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("手机号有效性验证")
    @DisplayName("手机号校验_手机号未注册[TC_LOGIN_008T]")
    //多数系统为了安全，会统一提示"手机号或密码错误！"，一般不会提示手机号未注册
    @Description("验证手机号未注册(手机号：13800001111，密码：12345678901)能否停留在页面不动并返回正确的错误提示'手机号或密码错误！'")
    @Tag("登录功能")
    @Tag("手机号校验")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-007")
    @TmsLink("TC_LOGIN_008T")
    
    @Test
    void TC_LOGIN_008T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("13800001111","12345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号或密码错误！");

    }
    /**
     * 密码校验_密码为空
     * 账号：13512345678
     * 密码为空
     */
    @Feature("登录功能模块功能测试")
    @Story("密码格式验证")
    @DisplayName("密码校验_密码为空[TC_LOGIN_009T]")
    @Description("验证密码为空(手机号：13512345678，密码：12345678901)能否停留在页面不动并返回正确的错误提示'密码不能为空！'")
    @Tag("登录功能")
    @Tag("密码验证")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-008")
    @TmsLink("TC_LOGIN_009T")
    
    @Test
    void TC_LOGIN_009T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("13512345678","");
        loginPage.loginStepMethod.errorSpanTextAssert("密码不能为空！");

    }
    /**
     * 密码校验_密码长度小于6位
     * 账号：13512345678
     * 密码12345
     */
    @Feature("登录功能模块功能测试")
    @Story("密码格式验证")
    @DisplayName("密码校验_密码长度小于6位[TC_LOGIN_010T]")
    @Description("验证密码长度小于6位(手机号：13512345678，密码：12345)能否停留在页面不动并返回正确的错误提示'手机号或密码错误！'")
    @Tag("登录功能")
    @Tag("密码验证")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-009")
    @TmsLink("TC_LOGIN_010T")
    
    @Test
    void TC_LOGIN_010T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("13512345678","12345");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号或密码错误！");

    }
    /**
     * 密码校验_密码长度大于20位
     * 账号：13512345678
     * 密码123456789012345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("密码格式验证")
    @DisplayName("密码校验_密码长度大于20位[TC_LOGIN_011T]")
    @Description("验证密码长度大于20位(手机号：13512345678，密码：123456789012345678901)" +
            "能否停留在页面不动并返回正确的错误提示'手机号或密码错误！'")
    @Tag("登录功能")
    @Tag("密码验证")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-010")
    @TmsLink("TC_LOGIN_011T")
    
    @Test
    void TC_LOGIN_011T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("13512345678","123456789012345678901");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号或密码错误！");
    }
    /**
     * 密码校验_密码包含非字母数字字符
     * 账号：13512345678
     * 密码abc@123
     */
    @Feature("登录功能模块功能测试")
    @Story("密码格式验证")
    @DisplayName("密码校验_密码包含非字母数字字符[TC_LOGIN_012T]")
    @Description("验证密码包含非字母数字字符(手机号：13512345678，密码：abc@123)，"+
            "能否停留在页面不动并返回正确的错误提示'手机号或密码错误！'")
    @Tag("登录功能")
    @Tag("密码验证")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-011")
    @TmsLink("TC_LOGIN_012T")
    
    @Test
    void TC_LOGIN_012T(WebDriver webdriver) {
        LoginPage loginPage=new   LoginPage(webdriver);
        loginPage.login("13512345678","abc@123");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号或密码错误！");
    }
    /**
     * 密码校验_密码错误
     * 账号：13512345678
     * 密码123456
     */
    @Feature("登录功能模块功能测试")
    @Story("密码有效性验证")
    @DisplayName("密码校验_密码错误[TC_LOGIN_013T]")
    @Description("验证密码错误(手机号：13512345678，密码：123456)" +
            "能否停留在页面不动并返回正确的错误提示'手机号或密码错误！'")
    @Tag("登录功能")
    @Tag("密码验证")
    @Tag("异常用例")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("汪俊杰")
    @Issue("FUNC-012")
    @TmsLink("TC_LOGIN_013T")
    
    @Test
    void TC_LOGIN_013T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.login("13512345678","123456");
        loginPage.loginStepMethod.errorSpanTextAssert("手机号或密码错误！");
    }
    /**
     * 自动登录_自动登录功能验证
     * 账号：13512345678
     * 密码：12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("自动登录功能验证")
    @DisplayName("自动登录_自动登录功能验证[TC_LOGIN_014T]")
    @Description("验证自动登录功能，先勾选自动登录，然后正常登录(手机号：13512345678，密码：12345678901)成功，" +
            "再关闭浏览器，之后再打开浏览器，查看首页是否有表示已登录的个人中心按钮和退出按钮，验证自动登录功能是否有效'")
    @Tag("登录功能")
    @Tag("自动登录")
    @Severity(SeverityLevel.NORMAL)//由于自动登录不影响核心功能，设置为NORMAL正常级
    @Owner("汪俊杰")
    @Issue("FUNC-013")
    @TmsLink("TC_LOGIN_014T")
    @Test
    void TC_LOGIN_014T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.loginStepMethod.clickAutoLoginLabel();
        FullPageScreenshot.fullPageScreenshot(webdriver,"自动登录功能验证");//截图
        loginPage.login("13512345678","12345678901");
        CookieUtils.saveLoginCookie(webdriver);
        webdriver.quit();
        WebDriver webdriverNew = BrowserFactory.setBrowser("chrome");
        new LoginPage(webdriverNew);
        //先加载Cookie，由于是在登录界面保存Cookie，所以需要先在登录界面加载Cookie
        //很无奈，这是selenium自带的限制，我想过修改写入的Cookie,但是已经写入的Cookie只能读不能改，至于重新写入，太麻烦了
        CookieUtils.loadLoginCookie(webdriverNew);
        new HomePage(webdriverNew,true);
        if(webdriverNew!=null){
            FullPageScreenshot.fullPageScreenshot(webdriverNew,"最终截图");
            webdriverNew.quit();
        }
        CookieUtils.deleteLoginCookie();
    }
    /**
     * 注册链接_点击“立即注册”跳转
     */
    @Feature("登录功能模块功能测试")
    @Story("点击“立即注册”跳转验证")
    @DisplayName("注册按钮_点击“立即注册”跳转验证[TC_LOGIN_015T]")
    @Description("验证“立即注册”按钮跳转的有效性，点击“立即注册”按钮，查看是否到达注册界面")
    @Tag("登录功能")
    @Tag("立即注册")
    //由于未登录时导航栏是始终有注册按钮的，登录界面这个"立即注册"按钮主要是防呆，所以为TRIVIAL微不足道级
    @Severity(SeverityLevel.TRIVIAL)
    @Owner("汪俊杰")
    @Issue("FUNC-014")
    @TmsLink("TC_LOGIN_015T")
    
    @Test
    void TC_LOGIN_015T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        loginPage.loginStepMethod.clickRegisterButton();
        FullPageScreenshot.fullPageScreenshot(webdriver,"注册按钮_点击“立即注册”跳转验证");
    }
    /**
     * 退出功能_退出登录
     * 账号：13512345678
     * 密码：12345678901
     */
    @Feature("登录功能模块功能测试")
    @Story("退出登录功能验证")
    @DisplayName("退出功能_退出登录验证[TC_LOGIN_016T]")
    @Description("验证退出登录功能，先正常登录(手机号：13512345678，密码：12345678901)成功，" +
            "然后点击退出按钮退出登录，查看首页是否有表示未登录的登录按钮，验证退出登录功能是否有效'")
    @Tag("登录功能")
    @Tag("退出登录")
    //由于在勾选自动登录复选框时，关闭当前网页就可以退出登录，而且在个人中心设置处还有一个退出登录，所以设置为NORMAL正常级
    @Severity(SeverityLevel.NORMAL)
    @Owner("汪俊杰")
    @Issue("FUNC-015")
    @TmsLink("TC_LOGIN_016T")
    
    @Test
    void TC_LOGIN_016T(WebDriver webdriver) {
        LoginPage loginPage=new LoginPage(webdriver);
        HomePage homePage=loginPage.login("13512345678","12345678901");
        homePage.clickLogoutButton();
    }
    /**
     * 多浏览器登录测试，WebDiver需要手动设置，不再需要使用WebDriverManager
     * 浏览器：chrome、firefox、edge
     * 账号：13512345678
     * 密码：12345678901
     */
    @Feature("登录功能模块兼容性测试")
    @Story("主流浏览器登录验证")
    @DisplayName("主流浏览器登录验证[TC_LOGIN_018T]")
    @Description("测试登录功能模块在各个浏览器环境下是否有效，" +
            "Chrome浏览器、Firefox浏览器及Edge浏览器依次进行正常登录(手机号：13512345678，密码：12345678901)，" +
            "测试能否登录成功，验证登录功能模块在各个浏览器环境下的有效性")
    @Tag("多浏览器")
    @Tag("兼容性")
    @Severity(SeverityLevel.NORMAL)//由于不影响核心功能，兼容性属于辅助验证，设置为NORMAL正常级
    @Owner("汪俊杰")
    @Issue("COMP-001")//Compatibility（兼容性）
    @TmsLink("TC_LOGIN_018T")
    @ParameterizedTest
    @ValueSource(strings={"chrome","firefox","edge"})
    void TC_LOGIN_018T(String browserType) {
        log.info("当前浏览器：{}", browserType);
        WebDriver webdriver = BrowserFactory.setBrowser(browserType);
        try {
            LoginPage loginPage=new LoginPage(webdriver);
            loginPage.login("13512345678","12345678901");
        } finally {
            if (webdriver != null) {
                FullPageScreenshot.fullPageScreenshot(webdriver,"最终截图");
                webdriver.quit();
            }
        }
    }

}

