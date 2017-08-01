package utils;

import db.service.UserService;
import pojo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtils {

    public static String checkFlightSession(HttpSession httpSession, HttpServletRequest request) {
        Long flightIdSession = (Long) httpSession.getAttribute("flightId");
        String flightIdRequest = request.getParameter("flightId");
        String flightIdString;

        if ((flightIdSession == null) && (flightIdRequest == null)) {
            flightIdString = null;
        } else if (flightIdRequest == null) {
            flightIdString = flightIdSession.toString();
        } else if (flightIdSession == null) {
            flightIdString = flightIdRequest;
        } else if (!flightIdRequest.equals(flightIdSession.toString())) {
            flightIdString = flightIdRequest;
        } else flightIdString = flightIdSession.toString();

        return flightIdString;
    }

    public static void invalidateSession(HttpSession httpSession) {
        //TODO: добавить код для отмены неоплаченных заказов клиента, удаления билетов и возвращения числа забронированных мест
        // HttpSessionListener, HttpSessionEvent

        httpSession.invalidate();

    }

    public static void checkCookie(Cookie[] cookies, HttpServletRequest request,
                                   HttpSession httpSession) {
        User user;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    UserService userService = new UserService();
                    Optional<User> userOptional = userService.get(Integer.parseInt(cookie.getValue()));
                    if (userOptional.isPresent()) {
                        user = userOptional.get();
                        httpSession.setAttribute("user", user);
                    }
                }
            }
        }
    }

    public static void checkInvoice(){

    }
}
