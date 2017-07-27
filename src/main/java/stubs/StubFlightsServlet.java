package stubs;

import pojo.Airplane;
import pojo.Flight;
import pojo.Invoice;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/flights"})
public class StubFlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User(1,"UserName","@mail.ru","123452Afr3", LocalDateTime.now());
        Invoice invoice = new Invoice(1,user, Invoice.InvoiceStatus.CREATED,2,LocalDateTime.now());
        Airplane airplane = new Airplane(1,"Airbus 320",202,23);
        Flight flight1 = new Flight(1,airplane,"12-D","SPB","MSK",10052.30,202,23, LocalDateTime.of(2017,10,10,23,59));
        Flight flight2 = new Flight(2,airplane,"15-3","SPB","MSK",11002.30,202,23, LocalDateTime.of(2017,10,11,12,48));

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);

        request.setAttribute("flights",flights);
        request.setAttribute("username",user.getName());

        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request,response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
