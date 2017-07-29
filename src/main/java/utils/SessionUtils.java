package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

        httpSession.invalidate();

    }
}
