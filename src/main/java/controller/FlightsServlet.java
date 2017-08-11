package controller;

import db.services.interfaces.AirportService;
import db.services.servicesimpl.AirportServiceImpl;
import org.apache.log4j.Logger;
import pojo.Airport;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet for base page of app, it is first that user see when enter site, include unsigned users
 */
@WebServlet(urlPatterns = {"", "/flights"})
public class FlightsServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static AirportService airportService;

    public void init() {
        log.info("init(): Initializing 'airportService'.");
        airportService = AirportServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("lastServletPath", request.getServletPath());
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);

        //get locale of user and set it by default if null
        if (httpSession.getAttribute("currentLocale") == null) {
            httpSession.setAttribute("currentLocale", Locale.getDefault());
            getServletContext().setAttribute("errors", ResourceBundle.
                    getBundle("ErrorsBundle", Locale.getDefault()));
        }

        //prepare list of airports for filters
        log.info("doGet(request, response): Getting a list of all airports from 'airportService'.");
        List<Airport> airports = airportService.getAll();
        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        log.info("doGet(request, response): Executing request.getRequestDispatcher(...).");
        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}