package cn.tcmeta.logintest.Tools.AutoScreenshot;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;

public class AutoScreenshotExtension implements TestExecutionExceptionHandler{
    private static final Logger log = LoggerFactory.getLogger(AutoScreenshotExtension.class);
    //    //webdriver的key,即标识
//    private static final String WebDriver_Key = "My_WebDriver_Key";
//    //以AutoScreenshotExtension.class作为命名空间标识
//    private static final ExtensionContext.Namespace Namespace
//            = ExtensionContext.Namespace.create(AutoScreenshotExtension.class);
    //// 将webdriver保存到context中
//    public static void saveWebDriverToContext(WebDriver webdriver, ExtensionContext context) {
//        //getStore()作用：获取指定命名空间下的存储对象。put()作用：将指定对象保存到指定命名空间下的存储对象中。
//        context.getStore(Namespace).put(WebDriver_Key, webdriver);
//    }
//    // 从context中获取webdriver
//    public static WebDriver getWebDriverFromContext(ExtensionContext context) {
//        return (WebDriver) context.getStore(Namespace).get(WebDriver_Key);
//    }
    private final Function<ExtensionContext, WebDriver> driverSupplier;

    public AutoScreenshotExtension(Function<ExtensionContext, WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }
    // 核心：测试报错时自动执行
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        // 获取webdriver
        //apply() 是执行函数式接口的"计算逻辑"，输入一个参数，返回一个结果
        final WebDriver webdriver = this.driverSupplier.apply(context);//输入：context，返回：webdriver
        /*如果webdriver不为空，
        且抛出的异常不是LoginPage中的isLoginSuccess方法直接抛出的异常TimeoutException和StaleElementReferenceException，
        就进行报错截图
         */
        if (Objects.nonNull(webdriver)&&
                !(isFromLoginPageIsLoginSuccess(throwable)&&(
                        (throwable instanceof TimeoutException)||(throwable instanceof StaleElementReferenceException)
                ))) {
            log.error("测试报错", throwable);
            final String attachmentName = "截图：失败于 (" + throwable.getClass().getSimpleName() + ")";
            FullPageScreenshot.fullPageScreenshot(webdriver, attachmentName);
        }
        throw throwable;
    }
    /**
     * 判断异常是否来自 LoginPage 的 isLoginSuccess 方法
     */
    private boolean isFromLoginPageIsLoginSuccess(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().endsWith(".LoginPage") &&
                    element.getMethodName().equals("isLoginSuccess")) {
                return true;
            }
        }
        return false;
    }
}
