package com.inventory.dao;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import java.util.List;

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

    public List<Product> findAllOrderByPrice(boolean ascending) {
        String order = ascending ? "asc" : "desc";
        String hql = "from Product p order by p.price " + order;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Product.class).list();
        }
    }

    public List<Product> findAllOrderByQuantityDesc() {
        String hql = "from Product p order by p.quantity desc";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Product.class).list();
        }
    }

    public List<Product> findWithPagination(int offset, int limit) {
        String hql = "from Product p order by p.id asc";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Product.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
        }
    }

    public long countAllProducts() {
        String hql = "select count(p.id) from Product p";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Long.class).uniqueResult();
        }
    }

    public long countProductsWithQuantityGreaterThan(int quantity) {
        String hql = "select count(p.id) from Product p where p.quantity > :qty";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Long.class)
                    .setParameter("qty", quantity)
                    .uniqueResult();
        }
    }

    public List<Object[]> countProductsGroupedByDescription() {
        String hql = "select p.description, count(p.id) from Product p group by p.description";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Object[].class).list();
        }
    }

    public Object[] findMinAndMaxPrice() {
        String hql = "select min(p.price), max(p.price) from Product p";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Object[].class).uniqueResult();
        }
    }

    public List<Object[]> groupByDescription() {
        String hql = "select p.description, count(p.id), avg(p.price) from Product p group by p.description";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Object[].class).list();
        }
    }

    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        String hql = "from Product p where p.price between :minPrice and :maxPrice order by p.price asc";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Product.class)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .list();
        }
    }

    public List<Product> findByNameStartsWith(String prefix) {
        return findByNameLike(prefix + "%");
    }

    public List<Product> findByNameEndsWith(String suffix) {
        return findByNameLike("%" + suffix);
    }

    public List<Product> findByNameContains(String pattern) {
        return findByNameLike("%" + pattern + "%");
    }

    public List<Product> findByNameLength(int exactLength) {
        String hql = "from Product p where length(p.name) = :nameLength";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Product.class)
                    .setParameter("nameLength", exactLength)
                    .list();
        }
    }

    private List<Product> findByNameLike(String likePattern) {
        String hql = "from Product p where p.name like :pattern order by p.name asc";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("pattern", likePattern);
            return query.list();
        }
    }
}
