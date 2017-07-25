package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//getMaxAge() always return -1 because of:
// https://stackoverflow.com/questions/14391749/in-java-servlet-cookie-getmaxage-always-returns-1

/**
 * Class for example how to write Cookies and Session
 */
@WebServlet(urlPatterns = {"/bucket"}, name = "BucketServlet")
public class CookiesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println(Collections.list(request.getHeaderNames()));

        System.out.println(request.isRequestedSessionIdFromCookie());
        System.out.println(request.isRequestedSessionIdFromURL());
        System.out.println(request.isRequestedSessionIdValid());

        HttpSession httpSession = request.getSession(true);
        System.out.println(httpSession.getLastAccessedTime());
        System.out.println(httpSession.getMaxInactiveInterval());

        String sessionIdPreCookie = (request.getRemoteAddr() + new Date().toString()).replace(' ','_');;
        String userLocaleIdPreCookie = request.getLocale().getDisplayName().replace(' ','_');

        Cookie sessionId = new Cookie("session_id", sessionIdPreCookie);
        Cookie userLocale = new Cookie("user_locale", userLocaleIdPreCookie);

        System.out.println(sessionId.getValue());
        System.out.println(userLocale.getValue());
        sessionId.setMaxAge(900);
        userLocale.setMaxAge(900);

        response.addCookie(sessionId);
        response.addCookie(userLocale);

        List<String> list = new ArrayList<>();
        list.add("String1");
        list.add("String2");
        httpSession.setAttribute("list", list);

        long lastTime = httpSession.getLastAccessedTime();
        String id = httpSession.getId();
        httpSession.setAttribute("lastTime", lastTime);
        httpSession.setAttribute("sessionId", id);

        RequestDispatcher rd = getServletContext().
                getRequestDispatcher("/WEB-INF/pages/cookies.jsp");
        rd.forward(request, response);
    }
}

