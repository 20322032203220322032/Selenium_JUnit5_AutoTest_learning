# Selenium_JUnit5_AutoTest_learning
借助github上的小说精品屋项目学习自动化UI测试，小说精品屋登录界面网址http://117.72.165.13:8888/user/login.html

目前测试仅了登录功能模块，所以这是一个人 demo 级项目。

项目结构介绍：
src/test存放项目代码（/java/cn/tcmeta/logintest/test）及测试结果(/resources);

/java/cn/tcmeta/logintest/test目录下，

1.PageSetpMethods存放各个页面对象的各种方法（如usernameInputIsDisplayedAssert()、loginButtonIsEnabledAssert()等验证方法及如clickRegisterButton()等元素交互方法）。

这么设计原因是防止页面对象基础方法过多，影响代码的可观性及可维护性，这样写也方便页面对象基础方法的集中管理与维护。还有，值得一提的是我每个PageMerhod都使用了Map（Java的字典功能），利用ImmutableMap的put()方法将元素定位By与元素名称、文本属性绑定，方便集中管理，并减少硬编码。

2.TestPages存放页面对象PageNamePage类。

页面对象模型（Page Object Model），我是借助Goodles翻译在Selenium官网：https://www.selenium.dev/zh-cn/documentation/test_practices/encouraged/page_object_models/了解到的，但很遗憾我还未实际掌握页面组件对象Page Component Objects的使用。我起先将LoginTestApplicationTests中的TC_LOGIN_001T登录界面关键元素（即usernameInput账号输入框、passwordInput密码输入框及loginButton登录按钮）的UI可见性、可用性和文本验证测试及登录的逻辑方法放入LoginPage中，但是我的测试用例方法中依旧存在大量的wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='register'][class='btn_ora_white']")).click();之类的硬编码，然后我在参考AI的优化建议下，我将这些操作写成基础方法存放在LoginPage中，这就导致我的LoginPage的代码非常的肿胀，看起来头疼，于是后来为了提高代码的可观性及可维护性，我编写了两个方法类ElementAssert



