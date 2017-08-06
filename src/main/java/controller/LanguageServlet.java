package controller;

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String locale = request.getParameter("locale");
        request.getSession().setAttribute("currentLocale", getServletContext().getAttribute(locale));

        getServletContext().setAttribute("errors", ResourceBundle.
                getBundle("ErrorsBundle", (Locale) request.getSession().getAttribute("currentLocale")));

//        send redirect
        String goBack=request.getParameter("backPage");
        if (goBack.equals("")) goBack="/";
        response.sendRedirect(""+goBack);
    }
}
