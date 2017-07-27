package stubs;

import pojo.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/bucket"})
public class StubBucketServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User(1,"Name","@mail.ru","123452Afr3", LocalDateTime.now());
        Invoice invoice = new Invoice(1,user, Invoice.InvoiceStatus.CREATED,2,LocalDateTime.now());
        Airplane airplane = new Airplane(1,"Airbus 320",202,23);
        Flight flight = new Flight(1,airplane,"12-D","SPB","MSK",10052.30,202,23, LocalDateTime.of(2017,10,10,23,59));
        Ticket ticket1 = new Ticket(1,invoice,flight,"Passenger 1","321103422",10,true,false,10052.30);
        Ticket ticket2 = new Ticket(2,invoice,flight,"Passenger 2","321103422",11,true,false,10052.30);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);

        double sumTotal=0;
        for(Ticket ticket: tickets) {
            sumTotal = sumTotal+ticket.getPrice();
        }

        request.setAttribute("tickets",tickets);
        request.setAttribute("totalSum",sumTotal);

        request.setAttribute("flightNumber",flight.getFlightNumber());
        request.setAttribute("departureAirport",flight.getDepartureAirport());
        request.setAttribute("arrivalAirport",flight.getArrivalAirport());
        request.setAttribute("dateTime",flight.getDateTime());
        request.setAttribute("airplanename",flight.getAirplane().getName());


        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request,response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
