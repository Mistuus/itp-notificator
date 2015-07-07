package org.phantom.notificator.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.net.URL;

/**
 * Created by Master Victor on 07/07/2015.
 */
public class MockedHibernateUtil {

    // todo: Determine the best way of using different DB configuration for PROD vs DEV
    // 1) Spring
    // 2) persistence.xml
    // 3) Maven profiles
    // 4) DBUTILS
    // 5) Instance of HibernateUtils

    private static final String TESTING_DB_FILE_NAME = "vectorDBForTesting.mv.db";
    private static final String URL_PREFIX = "jdbc:h2:file:";
    private static final String URL_SUFFIX = ";IFEXISTS=TRUE;";
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
        URL resource = HibernateUtil.class.getClassLoader().getResource(TESTING_DB_FILE_NAME);
        String absolutePath;

        if (resource != null) {
            absolutePath = resource.getPath();
            // remove the first character, '\' and the suffix '.mv.db'
            return absolutePath.substring(1, absolutePath.indexOf(".mv.db"));
        } else {
            throw new RuntimeException("File " + TESTING_DB_FILE_NAME + " cannot be found. Check the file exists!");
        }
    }
}
