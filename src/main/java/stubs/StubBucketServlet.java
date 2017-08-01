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
import java.util.ResourceBundle;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/bucket"})
public class StubBucketServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        Long invoiceIdSession = (Long) httpSession.getAttribute("invoiceId");
        if (user == null) {
            //заглушка, будет еще предупреждение, что нужно сначала войти + сохранение выбранных фильтров
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");
        int numberTicketsFilter;
        if (numberTicketsFilterString == null) {
            numberTicketsFilter = 0;
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
        } else {
            numberTicketsFilter = Integer.parseInt(numberTicketsFilterString);
        }
        Invoice invoice;
        String invoiceIdRequest = (String) request.getAttribute("invoiceId");
        if ((invoiceIdRequest == null) && (invoiceIdSession == null)) {
            invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, numberTicketsFilter, LocalDateTime.now());
            //TODO: заказ в базе тоже должен создаться
            invoice.setInvoiceId(1); //временно
        }
        //TODO: прописать логику создания заказа, если уже есть в сессии, причем не менялся то не нужно создавать
        // и если нет - нужно
        else if (invoiceIdRequest == null) {
            invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, numberTicketsFilter, LocalDateTime.now());
        } else if (invoiceIdSession == null) {
            invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, numberTicketsFilter, LocalDateTime.now());
        } else if (!invoiceIdRequest.equals(invoiceIdSession.toString())) {
            //должен прошлый заказ устанавливаться в CANCELLED и создаваться новый
            //TODO: добавить установку прошлого в CANCELLED
            invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, numberTicketsFilter, LocalDateTime.now());
            invoice.setInvoiceId(2); //временно
        } else {
            invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, numberTicketsFilter, LocalDateTime.now());
        }
        httpSession.setAttribute("invoiceId", invoice.getInvoiceId());

        String flightIdString = SessionUtils.checkFlightSession(httpSession, request);

        if (flightIdString != null) {
            Long flightId = Long.parseLong(flightIdString);
            httpSession.setAttribute("flightId", flightId);
            //TODO: Упростить когда будет DAO
            List<Airport> airports = StubUtils.getAirports();
            List<Airplane> airplanes = StubUtils.getAirplanes();
            List<Flight> flights = StubUtils.getFlights(airports, airplanes);

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

        if (numberTicketsFilter != 0)

        {
            //Здесь нужно добавление tickets в базу
            for (int i = 0; i < numberTicketsFilter; i++) {
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
