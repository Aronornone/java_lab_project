package stubs;

import pojo.*;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/bucket"})
public class StubBucketServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        Integer numberTickets = (Integer) httpSession.getAttribute("numberTickets");

        if (numberTickets == null) {
            numberTickets = 0;
            String bucketEmpty = "Bucket empty.";
            request.setAttribute("bucketEmpty", bucketEmpty);
        }

        //Должно быть создание нового заказа
        Invoice invoice = new Invoice(1, user, Invoice.InvoiceStatus.CREATED, 2, LocalDateTime.now());
        Airplane airplane = new Airplane(1, "Airbus 320", 202, 23);

        String flightIdString = SessionUtils.checkFlightSession(httpSession,request);

        if (flightIdString != null) {
            Long flightId = Long.parseLong(flightIdString);
            httpSession.setAttribute("flightId", flightId);
            //Здесь будет из БД запрос по Id
            Airport SPB = new Airport(1, "SPB", "Saint-Petersburg");
            Airport MSK = new Airport(2, "MSK", "Saint-Petersburg");
            Airport HEL = new Airport(3, "HEL", "Saint-Petersburg");
            Flight flight1 = new Flight(1, airplane, "12-D", SPB, MSK, 10052.30, 202, 23, LocalDateTime.of(2017, 07, 10, 23, 59));
            Flight flight2 = new Flight(2, airplane, "15-3", MSK, HEL, 11002.30, 202, 23, LocalDateTime.of(2017, 07, 15, 12, 48));
            Flight flight3 = new Flight(3, airplane, "15-5", SPB, HEL, 11002.30, 202, 23, LocalDateTime.of(2017, 07, 26, 12, 48));
            List<Flight> allFlights = new ArrayList<>();
            allFlights.add(flight1);
            allFlights.add(flight2);
            allFlights.add(flight3);

            for (Flight flight : allFlights) {
                if (flight.getFlightId() == flightId) {
                    Flight ticketFlight = flight;
                    request.setAttribute("flightNumber", ticketFlight.getFlightNumber());
                    request.setAttribute("departureAirport", ticketFlight.getDepartureAirport());
                    request.setAttribute("arrivalAirport", ticketFlight.getArrivalAirport());
                    request.setAttribute("dateTime", ticketFlight.getDateTime());
                    request.setAttribute("airplanename", ticketFlight.getAirplane().getName());
                }
            }
        }

        List<Ticket> tickets = new ArrayList<>();
        double sumTotal = 0;

        if (numberTickets != 0)

        {
            //Здесь нужно добавление tickets в базу
            for (int i = 0; i < numberTickets; i++) {
                tickets.add(new Ticket());
            }
            //Для проверки сумм, нужно добавить логику расчета цены
            tickets.get(0).setPrice(10.3);
            for (Ticket ticket : tickets) {
                sumTotal = sumTotal + ticket.getPrice();
            }
        }

        request.setAttribute("tickets", tickets);
        request.setAttribute("totalSum", sumTotal);

        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
