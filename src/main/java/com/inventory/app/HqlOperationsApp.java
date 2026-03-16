package com.inventory.app;

import com.inventory.dao.ProductDao;
import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HqlOperationsApp {

    public static void main(String[] args) {
        ProductDao productDao = new ProductDao();

        resetProductsTable();
        seedProducts(productDao);

        printHeader("3a) Products sorted by price (ASC)");
        printProducts(productDao.findAllOrderByPrice(true));

        printHeader("3b) Products sorted by price (DESC)");
        printProducts(productDao.findAllOrderByPrice(false));

        printHeader("4) Products sorted by quantity (highest first)");
        printProducts(productDao.findAllOrderByQuantityDesc());

        printHeader("5a) Pagination - first 3 products");
        printProducts(productDao.findWithPagination(0, 3));

        printHeader("5b) Pagination - next 3 products");
        printProducts(productDao.findWithPagination(3, 3));

        printHeader("6a) Aggregate - total product count");
        System.out.println("Total products: " + productDao.countAllProducts());

        printHeader("6b) Aggregate - count where quantity > 0");
        System.out.println("Products with quantity > 0: " + productDao.countProductsWithQuantityGreaterThan(0));

        printHeader("6c) Aggregate - count grouped by description");
        printGroupedCount(productDao.countProductsGroupedByDescription());

        printHeader("6d) Aggregate - minimum and maximum price");
        Object[] minMax = productDao.findMinAndMaxPrice();
        System.out.println("Min price: " + minMax[0] + ", Max price: " + minMax[1]);

        printHeader("7) GROUP BY description");
        printGroupedStats(productDao.groupByDescription());

        printHeader("8) WHERE price range (1000 to 9000)");
        printProducts(productDao.findByPriceRange(1000, 9000));

        printHeader("9a) LIKE - names starting with 'M'");
        printProducts(productDao.findByNameStartsWith("M"));

        printHeader("9b) LIKE - names ending with 'r'");
        printProducts(productDao.findByNameEndsWith("r"));

        printHeader("9c) LIKE - names containing 'ea'");
        printProducts(productDao.findByNameContains("ea"));

        printHeader("9d) LIKE/Function - names with exact length 5");
        printProducts(productDao.findByNameLength(5));

        HibernateUtil.shutdown();
    }

    private static void resetProductsTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Product").executeUpdate();
            tx.commit();
        }
    }

    private static void seedProducts(ProductDao productDao) {
        productDao.save(new Product("Mouse", "Computer Accessory", 850.0, 50));
        productDao.save(new Product("Monitor", "Display Unit", 12500.0, 12));
        productDao.save(new Product("Keyboard", "Computer Accessory", 2200.0, 28));
        productDao.save(new Product("Speaker", "Audio Device", 3200.0, 16));
        productDao.save(new Product("Router", "Networking", 2800.0, 9));
        productDao.save(new Product("Printer", "Office Device", 9100.0, 5));
        productDao.save(new Product("Scanner", "Office Device", 7600.0, 7));
        productDao.save(new Product("Webcam", "Computer Accessory", 1800.0, 0));
    }

    private static void printProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void printGroupedCount(List<Object[]> rows) {
        for (Object[] row : rows) {
            System.out.println("Description: " + row[0] + ", Count: " + row[1]);
        }
    }

    private static void printGroupedStats(List<Object[]> rows) {
        for (Object[] row : rows) {
            System.out.println("Description: " + row[0] + ", Count: " + row[1] + ", Avg Price: " + row[2]);
        }
    }

    private static void printHeader(String title) {
        System.out.println("\n==================================================");
        System.out.println(title);
        System.out.println("==================================================");
    }
}
