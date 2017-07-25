package pojo;

public class Ticket {
    private long ticketId;
    private Invoice Invoice;
    private Flight Flight;
    private String passengerName;
    private String passport;
    private byte sittingPlace;
    private boolean luggage;
    private boolean businessClass;

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public pojo.Invoice getInvoice() {
        return Invoice;
    }

    public void setInvoice(pojo.Invoice invoice) {
        Invoice = invoice;
    }

    public Flight getFlight() {
        return Flight;
    }

    public void setFlight(Flight flight) {
        Flight = flight;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public byte getSittingPlace() {
        return sittingPlace;
    }

    public void setSittingPlace(byte sittingPlace) {
        this.sittingPlace = sittingPlace;
    }

    public boolean isLuggage() {
        return luggage;
    }

    public void setLuggage(boolean luggage) {
        this.luggage = luggage;
    }

    public boolean isBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(boolean businessClass) {
        this.businessClass = businessClass;
    }
}
