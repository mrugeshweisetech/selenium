package org.example.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.example.utils.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Homepage {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        ExtentReports extent = ExtentReportManager.getReportInstance();
        ExtentTest test = ExtentReportManager.createTest("Homepage Test");

        try {
            driver.get("https://dev.weisetechdev.com/weisetech/");
            Thread.sleep(2000);

            Screenshot currentScreenshot = new AShot().takeScreenshot(driver);

            BufferedImage baseline = ImageIO.read(new File("baseline/homepage.png"));
            BufferedImage actual = currentScreenshot.getImage();

            ImageDiff diff = new ImageDiffer().makeDiff(baseline, actual);

            if (diff.hasDiff()) {

                ImageIO.write(diff.getMarkedImage(), "PNG", new File("diffs/homepage_diff.png"));
                test.fail(" Visual mismatch detected. See 'diffs/homepage_diff.png' for details.");
            } else {
                test.pass(" Visual check passed. No UI differences found.");
            }

        } catch (Exception e) {
            test.fail("️ Visual check failed: " + e.getMessage());
        }
    }
}