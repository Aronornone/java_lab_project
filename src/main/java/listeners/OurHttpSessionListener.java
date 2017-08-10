package listeners;

import org.apache.log4j.Logger;
import utils.SessionUtils;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.LocalDateTime;


@WebListener
public class OurHttpSessionListener implements HttpSessionListener {
    private static Logger log = Logger.getLogger("servletLogger");

    public void sessionCreated(HttpSessionEvent se) {
        HttpSession httpSession = se.getSession();
        log.info("sessionCreated(se): Session ID: " + httpSession.getId() +
                " is created at: " + LocalDateTime.now());
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession httpSession = se.getSession();
        SessionUtils.invalidateSession(httpSession);
        log.info("sessionDestroyed(se): Session ID:" + httpSession.getId() +
                " has destroyed at: " + LocalDateTime.now());
    }
}
