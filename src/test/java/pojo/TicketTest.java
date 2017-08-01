package pojo;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;

public class TicketTest {
    @Test
    public void testTicket() {
        User user = new User(1,"Name","@mail.ru","123452Afr3", LocalDateTime.now());
        Invoice invoice = new Invoice(1,user, Invoice.InvoiceStatus.CREATED,LocalDateTime.now());
        Airplane airplane = new Airplane(1,"Airbus",202,23);
        Airport SPB = new Airport(1,"SPB","Saint-Petersburg","",64.4,23.32);
        Airport MSK = new Airport(2,"MSK","Moscow","",32.4,23.53);
        Flight flight = new Flight(1, airplane, "12-D", SPB, MSK, 10052.30, 202, 23, LocalDateTime.of(2017, 07, 10, 23, 59));
        Ticket ticket1 = new Ticket(1,invoice,flight,"Passenger 1","321103422",10,true,false,1005.30);
        Ticket ticket2 = new Ticket(2,invoice,flight,"Passenger 2","321103422",11,true,false,1005.30);

        assertFalse(ticket1.equals(ticket2));
        System.out.println(ticket1);
    }
}