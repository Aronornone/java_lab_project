package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Invoice {
    private long invoiceId;
    private User user;
    private InvoiceStatus invoiceStatus;
    private int numberTickets;
    private LocalDateTime timestamp;

    public Invoice(User user, InvoiceStatus invoiceStatus, int numberTickets, LocalDateTime timestamp) {
        this.user = user;
        this.invoiceStatus = invoiceStatus;
        this.numberTickets = numberTickets;
        this.timestamp = timestamp;
    }

    public enum InvoiceStatus {
        CREATED,
        PAYED,
        CANCELLED
    }
}
