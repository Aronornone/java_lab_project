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

    enum InvoiceStatus {
        CREATED,
        PAYED,
        CANCELLED
    }
}
