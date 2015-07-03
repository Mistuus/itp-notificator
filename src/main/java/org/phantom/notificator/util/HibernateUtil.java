package org.phantom.notificator.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.net.URL;

/**
 * Created by Master Victor on 03/07/2015.
 * This class is used to create only one session factory per application and to hangdle the
 * Hibernate Session.
 */
public class HibernateUtil {

    private static final String URL_PREFIX = "jdbc:h2:file:";
    private static final String URL_SUFFIX = ";DB_CLOSE_DELAY=10;IFEXISTS=TRUE;";
    private static final String DB_FILE_NAME = "vectorDB.mv.db";
    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.getProperties().setProperty("hibernate.connection.url", URL_PREFIX + getDbFilePath() + URL_SUFFIX);
            configuration.configure();

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static String getDbFilePath() {
        URL resource = HibernateUtil.class.getClassLoader().getResource(DB_FILE_NAME);
        String absolutePath;

        if (resource != null) {
            absolutePath = resource.getPath();
            // remove the first character, '\' and the suffix '.mv.db'
            return absolutePath.substring(1, absolutePath.indexOf(".mv.db"));
        } else {
            throw new RuntimeException("File " + DB_FILE_NAME + " cannot be found. Check the file exists!");
        }
    }

}
