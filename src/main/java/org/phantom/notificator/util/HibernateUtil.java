package org.phantom.notificator.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.phantom.notificator.Constants;

import java.io.File;

/**
 * Created by Master Victor on 03/07/2015.
 * This class is used to create only one session factory per application and to handle the
 * Hibernate Session.
 *
 * Use MockedHibernateUtil
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.getProperties().setProperty("hibernate.connection.username", PropertiesRetrievalUtil.getProperty("db"));
            configuration.getProperties().setProperty("hibernate.connection.password", PropertiesRetrievalUtil.getProperty("db_password"));
            configuration.getProperties().setProperty("hibernate.connection.url", Constants.URL_PREFIX + getDbFilePath() + Constants.URL_SUFFIX);
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
        File file = new File(Constants.DATABASE_DIRECTORY + Constants.PATH_SEPARATOR + Constants.PROD_DB_FILE_NAME);
        String absolutePath;

        if (file.exists()) {
            absolutePath = file.getAbsolutePath();
            // remove the the suffix '.mv.db'
            return absolutePath.substring(0, absolutePath.indexOf(".mv.db"));
        } else {
            throw new RuntimeException("File " + Constants.PROD_DB_FILE_NAME + " cannot be found. Check the file exists!");
        }
    }

}
