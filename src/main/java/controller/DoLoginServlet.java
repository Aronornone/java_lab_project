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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/doLogin"})
public class DoLoginServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static Logger userLogger = Logger.getLogger("userLogger");
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

        //get all parameters from login form
        String email = request.getParameter("email");
        String nonHashedPasswordReq = request.getParameter("password");
        LocalDateTime time;

        //check if all fields aren't empty, if so show notification
        log.info("doGet(request, response): Trying to login.");
        if ((nonHashedPasswordReq == null || (email == null ||
                nonHashedPasswordReq.isEmpty()) || email.isEmpty())) {
            request.setAttribute("fieldEmpty", err.getString("fieldEmpty"));
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        //check if user with entered email exists
        User user = null;
        Optional<User> userOptional = userService.get(email);
        String passwordHashDB = "";
        String passwordHashReq = DigestUtils.md5Hex(nonHashedPasswordReq);

        // if user exists get his password hash from DB
        if (userOptional.isPresent()) {
            user = userOptional.get();
            passwordHashDB = user.getPasswordHash();
        }
        //else notificate about non-existing user
        else {
            time = LocalDateTime.now();
            userLogger.error("doGet(request, response): --> Failed attempt to log-in:\n +" +
                    "email: " + email +
                    "password: " + nonHashedPasswordReq +
                    "time: " + time + "\n"
            );
            request.setAttribute("nonexistentLogin", err.getString("nonexistentLogin"));
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        //check that password hashes from DB and from request are equal and if so create cookie
        // with userId for 1 week
        if (checkIfPasswordHashesEquals(request, response,
                httpSession, user, passwordHashDB, passwordHashReq)) return;

        //if password hashes are not equal show notification
        log.error("doGet(request, response): Log-in failed!");
        request.setAttribute("loginFailed", err.getString("loginFailed"));
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    /**
     * Method checks password hashes equals and if they are save filters to session and redirect user to
     * base page
     * @param request from user
     * @param response to user
     * @param httpSession session for user which completes with attributes
     * @param user user tried to login
     * @param passwordHashDB hash of password from DB
     * @param passwordHashReq hash of password from user request (site form)
     * @return true if user check is ok and return to base page, false if not and
     * show notification
     * @throws IOException
     */
    private boolean checkIfPasswordHashesEquals(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, User user, String passwordHashDB, String passwordHashReq) throws IOException {
        if (passwordHashDB.equals(passwordHashReq)) {
            httpSession.setAttribute("user", user);
            setCookies(response, user);
            getAirports(request);

            //get and save filters in search that were set by user before login
            String dateFromString = (String) httpSession.getAttribute("dateFrom");
            String dateToString = (String) httpSession.getAttribute("dateTo");
            String departure = (String) httpSession.getAttribute("departureF");
            String arrival = (String) httpSession.getAttribute("arrivalF");
            String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");
            String[] checkBox = (String[]) httpSession.getAttribute("business");
            String redirectBackString = getRedirectBackString(dateFromString,
                    dateToString, departure, arrival, numberTicketsFilterString, checkBox);

            //if filters are empty redirect on base page
            if ((dateFromString == null) ||
                    (dateToString == null) ||
                    (departure == null) ||
                    (arrival == null) ||
                    (numberTicketsFilterString == null)) {
                response.sendRedirect("/");
                return true;
            }
            // if filters are set redirect to them
            else {
                response.sendRedirect(redirectBackString);
                return true;
            }
        }
        return false;
    }

    /**
     * Create and set cookies for logged user
     * @param response to user
     * @param user logged user
     */
    private void setCookies(HttpServletResponse response, User user) {
        Cookie cookieUserId;
        cookieUserId = new Cookie("userId", String.valueOf(user.getUserId()));
        cookieUserId.setMaxAge(60 * 60 * 24 * 7); //one week
        response.addCookie(cookieUserId);
    }

    /**
     * get lists of airports
     * @param request whom sent user
     */
    private void getAirports(HttpServletRequest request) {
        List<Airport> airports = airportService.getAll();
        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);
    }

    /**
     * Create back string with which user will be returning for his filters in search
     * @param dateFromString http session attribute
     * @param dateToString http session attribute
     * @param departure http session attribute
     * @param arrival http session attribute
     * @param numberTicketsFilterString http session attribute
     * @param checkBox http session attribute
     * @return ready string to servlet path
     */
    private String getRedirectBackString(String dateFromString, String dateToString, String departure, String arrival, String numberTicketsFilterString, String[] checkBox) {
        StringBuilder redirectBackStringBuilder = new StringBuilder();
        redirectBackStringBuilder.append("/doSearch?dateFrom=").append(dateFromString).append("&dateTo=").
                append(dateToString).append("&selectedDeparture=").append(departure).append("&selectedArrival=").
                append(arrival).append("&numberTicketsFilter=").append(numberTicketsFilterString);
        if (checkBox != null) {
            redirectBackStringBuilder.append("&box=").append(checkBox[0]);
        }
        return redirectBackStringBuilder.toString();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
