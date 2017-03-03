import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import models.builders.SiteBuilder;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import scrappers.WebDriver;

import java.time.Clock;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        bind(SiteBuilder.class).asEagerSingleton();
    }

//    @Provides
//    public WebDriver provideWebScrapper() {
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setJavascriptEnabled(true);
//        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/Cellar/phantomjs/2.1.1/bin/phantomjs");
//
//        return new WebDriver(capabilities);
//    }

}
