package listeners;

import utils.SessionUtils;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.LocalDateTime;


@WebListener
public class OurHttpSessionListener implements HttpSessionListener {
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession httpSession = se.getSession();
        SessionUtils.invalidateSession(httpSession);
        System.out.println("Session ID:" + httpSession.getId() +
                " has destroyed at: " + LocalDateTime.now());
    }
}
