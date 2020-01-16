package com.softpark.util;


import com.softpark.model.Categoria;
import com.softpark.model.Entrada;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSession {

    private static SessionFactory SESSION_FACTORY;

    static {
        try {
            SESSION_FACTORY =
                    new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {

        return SESSION_FACTORY.openSession();
    }

}
