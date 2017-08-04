package stubs;

import pojo.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ResourceBundle;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/save"})
public class StubBucketSaveServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");

        httpSession.setAttribute("lastServletPath", request.getServletPath());
        //int ticketsNumber = StubUtils.getNumberOfTicketsInInvoice(user);

        String[] ticketsIds = request.getParameterValues("ticketId");
        String[] passengerNames = request.getParameterValues("passengerName");
        String[] passports = request.getParameterValues("passport");

        httpSession.setAttribute("ticketsArray", ticketsIds);
        httpSession.setAttribute("passengersArray", passengerNames);
        httpSession.setAttribute("passportsArray", passports);

        if (StubUtils.checkEmptyAndSaveForPay(ticketsIds, passengerNames, passports)) {
            request.setAttribute("setFields", err.getString("setFields"));
            request.setAttribute("changesSaved", err.getString("changesSaved"));
            request.getRequestDispatcher("/bucket").forward(request, response);
        } else {
            request.setAttribute("changesSaved", err.getString("changesSaved"));
        }

        request.getRequestDispatcher("/bucket").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
