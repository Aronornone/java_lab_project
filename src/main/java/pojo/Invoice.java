package pojo;

import java.time.LocalDateTime;

public class Invoice {
    private long invoiceId;
    private User user;
    private InvoiceStatus invoiceStatus;
    private short numberTickets;
    private LocalDateTime timestamp;

    enum InvoiceStatus {
        CREATED,
        PAYED
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public short getNumberTickets() {
        return numberTickets;
    }

    public void setNumberTickets(short numberTickets) {
        this.numberTickets = numberTickets;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
