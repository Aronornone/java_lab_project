package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {

    private long ticketId;
    private Invoice Invoice;
    private Flight Flight;
    private String passengerName;
    private String passport;
    private int sittingPlace;
    private boolean luggage;
    private boolean businessClass;
    private double price;
}
