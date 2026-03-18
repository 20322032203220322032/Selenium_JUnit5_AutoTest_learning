package cn.tcmeta.logintest.Tools.AutoScreenshot;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.lang.reflect.InvocationHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import cn.tcmeta.logintest.Tools.BrowserFactory;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebdriverExtension implements ParameterResolver,  AfterEachCallback {
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(WebdriverExtension.class);
    private static final String WEBDRIVER = "webdriver";
    private static final String BROWSER_TYPE = "browserType";
    private static final String DEFAULT_BROWSER = "chrome";
    private static final Semaphore DRIVER_SEMAPHORE = new Semaphore(4);
    // 驱动创建超时时间（避免线程阻塞）
    private static final int SEMAPHORE_TIMEOUT = 30;
    private static final Logger log = LoggerFactory.getLogger(WebdriverExtension.class);

    /*supportsParameter是JUnit5 ParameterResolver接口中的一个方法，用于判断当前测试方法的某个参数是否需要由这个解析器来提供值。
    这个方法返回一个布尔值：
    返回 true：表示"这个参数我能解决"，JUnit 会调用 resolveParameter 方法来提供参数值
    返回 false：表示"这个参数我解决不了"，JUnit 会尝试其他 ParameterResolver 或抛出异常
     */
    /**
     *通过重写supportsParameter方法，判断参数类型是否为WebDriver类型，如果是则返回true，否则返回false
     */
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(WebDriver.class);
    }


    /*resolveParameter是JUnit5 ParameterResolver接口中的一个方法，用于返回参数值。*/
    /**
    *通过重写resolveParameter在运行时动态创建一个实现了WebDriver接口的代理对象，实现WebDriver接口代理对象的懒加载模式
     */
    @Override
    public WebDriver resolveParameter(final ParameterContext parameterContext,
                                      final ExtensionContext extensionContext) throws ParameterResolutionException {
        /*
         Proxy.newProxyInstance()这是一个 Java 动态代理的实现，用于创建一个WebDriver的代理对象
        该方法通过 Java 反射机制，在运行时动态创建一个实现了WebDriver接口的代理对象，
        而不是直接创建真实的 WebDriver 实例（如 ChromeDriver）
         */
        //实现了一个懒加载（Lazy Loading）模式
        return (WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),//参数 1: 使用 WebDriver 接口的类加载器
                new Class<?>[]{WebDriver.class},//参数 2: 指定代理对象要实现 WebDriver 接口
                new LazyWebDriverHandler(extensionContext)//参数 3: 调用处理器，拦截所有对代理对象的方法调用
        );
    }

//    @Override
//    public void beforeEach(final ExtensionContext context) {
//        final ExtensionContext.Store store = context.getStore(NAMESPACE);
//        store.put(WEBDRIVER, new ChromeDriver());
//
//    }
/*重写beforeEach已经不需要了，每次测试用例方法导入WebDriver对象时，会调用重写的supportsParameter方法，
只要参数类型为WebDriver，则返回true，调用resolveParameter()，
由于重写了resolveParameter使之实现了一个懒加载（Lazy Loading）模式，
会调用处理器进行初始化真实驱动，将WebDriver对象保存在ExtensionContext.Store中。
所以不在需要在beforeEach进行初始化WebDriver对象了
 */

    @Override
    public void afterEach(final ExtensionContext context) {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        final WebDriver maybeDriver = store.get(WEBDRIVER, WebDriver.class);
        try{
            if (Objects.nonNull(maybeDriver)) {
                maybeDriver.quit();
            }
        }finally {
            DRIVER_SEMAPHORE.release();
        }
        // 清理浏览器类型缓存
        store.remove(BROWSER_TYPE);
        log.info("关闭浏览器，释放信号量,清理浏览器类型缓存");
    }

    /*处理器，拦截所有对WebDriver接口的代理对象的方法调用，
    通过重写invoke方法，在其中调用initializeRealDriver()，对NAMESPACE的context中有无明确BROWSER_TYPE进行判断并创建对应的驱动，
    如果没有，则使用默认的Chrome浏览器，如果有，则调用BrowserFactory.setBrowser(browserType)使用指定的浏览器,
     */
    private static class LazyWebDriverHandler implements InvocationHandler {
        private final ExtensionContext context;
        private WebDriver realDriver;

        public LazyWebDriverHandler(ExtensionContext context) {
            this.context = context;
        }

        //重写invoke方法，拦截所有对代理对象的方法调用
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (realDriver == null) {
                initializeRealDriver();
            }
            return method.invoke(realDriver, args);
        }

        //初始化真实驱动
        private void initializeRealDriver() {
            boolean acquired= false;
            try {
                // 核心：获取信号量许可（最多 4 个并发），超时则抛出异常
                acquired = DRIVER_SEMAPHORE.tryAcquire(SEMAPHORE_TIMEOUT, TimeUnit.SECONDS);
                if (!acquired) {
                    int usedPermits = 4 - DRIVER_SEMAPHORE.availablePermits();
                    log.error("获取浏览器驱动许可超时！当前已使用许可数：{}/4，可用许可：{}",
                            usedPermits, DRIVER_SEMAPHORE.availablePermits());
                    throw new RuntimeException(String.format(
                            "获取浏览器驱动许可超时！当前并发数已达上限 (%d/4)，请等待其他测试完成或检查是否有浏览器未正常关闭",
                            usedPermits));
                }

                ExtensionContext.Store store = context.getStore(NAMESPACE);
                String browserType = store.get(BROWSER_TYPE, String.class);
                if (browserType == null || browserType.isEmpty()) {
                    browserType = DEFAULT_BROWSER;
                }

                // 创建真实驱动
                realDriver = BrowserFactory.setBrowser(browserType);
                store.put(WEBDRIVER, realDriver);
                log.info("浏览器 [{}] 初始化成功，当前剩余许可：{}", browserType, DRIVER_SEMAPHORE.availablePermits());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();//恢复中断
                log.error("驱动创建被中断：", e);
                throw new RuntimeException("驱动创建被中断：", e);
            } catch (Exception e) {
                // 如果发生异常且已获取许可，释放许可
                if (acquired) {
                    DRIVER_SEMAPHORE.release();
                    log.warn("浏览器初始化失败，已释放信号量许可");
                }
                log.error("浏览器初始化失败：", e);
                throw new RuntimeException("浏览器初始化失败：" + e.getMessage(), e);
            }
        }
        /**
         * 获取真实的 WebDriver 实例
         * 如果还未初始化，会先创建驱动
         */
        public WebDriver getRealDriver() {
            if (realDriver == null) {
                initializeRealDriver();
            }
            return realDriver;
        }
    }
    // 提供非静态方法getWebDriver()获取WebDriver对象
    public WebDriver getWebDriver(ExtensionContext context) {
        final ExtensionContext.Store store = context.getStore(NAMESPACE);
        return store.get(WEBDRIVER, WebDriver.class);
    }
    //提供方法setBrowserType()设置浏览器类型
    public void setBrowserType(ExtensionContext context, String browserType) {
        context.getStore(WebdriverExtension.NAMESPACE).put(WebdriverExtension.BROWSER_TYPE, browserType);
    }
    /**
     * 从代理 WebDriver 中获取真实的 WebDriver 实例
     * 支持并行测试，直接从 InvocationHandler 中提取
     */
    public static WebDriver unwrap(WebDriver proxyWebDriver) {
        if (proxyWebDriver == null) {
            log.error("WebDriver对象为null");
            return null;
        }

        // 检查是否是 JDK 动态代理
        if (!java.lang.reflect.Proxy.isProxyClass(proxyWebDriver.getClass())) {
            log.info("WebDriver不是JDK动态代理对象", proxyWebDriver.getClass());
            return proxyWebDriver;
        }

        try {
            java.lang.reflect.InvocationHandler handler =
                    java.lang.reflect.Proxy.getInvocationHandler(proxyWebDriver);

            // 如果是我们的 LazyWebDriverHandler，直接获取 realDriver
            if (handler instanceof LazyWebDriverHandler) {
                log.info("从LazyWebDriverHandler中获取真实WebDriver实例成功", handler.getClass());
                return ((LazyWebDriverHandler) handler).getRealDriver();
            }

            // 兼容其他类型的 Handler（反射方式）
            Class<?> handlerClass = handler.getClass();
            try {
                java.lang.reflect.Field realDriverField =
                        handlerClass.getDeclaredField("realDriver");
                realDriverField.setAccessible(true);
                Object realDriver = realDriverField.get(handler);

                if (realDriver instanceof WebDriver) {
                    log.info("从代理WebDriver中获取真实WebDriver实例成功", realDriver.getClass());
                    return (WebDriver) realDriver;
                }
            } catch (NoSuchFieldException ignored) {
                // 忽略，返回原对象
                log.info("忽略异常NoSuchFieldException，返回原对象");
            }
        } catch (Exception e) {
            // 提取失败，返回原对象
            log.error("从代理WebDriver中获取真实WebDriver实例失败,返回原对象", e);
        }

        return proxyWebDriver;
    }


}
