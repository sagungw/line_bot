package scrappers;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WebDriver extends PhantomJSDriver {

    private static String DEFAULT_SCREENSHOT_LOCATION = "/Users/sagungwijaya/Documents/";

    public WebDriver(DesiredCapabilities capabilities) {
        super(capabilities);
    }

    public void screenShotCurrentPage() {
        File file = ((TakesScreenshot)this).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(DEFAULT_SCREENSHOT_LOCATION + DateTime.now(DateTimeZone.forID("Asia/Jakarta")) + "-SS.png"));
            Logger.info("Screen captured successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WebElement smartFindElement(String selector) {
        return isXpathExpression(selector) ? this.findElement(By.xpath(selector)) : this.findElement(By.cssSelector(selector));
    }

    public List<WebElement> smartFindElements(String selector) {
        return isXpathExpression(selector) ? this.findElements(By.xpath(selector)) : this.findElements(By.cssSelector(selector));
    }

    public void withClickAndBack(WebElement element, Runnable runnable) {
        element.click();
        runnable.run();
        this.navigate().back();
    }

    private boolean isXpathExpression(String selector) {
        return selector.startsWith("/");
    }

}
