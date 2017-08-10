package controller;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = "/language")
public class LanguageServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        String locale = request.getParameter("locale");
        request.getSession().setAttribute("currentLocale", getServletContext().getAttribute(locale));

        getServletContext().setAttribute("errors", ResourceBundle.
                getBundle("ErrorsBundle", (Locale) request.getSession().getAttribute("currentLocale")));

//        send redirect
        log.info("doPost(request, response): Sending redirect.");
        String goBack=request.getParameter("backPage");
        if (goBack.equals("")) {
            goBack="/";
        }
        response.sendRedirect(""+goBack);
    }
}
