package org.phantom.notificator.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

/**
 * Created by Master Victor on 27/06/2015.
 */
@SuppressWarnings("unchecked")
public class HibernateAndH2Test {
    public static final String URL_PREFIX = "jdbc:h2:file:";
    public static final String URL_SUFFIX = ";DB_CLOSE_DELAY=10;IFEXISTS=TRUE;";
    public static final String DB_FILE_NAME = "vectorDB.mv.db";
    private static SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        URL resource = getClass().getClassLoader().getResource(DB_FILE_NAME);
        String absolutePath;
        if (resource != null) {
            absolutePath = resource.getPath();
            // remove the first character, '\' and the suffix '.mv.db'
        } else {
            throw new RuntimeException("File " + DB_FILE_NAME + " cannot be found. Check the file exists!");
        }
        String dbFilePath = absolutePath.substring(1, absolutePath.indexOf(".mv.db"));

        Configuration configuration = new Configuration();
        configuration.getProperties().setProperty("hibernate.connection.url", URL_PREFIX + dbFilePath + URL_SUFFIX);
        configuration.configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @After
    public void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testInsertAndRetrieve() {
        // create a couple of events...
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CarOwner mihCarOwner = new CarOwner("Mihnea", "Patentasu", "077777777");
        session.save(new Car("B 725 MIH", new LocalDate(2014, 10, 5), mihCarOwner));
        session.getTransaction().commit();
        session.close();

        // now lets pull events from the database and list them
        session = sessionFactory.openSession();
        session.beginTransaction();
        List<Car> result = session.createQuery("from Car").list();
        for (Car car : result) {
            System.out.println("CarOwner: " + car.getCarOwner().getFirstName() + " " + car.getCarOwner().getLastName() + ", masina: " + car.getCarRegistrationNumber());
        }
        session.getTransaction().commit();
        session.close();
    }
}