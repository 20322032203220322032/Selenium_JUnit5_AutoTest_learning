package cn.tcmeta.logintest.Tools;
import cn.tcmeta.logintest.Tools.GetInformation.GetRealWebDriver;
import org.openqa.selenium.WebDriver;
import java.util.Set;

public class WindowHandle {
    private WebDriver webdriverReal;
    public String oldWindowHandle;
    public String newWindowHandle;
    public int windowHandleNumber;

    //构造函数
    public WindowHandle(WebDriver webdriver){
        this.webdriverReal=GetRealWebDriver.getRealWebDriver(webdriver);
    }

    //保存窗口句柄
    public void saveWindowHandle(){
        oldWindowHandle=webdriverReal.getWindowHandle();
        Set<String> windowHandles=webdriverReal.getWindowHandles();
        windowHandleNumber=windowHandles.size();
    }
    //切换到新窗口句柄
    public void switchToNewWindHandle(){
        Set<String> windowHandles=webdriverReal.getWindowHandles();
        if(windowHandles.size()>windowHandleNumber) {
            for (String windowHandle : windowHandles) {
                if (windowHandle != null && !windowHandle.equals(oldWindowHandle)) {
                    newWindowHandle = windowHandle;
                }
            }
            windowHandleNumber=windowHandles.size();
            if(newWindowHandle==null){
                throw new RuntimeException("新窗口丢失");
            }
            webdriverReal.switchTo().window(newWindowHandle);
        }
        else {
            switchToOldWindowHandle();
        }
    }
    //切换到原窗口句柄
    public void switchToOldWindowHandle(){
        if(oldWindowHandle==null){
            throw new RuntimeException("原窗口丢失");
        }
        webdriverReal.switchTo().window(oldWindowHandle);
    }
    
}
