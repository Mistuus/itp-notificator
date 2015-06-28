package org.phantom.notificator.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Master Victor on 27/06/2015.
 */
public class HibernateAndH2Test {
    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
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
        Client mihClient = new Client("Mihnea", "Patentasu", "077777777");
        session.save(new Car("B 725 MIH", new LocalDate(2014, 10, 5), mihClient));
        session.getTransaction().commit();
        session.close();

        // now lets pull events from the database and list them
        session = sessionFactory.openSession();
        session.beginTransaction();
        List<Car> result = session.createQuery("from Car").list();
        for (Car car : result) {
            System.out.println("Client: " + car.getClient().getFirstName() + " " + car.getClient().getLastName() + ", masina: " + car.getCarRegistrationNumber());
        }
        session.getTransaction().commit();
        session.close();
    }
}