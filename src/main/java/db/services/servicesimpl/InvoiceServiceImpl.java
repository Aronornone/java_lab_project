package db.services.servicesimpl;

import db.dao.interfaces.InvoiceDAO;
import db.services.interfaces.InvoiceService;
import pojo.Invoice;

import java.util.List;
import java.util.Optional;

public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceDAO dao = new db.dao.daoimpl.InvoiceServiceImpl();

    @Override
    public void add(Invoice invoice) {
        dao.add(invoice);
    }

    @Override
    public Optional<Invoice> get(long id) {
        return dao.get(id);
    }

    @Override
    public Optional<Invoice> getInvoiceByUser(long userId, Invoice.InvoiceStatus status) {
        return dao.getInvoiceByUser(userId, status);
    }

    @Override
    public void update(Invoice invoice) {
        dao.update(invoice);
    }

    @Override
    public void delete(Invoice invoice) {
        dao.delete(invoice);
    }

    @Override
    public List<Invoice> getAll() {
        return dao.getAll();
    }

    @Override
    public List<Invoice> getAllInvoicesByUserAndStatus(long userId, Invoice.InvoiceStatus status) {
        return dao.getAllInvoicesByUserAndStatus(userId, status);
    }
}
