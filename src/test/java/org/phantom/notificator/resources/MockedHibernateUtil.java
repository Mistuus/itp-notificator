package org.phantom.notificator.resources;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.phantom.notificator.Constants;
import org.phantom.notificator.util.PropertiesRetrievalUtil;

import java.io.File;

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

    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            System.out.println(Constants.URL_PREFIX + getDbFilePath() + Constants.URL_SUFFIX);
            configuration.getProperties().setProperty("hibernate.connection.username", PropertiesRetrievalUtil.getProperty("test_db"));
            configuration.getProperties().setProperty("hibernate.connection.password", PropertiesRetrievalUtil.getProperty("test_db_password"));
            configuration.getProperties().setProperty("hibernate.connection.url", Constants.URL_PREFIX + getDbFilePath() + Constants.URL_SUFFIX);
            configuration.configure();
            // The following property overrides the existing hibernate.hbm2ddl.auto=update to hibernate.hbm2ddl.auto=create.
            // Consequently, this will drop and recreate the test DB after each use.
            configuration.getProperties().setProperty("hibernate.hbm2ddl.auto", "create");

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
        File file = new File(Constants.DATABASE_DIRECTORY + Constants.PATH_SEPARATOR + Constants.TESTING_DB_FILE_NAME);
        String absolutePath;

        if (file.exists()) {
            absolutePath = file.getAbsolutePath();
            System.out.println("---> Path is " + absolutePath);
            // remove the first character, '\' and the suffix '.mv.db'
            return absolutePath.substring(0, absolutePath.indexOf(".mv.db"));
        } else {
            throw new RuntimeException("File " + Constants.TESTING_DB_FILE_NAME + " cannot be found. Check the file exists!");
        }
    }
}
