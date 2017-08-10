package listeners;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Locale;

@WebListener
public class OurServletContextListener implements ServletContextListener {
    private static Logger log = Logger.getLogger("servletLogger");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //setting up available locales when initialized
        log.info("contextInitialized(sce): Received the following 'ServletContextEvent': " + sce.getServletContext());
        sce.getServletContext().setAttribute("enLocale", new Locale("en", "US"));
        sce.getServletContext().setAttribute("ruLocale", new Locale("ru", "RU"));
    }
}
