Selenium_JUnit5_AutoTest_learning

项目基础

基于小说精品屋登录模块的 UI 自动化测试 demo，仅覆盖登录功能；

测试地址：http://117.72.165.13:8888/user/login.html


项目结构

核心目录：src/test/java/cn/tcmeta/logintest/test

PageSetpMethods

作用：集中管理页面对象的验证方法（如 usernameInputIsDisplayedAssert ()）、元素交互方法（如 clickRegisterButton ()）；

设计亮点：通过 ImmutableMap 绑定元素定位 By 与元素名称 / 文本属性，减少硬编码；解决页面对象类方法臃肿问题，提升可读性与可维护性。

TestPages

作用：存放页面对象类（如 LoginPage）；

PO 模型落地：参考 Selenium 官方文档实现（https://www.selenium.dev/zh-cn/documentation/test_practices/encouraged/page_object_models/）；

优化过程：

初始问题：测试用例硬编码多，LoginPage 整合验证 / 交互方法后代码臃肿（27 个方法）；

优化方案：拆分 ElementAssert/ElementInteraction 基础方法类→进一步拆分 LoginPageStepMethods 统一管理；最终 LoginPage 仅保留构造函数与核心业务逻辑方法 login ()；

待验证问题：官方建议页面对象不负责断言，但当前通过 HomePage 断言页面标题变更（"会员登录_小说精品屋"→"小说精品屋_原创小说网站"）作为登录成功的第二重验证，该写法减少硬编码但违背官方建议，需后续验证合理性；

待优化：未落地 Page Component Objects，计划在首页测试中应用。

Tools

AboutElement：ElementAssert（元素验证）、ElementInteraction（元素交互）；

AboutScreenshot：FullPageScreenshot（全屏截图）、AutoScreenshotExtension（异常时自动截图，基于 Allure 官方 demo 实现，重写 handleTestExecutionException ()，但对 ExtensionContext 参数及 WebdriverExtension 的并行资源管理逻辑尚未完全掌握）；

GetInformation：GetBrowserInfo（浏览器版本）、GetCurrentMethodName（测试用例编号）、GetRealWebDriver；

其他：BrowserFactory、CookieUntils、WindowHandle（基础工具类）。
