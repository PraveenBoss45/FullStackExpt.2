package com.inventory.dao;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProductDao {

    public Long save(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Long id = (Long) session.save(product);
            tx.commit();
            return id;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public Product findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Product.class, id);
        }
    }

    public boolean updatePriceOrQuantity(Long id, Double newPrice, Integer newQuantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product == null) {
                return false;
            }
            if (newPrice != null) {
                product.setPrice(newPrice);
            }
            if (newQuantity != null) {
                product.setQuantity(newQuantity);
            }
            session.update(product);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public boolean deleteById(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product == null) {
                return false;
            }
            session.delete(product);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
    }
}
