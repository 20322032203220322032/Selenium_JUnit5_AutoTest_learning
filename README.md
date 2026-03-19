Selenium_JUnit5_AutoTest_learning

一、项目基础

基于小说精品屋登录模块的 UI 自动化测试 demo，仅覆盖登录功能；

测试地址：http://117.72.165.13:8888/user/login.html

二、环境搭建

详情见环境搭建文档，此处仅简单介绍。

个人使用的软件/工具：jdk21.0.4、apache-maven-3.9.12、IntelliJ IDEA 2024.2、浏览器(chrome146.0.7680.80建议使用145版本，Firefox48.0 (64-bit)，146.0.3856.62 (正式版本) (64 位))、allure3.3.1

依赖模块:selenium-java4.41.0、junit-jupiter-api/engine/params5.9.2、webdrivermanager6.3.3、allure-junit52.29.0、slf4j-api2.0.16、mockito-inline5.2.0、spring-boot-starter-parent4.0.1、aspectjweaver1.9.21

核心 Maven 插件配置：

三、项目结构

1.核心目录：src/test/java/cn/tcmeta/logintest/test

1.1PageSetpMethods

作用：集中管理页面对象的验证方法（如 usernameInputIsDisplayedAssert ()）、元素交互方法（如 clickRegisterButton ()）；

设计亮点：通过 ImmutableMap 绑定元素定位 By 与元素名称 / 文本属性，减少硬编码；解决页面对象类方法臃肿问题，提升可读性与可维护性。

1.2TestPages

作用：存放页面对象类（如 LoginPage）；

PO 模型落地：参考 Selenium 官方文档实现（https://www.selenium.dev/zh-cn/documentation/test_practices/encouraged/page_object_models/）；

优化过程：

初始问题：测试用例硬编码多，LoginPage 整合验证 / 交互方法后代码臃肿（27 个方法）；

优化方案：拆分 ElementAssert/ElementInteraction 基础方法类→进一步拆分 LoginPageStepMethods 统一管理；最终 LoginPage 仅保留构造函数与核心业务逻辑方法login ()；

待验证问题：官方建议页面对象不负责断言，但当前通过 HomePage 断言页面标题变更（"会员登录_小说精品屋"→"小说精品屋_原创小说网站"）作为登录成功的第二重验证，该写法减少硬编码但违背官方建议，需后续验证合理性；

待优化：未落地 Page Component Objects，计划在首页测试中应用。

1.3Tools

AboutElement：ElementAssert（元素验证）、ElementInteraction（元素交互）；

AboutScreenshot：FullPageScreenshot（全屏截图）、AutoScreenshotExtension（异常时自动截图，基于 Allure 官方 demo 实现，重写 handleTestExecutionException ()，但对 ExtensionContext 参数及 WebdriverExtension 的并行资源管理逻辑尚未完全掌握）；

GetInformation：GetBrowserInfo（浏览器版本）、GetCurrentMethodName（测试用例编号）、GetRealWebDriver；

其他：BrowserFactory、CookieUntils、WindowHandle（基础工具类）。

1.4LoginTestApplicationTests登录测试

存放小说精品屋登录模块全量自动化测试用例，基于 JUnit5 框架实现用例的组织与执行；通过调用TestPages页面对象层、PageSetpMethods方法层的封装逻辑，结合Tools层的驱动管理、自动截图、工具类等能力，实现登录场景的无硬编码全自动化测试，是项目的用例执行入口。

1.4.1测试用例设计

设计方法：基于等价类划分、边界值分析法设计，覆盖登录模块的正常业务场景与异常异常场景；

覆盖场景：包含正常账号密码登录、账号为空 / 密码为空登录、无效账号 / 错误密码登录、验证码校验拦截（若有）、登录按钮禁用态验证等核心场景，用例编号按规范命名（如TC_LOGIN_001~TC_LOGIN_XXX）；

用例粒度：单场景单用例，每个用例仅验证一个核心业务点，保证用例的独立性、可维护性，便于失败后快速定位问题。

1.4.2核心实现亮点

(1)JUnit5 参数化注入 WebDriver

无需手动编写驱动初始化 / 关闭代码，通过WebdriverExtension实现WebDriver 对象的 JUnit5 参数化注入，用例方法直接声明WebDriver参数即可使用，由框架底层完成懒加载、并发控制、用例结束后自动关闭驱动。

(2)用例失败自动截图 + Allure 报告关联

集成AutoScreenshotExtension，重写 JUnit5 异常处理方法，用例执行抛出异常时自动触发全屏截图，截图文件自动关联至 Allure 测试报告，实现 “失败场景可复现、问题定位有依据”。

(3)无硬编码，完全复用封装层逻辑

用例中无任何driver.findElement()、wait.until()等硬编码代码，所有元素定位、交互操作、状态验证均通过调用LoginPageStepMethods的封装方法实现，若 UI 发生变更，仅需修改封装层，无需调整用例代码。

(4)支持用例并行执行

基于WebdriverExtension中Semaphore信号量实现最大 4 个浏览器驱动并发控制，在 JUnit5 中配置并行执行后，可实现登录用例的批量并行运行，大幅提升测试执行效率。

(5)登录成功双重验证逻辑

用例中调用LoginPage的login()核心业务方法后，通过两层验证判断登录是否成功：一是验证错误提示是否出现!errorSpan.getText().isEmpty()；二是页面状态验证（调用HomePageStepMethods的页面标题断言方法，验证标题由「会员登录_小说精品屋」变更为「小说精品屋_原创小说网站」），提升用例的验证准确性。

2.测试资源resources

存放Allure配置文件allure.properties、并行的配置文件junit-platform.properties及slf4j日志配置文件simplelogger.properties
