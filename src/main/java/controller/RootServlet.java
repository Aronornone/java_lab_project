package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// with urlPattern "/" we can't include css through simple waym onl absolute paths:
// https://stackoverflow.com/questions/4140448/difference-between-and-in-servlet-mapping-url-pattern
// https://stackoverflow.com/questions/3655316/browser-cant-access-find-relative-resources-like-css-images-and-links-when-cal
@WebServlet(urlPatterns = {"/index"}, name = "RootServlet")
public class RootServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRedirect(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRedirect(request,response);
    }

    private void processRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher =
                request.getRequestDispatcher("/WEB-INF/pages/index.jsp");
        requestDispatcher.forward(request, response);
    }
}

