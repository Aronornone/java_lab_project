package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class for example how to write directly to html page
 */
@WebServlet(urlPatterns = "/hello", name = "HelloServlet")
public class HelloWorldServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<body>");
            out.println("<h2>");
            out.println("HelloServlet");
            out.println("</h2>");
            out.println("<form action=\"index\" method=\"post\"> <input type=\"submit\" value=\"To Index\"> </form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request,response);
    }

}
