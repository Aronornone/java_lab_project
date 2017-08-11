package filter;

import org.apache.log4j.Logger;
import pojo.User;
import utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/addFlightToInvoice","/ticketDelete","/invoicePay","/bucket","/addFlightToInvoice"})
public class LoginFilter implements Filter {
    private static Logger log = Logger.getLogger("servletLogger");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession httpSession = req.getSession();
        Cookie[] cookies = req.getCookies();
        User user = (User) httpSession.getAttribute("user");
        SessionUtils.checkCookie(cookies, httpSession);
        if (user == null) {
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        log.info("doFilter(request, response, chain): Executing chain.doFilter(request,response).");
        chain.doFilter(request,response);
    }
}
