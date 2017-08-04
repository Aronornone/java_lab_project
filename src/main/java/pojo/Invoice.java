package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class Invoice {
    private long invoiceId;
    private User user;
    private InvoiceStatus invoiceStatus;
    private LocalDateTime timestamp;
    private List<Ticket> tickets;

    public Invoice(User user, InvoiceStatus invoiceStatus, LocalDateTime timestamp) {
        this.user = user;
        this.invoiceStatus = invoiceStatus;
        this.timestamp = timestamp;
    }

    public Invoice(long invoiceId, User user, InvoiceStatus invoiceStatus, LocalDateTime timestamp) {
        this.invoiceId = invoiceId;
        this.user = user;
        this.invoiceStatus = invoiceStatus;
        this.timestamp = timestamp;
    }

    public enum InvoiceStatus {
        CREATED,
        PAYED,
        CANCELLED
    }
}
