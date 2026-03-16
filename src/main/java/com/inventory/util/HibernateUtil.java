package com.inventory.util;

import com.inventory.entity.Product;
import com.inventory.entity.ProductAuto;
import com.inventory.entity.ProductIdentity;
import com.inventory.entity.ProductSequence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(ProductAuto.class)
                .addAnnotatedClass(ProductIdentity.class)
                .addAnnotatedClass(ProductSequence.class)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null) {
            SESSION_FACTORY.close();
        }
    }
}
