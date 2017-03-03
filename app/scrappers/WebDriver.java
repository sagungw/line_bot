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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class WebDriver extends PhantomJSDriver {

    private static String DEFAULT_SCREENSHOT_LOCATION = "/Users/sagungwijaya/Documents/";

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public WebDriver(DesiredCapabilities capabilities) {
        super(capabilities);
    }

    public void screenShotCurrentPage() {
        File file = ((TakesScreenshot)this).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(DEFAULT_SCREENSHOT_LOCATION + DateTime.now(DateTimeZone.forID("Asia/Jakarta")) + "-SS.png"));
            logger.fine("Screen captured successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WebElement findElement(String selector) {
        return isXpathExpression(selector) ? this.findElement(By.xpath(selector)) : this.findElement(By.cssSelector(selector));
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
