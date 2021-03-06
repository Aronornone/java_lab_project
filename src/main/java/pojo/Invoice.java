package pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Invoice {
    private long invoiceId;
    private User user;
    private InvoiceStatus invoiceStatus;
    private LocalDateTime timestamp;
    private List<Ticket> tickets;

    @java.beans.ConstructorProperties({"invoiceId", "user", "invoiceStatus", "timestamp", "tickets"})
    private Invoice(long invoiceId, User user, InvoiceStatus invoiceStatus, LocalDateTime timestamp, List<Ticket> tickets) {
        this.invoiceId = invoiceId;
        this.user = user;
        this.invoiceStatus = invoiceStatus;
        this.timestamp = timestamp;
        this.tickets = tickets;
    }

    public enum InvoiceStatus {
        CREATED,
        PAYED,
        CANCELLED
    }

    public static class InvoiceBuilder {
        private long nestedInvoiceId;
        private User nestedUser;
        private InvoiceStatus nestedInvoiceStatus;
        private LocalDateTime nestedTimestamp;
        private List<Ticket> nestedTickets;

        public InvoiceBuilder(User newUser) {
            this.nestedUser = newUser;
        }

        public InvoiceBuilder invoiceStatus(Invoice.InvoiceStatus newStatus) {
            this.nestedInvoiceStatus = newStatus;
            return this;
        }

        public InvoiceBuilder invoiceId(long newId) {
            this.nestedInvoiceId = newId;
            return this;
        }

        public InvoiceBuilder timestamp(LocalDateTime newTimestamp) {
            this.nestedTimestamp = newTimestamp;
            return this;
        }

        public InvoiceBuilder tickets(List<Ticket> newTickets) {
            this.nestedTickets = newTickets;
            return this;
        }

        public Invoice createInvoice() {
            return new Invoice(nestedInvoiceId, nestedUser, nestedInvoiceStatus, nestedTimestamp, nestedTickets);
        }
    }
}
