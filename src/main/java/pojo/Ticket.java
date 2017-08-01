package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public Ticket(Invoice invoice, Flight flight, String passengerName,
                  String passport, int sittingPlace, boolean luggage, boolean businessClass, double price) {
        this.invoice = invoice;
        this.flight = flight;
        this.passengerName = passengerName;
        this.passport = passport;
        this.sittingPlace = sittingPlace;
        this.luggage = luggage;
        this.businessClass = businessClass;
        this.price = price;
    }

}
