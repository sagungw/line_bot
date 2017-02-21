import actors.TestActor;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import play.libs.akka.AkkaGuiceSupport;
import scrapper.WebScrapper;

import java.time.Clock;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        bindActor(TestActor.class, "test-actor");
    }

    @Provides
    public WebScrapper provideWebScrapper() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/Cellar/phantomjs/2.1.1/bin/phantomjs");

        return new WebScrapper(capabilities);
    }

}
