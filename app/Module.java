import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import play.api.Configuration;
import scrappers.WebDriver;

import java.time.Clock;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
    }

    @Inject
    @Provides
    @Singleton
    public WebDriver provideWebScrapper(Configuration configuration) {
        final String phantomJSPath = configuration.underlying().getString("phantomjs.path");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJSPath);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);

        return new WebDriver(capabilities);
    }

}
