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
        if (user==null) {
            //заглушка, будет еще предупреждение, что нужно сначала войти + сохранение выбранных фильтров
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        Integer numberTickets = (Integer) httpSession.getAttribute("numberTickets");

        if (numberTickets == null) {
            numberTickets = 0;
            String bucketEmpty = "Bucket empty.";
            request.setAttribute("bucketEmpty", bucketEmpty);
        }

        //TODO: Должно быть создание нового заказа
        Invoice invoice = new Invoice(1, user, Invoice.InvoiceStatus.CREATED, 2, LocalDateTime.now());

        String flightIdString = SessionUtils.checkFlightSession(httpSession,request);

        if (flightIdString != null) {
            Long flightId = Long.parseLong(flightIdString);
            httpSession.setAttribute("flightId", flightId);
            //TODO: Упростить когда будет DAO
            List<Airport> airports = StubUtils.getAirports();
            List<Airplane> airplanes = StubUtils.getAirplanes();
            List<Flight> flights = StubUtils.getFlights(airports,airplanes);

            for (Flight flight : flights) {
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
