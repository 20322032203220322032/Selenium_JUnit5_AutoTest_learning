package cn.tcmeta.logintest.Tools.AutoScreenshot;

import cn.tcmeta.logintest.Tools.GetInformation.GetBrowserInfo;
import cn.tcmeta.logintest.Tools.GetInformation.GetCurrentMethodName;
import cn.tcmeta.logintest.Tools.GetInformation.GetRealWebDriver;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FullPageScreenshot {

    private static final Logger log = LoggerFactory.getLogger(FullPageScreenshot.class);

    public  static void fullPageScreenshot(WebDriver webdriver, String name){
        String testId;
        if(!name.contains("失败于")) {
            testId = GetCurrentMethodName.getCurrentMethodName();
        }
        else {
            testId = "报错截图";
        }
        String browserType= GetBrowserInfo.getBrowserInfo(webdriver)[0];
        String browserVersion=GetBrowserInfo.getBrowserInfo(webdriver)[1];
        WebDriver webdriverReal= GetRealWebDriver.getRealWebDriver(webdriver);
        File fullpageScreenshotFile=((TakesScreenshot)webdriverReal).getScreenshotAs(OutputType.FILE);
        String fullpageScreenshotPath=System.getProperty("user.dir")
                +File.separator +"src"
                +File.separator +"test"
                +File.separator +"test_results"
                +File.separator+"fullpagescreenshot"
                +File.separator+ testId +"(用例编号)"
                +File.separator+browserType+browserVersion+"浏览器"
                +File.separator +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"))
                +name+".png";

        try {
            File FullpageScreenshotFile=new File(fullpageScreenshotPath);//
            File parentDir=FullpageScreenshotFile.getParentFile();
            if(parentDir==null||!parentDir.exists()){
                parentDir.mkdirs();
            }
            FileUtils.copyFile(fullpageScreenshotFile,FullpageScreenshotFile);
            //添加附件
            Allure.attachment(name, new ByteArrayInputStream(
                    FileUtils.readFileToByteArray(FullpageScreenshotFile)));//readFileToByteArray将文件内容读入字节数组中
        } catch(IOException e){
            log.error("图片保存失败", e);
        }
    }


}


