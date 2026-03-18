package cn.tcmeta.logintest.Tools;

import cn.tcmeta.logintest.Tools.AutoScreenshot.WebdriverExtension;
import cn.tcmeta.logintest.Tools.GetInformation.GetBrowserInfo;
import cn.tcmeta.logintest.Tools.GetInformation.GetCurrentMethodName;
import cn.tcmeta.logintest.Tools.GetInformation.GetRealWebDriver;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class CookieUtils {
    private static String cookiePath;
    /**
     * 保存登录后的Cookie到本地文件
     */
    public static void saveLoginCookie(WebDriver webdriver){
        String browserType= GetBrowserInfo.getBrowserInfo(webdriver)[0];
        String browserVersion= GetBrowserInfo.getBrowserInfo(webdriver)[1];
        WebDriver webdriverReal= GetRealWebDriver.getRealWebDriver(webdriver);
        cookiePath=System.getProperty("user.dir")
                +File.separator +"src"
                +File.separator +"test"
                +File.separator +"test_results"
                + File.separator + "login_cookie"
                +File.separator+browserType+browserVersion+"浏览器"
                +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"))+".data";
        File cookieFile=new File(cookiePath);
        File parentDir=cookieFile.getParentFile();
        if(parentDir!=null&&!parentDir.exists()){
            parentDir.mkdirs();
        }
        //保存Cookie
        try(ObjectOutputStream outputStream=new ObjectOutputStream((new FileOutputStream(cookieFile)))){
            Set<Cookie> cookies=webdriverReal.manage().getCookies();
            outputStream.writeObject(cookies);
            System.out.println("登录Cookie已保存至：" + cookiePath);
        } catch (IOException e ){
            System.err.println("保存Cookie时出错:"+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 加载Cookie到浏览器，实现自动登录
     */
    public static void loadLoginCookie(WebDriver webdriver) {
        WebDriver webdriverReal= GetRealWebDriver.getRealWebDriver(webdriver);
        File cookieFile = new File(cookiePath);
        if (!cookieFile.exists()) {
            System.err.println("Cookie文件不存在:" + cookiePath);
            return;
        }
        try (ObjectInputStream inputStream = new ObjectInputStream((new FileInputStream(cookieFile)))) {
            webdriverReal.manage().deleteAllCookies();
            Set<Cookie> cookies = (Set<Cookie>) inputStream.readObject();//会触发JAVA安全警告
            for (Cookie cookie : cookies) {

                webdriverReal.manage().addCookie(cookie);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载Cookie时出错:"+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 删除Cookie文件（用于清理测试数据）
     */
    public static void deleteLoginCookie(){
        File cookieFile=new File(cookiePath);
        if(!cookieFile.exists()){
            System.out.println("Cookie文件已不存在:" + cookiePath);
            return;
        }
        boolean detele=cookieFile.delete();
        if(!detele){
            System.err.println("删除Cookie文件时出错:" + cookiePath);
        }
        System.out.println("Cookie文件已删除:" + cookiePath);
    }

}
