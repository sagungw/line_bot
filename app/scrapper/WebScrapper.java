package scrapper;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class WebScrapper extends PhantomJSDriver {

    private static String DEFAULT_SCREENSHOT_LOCATION = "/Users/sagungwijaya/Documents/";

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public WebScrapper(DesiredCapabilities capabilities) {
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

}
