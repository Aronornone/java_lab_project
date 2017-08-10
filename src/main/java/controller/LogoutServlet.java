package controller;

import org.apache.log4j.Logger;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        HttpSession httpSession = request.getSession();
        SessionUtils.invalidateSession(httpSession);

        log.info("doGet(request, response): Getting cookies.");
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies) {
            if(cookie.getName().equals("userId")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        log.info("doGet(request, response): Sending redirect.");
        response.sendRedirect("/");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
