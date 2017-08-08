package controller;

import db.services.interfaces.AirportService;
import db.services.interfaces.UserService;
import db.services.servicesimpl.AirportServiceImpl;
import db.services.servicesimpl.UserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
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
    private static AirportService aps = new AirportServiceImpl();
    private static UserService us = new UserServiceImpl();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        String email = request.getParameter("email");
        String nonHashedPasswordReq = request.getParameter("password");

        if ((nonHashedPasswordReq == null || (email == null ||
                nonHashedPasswordReq.isEmpty()) || email.isEmpty())) {
            request.setAttribute("fieldEmpty", err.getString("fieldEmpty"));
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);

        } else {
            String passwordHashDB = "";
            User user = null;
            String passwordHashReq = DigestUtils.md5Hex(nonHashedPasswordReq);
            Optional<User> userOptional = us.get(email);
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

                List<Airport> airports = aps.getAll();
                request.setAttribute("departures", airports);
                request.setAttribute("arrivals", airports);

                String dateFromString = (String) httpSession.getAttribute("dateFrom");
                String dateToString = (String) httpSession.getAttribute("dateTo");
                String departure = (String) httpSession.getAttribute("departureF");
                String arrival = (String) httpSession.getAttribute("arrivalF");
                String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");
                String[] checkBox = (String[]) httpSession.getAttribute("business");

                if ((dateFromString == null) ||
                        (dateToString == null) ||
                        (departure == null) ||
                        (arrival == null) ||
                        (numberTicketsFilterString == null)) {
                    response.sendRedirect("/");
                } else if (checkBox != null) {
                    String redirectBackString = "/doSearch?dateFrom=" + dateFromString + "&dateTo=" + dateToString +
                            "&selectedDeparture=" + departure + "&selectedArrival=" + arrival +
                            "&numberTicketsFilter=" + numberTicketsFilterString + "&box=" + checkBox[0];
                    response.sendRedirect(redirectBackString);
                } else {
                    String redirectBackString = "/doSearch?dateFrom=" + dateFromString + "&dateTo=" + dateToString +
                            "&selectedDeparture=" + departure + "&selectedArrival=" + arrival +
                            "&numberTicketsFilter=" + numberTicketsFilterString;
                    response.sendRedirect(redirectBackString);

                }
            } else {
                request.setAttribute("loginFailed", err.getString("loginFailed"));
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
