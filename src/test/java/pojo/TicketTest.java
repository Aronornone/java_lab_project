package pojo;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;

public class TicketTest {
    @Test
    public void testTicket() {
        User user = new User(1, "Name", "@mail.ru", "123452Afr3", LocalDateTime.now());
        Invoice invoice = new Invoice.InvoiceBuilder(user).invoiceId(1).invoiceStatus(Invoice.InvoiceStatus.CREATED)
        .timestamp(LocalDateTime.now()).createInvoice();
        Airplane airplane = new Airplane(1, "Airbus", 202, 23);
        Airport SPB = new Airport(1, "SPB", "Saint-Petersburg", "", 64.4, 23.32);
        Airport MSK = new Airport(2, "MSK", "Moscow", "", 32.4, 23.53);
        Flight flight = new Flight.FlightBuilder(1, "12-D").airplane(airplane).departureAirport(SPB)
                .arrivalAirport(MSK).baseCost(10052.30).availableEconom(202).availableBusiness(23)
                .dateTime(LocalDateTime.of(2017, 7, 10, 23, 59)).createFlight();
        Ticket ticket1 = new Ticket.TicketBuilder(invoice, flight).ticketId(1).passengerName("Passenger 1")
                .passport("321103422").sittingPlace(10).business(true).luggage(false).price(1005.30)
                .createTicket();
        Ticket ticket2 = new Ticket.TicketBuilder(invoice, flight).ticketId(2).passengerName("Passenger 2")
                .passport("321103422").sittingPlace(11).business(true).luggage(false).price(1005.30)
                .createTicket();

        assertFalse(ticket1.equals(ticket2));
        System.out.println(ticket1);
    }
}