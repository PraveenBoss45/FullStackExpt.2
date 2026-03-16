package com.inventory.app;

import com.inventory.dao.ProductDao;
import com.inventory.entity.Product;
import com.inventory.entity.ProductAuto;
import com.inventory.entity.ProductIdentity;
import com.inventory.entity.ProductSequence;
import com.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class InventoryApp {

    public static void main(String[] args) {
        ProductDao productDao = new ProductDao();

        // 1) Insert multiple products
        Long id1 = productDao.save(new Product("Keyboard", "Mechanical keyboard", 2499.0, 25));
        Long id2 = productDao.save(new Product("Mouse", "Wireless optical mouse", 899.0, 50));
        Long id3 = productDao.save(new Product("Monitor", "24-inch IPS display", 11999.0, 10));

        System.out.println("Inserted product IDs: " + id1 + ", " + id2 + ", " + id3);

        // 2) Retrieve product by id
        Product fetched = productDao.findById(id2);
        System.out.println("Fetched by ID " + id2 + ": " + fetched);

        // 3) Update price and quantity
        boolean updated = productDao.updatePriceOrQuantity(id3, 10999.0, 8);
        System.out.println("Updated product " + id3 + ": " + updated);
        System.out.println("After update: " + productDao.findById(id3));

        // 4) Delete discontinued product
        boolean deleted = productDao.deleteById(id1);
        System.out.println("Deleted product " + id1 + ": " + deleted);

        // 5) Show ID generation differences
        demoIdStrategies();

        HibernateUtil.shutdown();
    }

    private static void demoIdStrategies() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            ProductAuto auto = new ProductAuto("Auto-ID Item", 100.0);
            ProductIdentity identity = new ProductIdentity("Identity-ID Item", 200.0);
            ProductSequence sequence = new ProductSequence("Sequence-ID Item", 300.0);

            session.save(auto);
            session.save(identity);
            session.save(sequence);

            tx.commit();

            System.out.println("ID strategy demo:");
            System.out.println("AUTO ID      -> " + auto.getId());
            System.out.println("IDENTITY ID  -> " + identity.getId());
            System.out.println("SEQUENCE ID  -> " + sequence.getId());
        }
    }
}
