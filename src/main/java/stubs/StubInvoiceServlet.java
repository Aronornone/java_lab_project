package stubs;

import db.service.InvoiceService;
import pojo.*;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static utils.EncodingUtil.encode;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/addFlightToInvoice"})
public class StubInvoiceServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");

        String dateFromString = (String) httpSession.getAttribute("dateFrom");
        String dateToString = (String) httpSession.getAttribute("dateTo");
        String departure = (String) httpSession.getAttribute("departureF");
        String arrival = (String) httpSession.getAttribute("arrivalF");
        String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");

        String redirectBackString="/doSearch?dateFrom="+dateFromString+"&dateTo="+dateToString+
                "&selectedDeparture="+departure+"&selectedArrival="+arrival+
                "&numberTicketsFilter="+numberTicketsFilterString;

        if (user == null) {
            //заглушка, будет еще предупреждение, что нужно сначала войти + сохранение выбранных фильтров
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        int numberTicketsFlight;
        String numberTicketsFlightString = request.getParameter("numberTicketsFlight");
        numberTicketsFlight = Integer.parseInt(numberTicketsFlightString);

        String flightIdString = request.getParameter("flightId");
        Flight invoiceFlight = null;
        if (flightIdString != null) {

            Long flightId = Long.parseLong(flightIdString);
            List<Airplane> airplanes = StubUtils.getAirplanes();
            List<Airport> airports = StubUtils.getAirports();
            List<Flight> flights = StubUtils.getFlights(airports, airplanes);

            for (Flight flight : flights) {
                if (flight.getFlightId() == flightId) {
                    invoiceFlight = flight;
                    request.setAttribute("flightNumber", invoiceFlight.getFlightNumber());
                    request.setAttribute("departureAirport", invoiceFlight.getDepartureAirport());
                    request.setAttribute("arrivalAirport", invoiceFlight.getArrivalAirport());
                    request.setAttribute("dateTime", invoiceFlight.getDateTime());
                    request.setAttribute("airplanename", invoiceFlight.getAirplane().getName());
                }
            }
        }

        if (numberTicketsFlight > (invoiceFlight.getAvailablePlacesBusiness() + invoiceFlight.getAvailablePlacesEconom())) {
            request.setAttribute("notEnoughPlaces", encode(err.getString("notEnoughPlaces")));
            request.getRequestDispatcher(redirectBackString).forward(request, response);
        } else {
            Invoice invoice;
            Long invoiceIdSession = (Long) httpSession.getAttribute("invoiceId");
            if (invoiceIdSession == null) {
                invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, LocalDateTime.now());
                //TODO: заказ в базе тоже должен создаться
                invoice.setInvoiceId(1); //временно
                httpSession.setAttribute("invoiceId", invoice.getInvoiceId());

            } else {
                InvoiceService invoiceService = new InvoiceService();
                Optional<Invoice> invoiceOptional = invoiceService.get(invoiceIdSession.intValue());
                if (invoiceOptional.isPresent()) {
                    invoice = invoiceOptional.get();
                    httpSession.setAttribute("invoiceId", invoice.getInvoiceId());
                } else invoice = null;
            }

            List<Ticket> tickets = new ArrayList<>();
            if (numberTicketsFlight != 0) {
                System.out.println("number of tickets to buy: " + numberTicketsFlight);
                for (int i = 0; i < numberTicketsFlight; i++) {
                    int sittingPlace = StubUtils.randomSittingPlaceEconom(invoiceFlight.getFlightId());
                    tickets.add(new Ticket(invoice, invoiceFlight, "", "", sittingPlace,
                            false, false, invoiceFlight.getBaseCost()));
                    //Здесь нужно добавление tickets в базу и бронирование количества билетов
                    // А также отображение в header инфо о корзине
                }
            }
            request.getRequestDispatcher(redirectBackString).forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
