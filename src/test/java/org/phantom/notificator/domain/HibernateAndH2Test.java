package org.phantom.notificator.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.phantom.notificator.util.HibernateUtil;

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

    // todo: Test saving
    // todo: Test updating
    // todo: test deleting one car out of 2
    // todo: test deleting both cars
    // todo: test deleting the car owner of two cars
    // todo: test validation of object
    @Before
    public void setUp() throws Exception {
        sessionFactory = HibernateUtil.getSessionFactory();
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
        CarOwner mihCarOwner = createCarOwner("Mihnea", "Patentasu", "077777777");
        Car mihsCar = createCar(mihCarOwner, "B 725 MIH");
        session.save(mihsCar);
        session.getTransaction().commit();
        session.close();

        // now lets pull events from the database and list them
        session = sessionFactory.openSession();
        session.beginTransaction();
        List<Car> result = session.createQuery("from Car").list();
        for (Car car : result) {
            System.out.println("CarOwner: " + car.getCarOwner().getFirstName() + " " + car.getCarOwner().getLastName() + ", Msina: " + car.getCarRegistrationNumber());
        }
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testCarToCarOwnerRelationship() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CarOwner mih = createCarOwner("Mihnea", "Patentasu", "0123456789");
        session.save(mih);
        Car mihsCar = createCar(mih, "B 725 MIH");
        Car mihsSecondCar = createCar(mih, "B 123 ZDA");
        mih.addCar(mihsCar);
        mih.addCar(mihsSecondCar);
        session.getTransaction().commit();
        session.close();

        Assert.assertEquals(1, mih.getCars().size());
    }

    private Car createCar(CarOwner mihCarOwner, String carRegistrationNumber) {
        return new Car(carRegistrationNumber, new LocalDate(2014, 10, 5), mihCarOwner);
    }

    private CarOwner createCarOwner(String firstName, String lastName, String telephoneNumber) {
        return new CarOwner(firstName, lastName, telephoneNumber);
    }
}