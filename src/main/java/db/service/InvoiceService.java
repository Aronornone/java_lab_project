package db.service;

import db.dao.InvoiceDAO;
import pojo.Invoice;

import java.util.List;
import java.util.Optional;

public class InvoiceService implements InvoiceDAO {
    @Override
    public long create(Invoice invoice) {
        return 0;
    }

    @Override
    public Optional<Invoice> get(int id) {
        return null;
    }

    @Override
    public void update(Invoice invoice) {

    }

    @Override
    public void remove(Invoice invoice) {

    }

    @Override
    public List<Invoice> getAll() {
        return null;
    }
}
