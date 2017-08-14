package pojo;

import lombok.Data;

@Data
public class Ticket {

    private long ticketId;
    private Invoice invoice;
    private Flight flight;
    private String passengerName;
    private String passport;
    private int sittingPlace;
    private boolean luggage;
    private boolean businessClass;
    private double price;

    @java.beans.ConstructorProperties({"ticketId", "invoice", "flight", "passengerName", "passport", "sittingPlace", "luggage", "businessClass", "price"})
    private Ticket(long ticketId, Invoice invoice, Flight flight, String passengerName, String passport, int sittingPlace, boolean luggage, boolean businessClass, double price) {
        this.ticketId = ticketId;
        this.invoice = invoice;
        this.flight = flight;
        this.passengerName = passengerName;
        this.passport = passport;
        this.sittingPlace = sittingPlace;
        this.luggage = luggage;
        this.businessClass = businessClass;
        this.price = price;
    }

    public static class TicketBuilder {
        private long nestedTicketId;
        private Invoice nestedInvoice;
        private Flight nestedFlight;
        private String nestedPassengerName;
        private String nestedPassport;
        private int nestedSittingPlace;
        private boolean nestedLuggage;
        private boolean nestedBusinessClass;
        private double nestedPrice;

        public TicketBuilder(Invoice invoice,Flight flight) {
            this.nestedFlight = flight;
            this.nestedInvoice = invoice;
        }

        public TicketBuilder ticketId(long ticketId) {
            this.nestedTicketId = ticketId;
            return this;
        }
        public TicketBuilder passengerName(String passengerName) {
            this.nestedPassengerName = passengerName;
            return this;
        }

        public TicketBuilder passport(String passport) {
            this.nestedPassport = passport;
            return this;
        }
        public TicketBuilder sittingPlace(int sittingPlace) {
            this.nestedSittingPlace = sittingPlace;
            return this;
        }

        public TicketBuilder luggage(boolean luggage) {
            this.nestedLuggage = luggage;
            return this;
        }

        public TicketBuilder business(boolean business) {
            this.nestedBusinessClass = business;
            return this;
        }

        public TicketBuilder price(double price) {
            this.nestedPrice = price;
            return this;
        }
        public Ticket createTicket() {
            return new Ticket(nestedTicketId,nestedInvoice,nestedFlight,nestedPassengerName,nestedPassport,
                    nestedSittingPlace,nestedLuggage,nestedBusinessClass,nestedPrice);
        }
    }

}
