package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Locale;

@WebListener
public class OurServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //setting up available locales when initialized
        sce.getServletContext().setAttribute("enLocale", new Locale("en", "US"));
        sce.getServletContext().setAttribute("ruLocale", new Locale("ru", "RU"));
    }
}
