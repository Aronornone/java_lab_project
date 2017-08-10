package controller;

import db.services.interfaces.AirportService;
import db.services.interfaces.UserService;
import db.services.servicesimpl.AirportServiceImpl;
import db.services.servicesimpl.UserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import pojo.Airport;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/doLogin"})
public class DoLoginServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servLog");
    private static AirportService airportService;
    private static UserService userService;

    public void init() {
        log.info("init(): Initializing 'airportService' and 'userService'.");
        airportService = AirportServiceImpl.getInstance();
        userService = UserServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        String email = request.getParameter("email");
        String nonHashedPasswordReq = request.getParameter("password");

        log.info("doGet(request, response): Trying to login.");
        if ((nonHashedPasswordReq == null || (email == null ||
                nonHashedPasswordReq.isEmpty()) || email.isEmpty())) {
            request.setAttribute("fieldEmpty", err.getString("fieldEmpty"));
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);

        } else {
            String passwordHashDB = "";
            User user = null;
            String passwordHashReq = DigestUtils.md5Hex(nonHashedPasswordReq);
            Optional<User> userOptional = userService.get(email);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                passwordHashDB = user.getPasswordHash();
            } else {
                request.setAttribute("nonexistentLogin", err.getString("nonexistentLogin"));
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            }

            if (passwordHashDB.equals(passwordHashReq)) {
                httpSession.setAttribute("user", user);

                Cookie cookieUserId;
                cookieUserId = new Cookie("userId", String.valueOf(user.getUserId()));
                cookieUserId.setMaxAge(60 * 60 * 24 * 7); //one week
                response.addCookie(cookieUserId);

                List<Airport> airports = airportService.getAll();
                request.setAttribute("departures", airports);
                request.setAttribute("arrivals", airports);

                String dateFromString = (String) httpSession.getAttribute("dateFrom");
                String dateToString = (String) httpSession.getAttribute("dateTo");
                String departure = (String) httpSession.getAttribute("departureF");
                String arrival = (String) httpSession.getAttribute("arrivalF");
                String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");
                String[] checkBox = (String[]) httpSession.getAttribute("business");

                StringBuilder redirectBackStringBuilder = new StringBuilder();
                redirectBackStringBuilder.append("/doSearch?dateFrom=").append(dateFromString).append("&dateTo=").
                        append(dateToString).append("&selectedDeparture=").append(departure).append("&selectedArrival=").
                        append(arrival).append("&numberTicketsFilter=").append(numberTicketsFilterString);
                if (checkBox != null) {
                    redirectBackStringBuilder.append("&box=").append(checkBox[0]);
                }

                String redirectBackString = redirectBackStringBuilder.toString();

                if ((dateFromString == null) ||
                        (dateToString == null) ||
                        (departure == null) ||
                        (arrival == null) ||
                        (numberTicketsFilterString == null)) {
                    response.sendRedirect("/");
                }
                else {
                    response.sendRedirect(redirectBackString);
                }
            } else {
                log.info("doGet(request, response): Log-in failed!");
                request.setAttribute("loginFailed", err.getString("loginFailed"));
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
