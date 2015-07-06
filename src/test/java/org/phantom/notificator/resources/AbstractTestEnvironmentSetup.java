package org.phantom.notificator.resources;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.schedulers.ItpNotificator;
import org.phantom.notificator.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by Master Victor on 06/07/2015.
 */
public abstract class AbstractTestEnvironmentSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestEnvironmentSetup.class);

    public static ItpNotificator spyItpNotificator;
    public static Days daysToNotifyInAdvance;

    public static CarOwner victor;
    public static CarOwner mihnea;
    public static CarOwner daniel;
    public static CarOwner bunu;

    public static Car victorsCarWithoutUpcomingItp;
    public static Car mihneasCarWithUpcomingItp;
    public static Car mihneasOtherCarWithUpcomingItp;
    public static Car bunusCar;
    public static Car danielsCar;

    public static SessionFactory sessionFactory;

    public static List<CarOwner> carOwners;
    public static List<Car> cars;

    public static void setUpCarsAndOwnersWithoutPersistingToDb() {
        LOGGER.info("---->>>> SETTING UP TEST ENVIRONMENT (No DB Setup)<<<<----");
        daysToNotifyInAdvance = Days.FIVE;
        LocalDate currentDateForTest = new LocalDate(2015, 6, 29);

        setUpCarOwners();
        LOGGER.info("---->>>> Car Owners Configured!! <<<<----");
        setUpCars(currentDateForTest);
        LOGGER.info("---->>>> Cars Configured!! <<<<----");

        // Create a mock of ItpNotificator to return a predefined date.
        spyItpNotificator = spy(new ItpNotificator(cars, daysToNotifyInAdvance));
        doReturn(currentDateForTest).when(spyItpNotificator).getCurrentDate();
        LOGGER.info("---->>>> Itp Notificator Mocked!! <<<<----");

        LOGGER.info("---->>>> FINISHED SETTING UP TEST ENVIRONMENT (No DB Setup)<<<<----");
    }

    private static void setUpCars(LocalDate currentDateForTest) {
        // Set up cars with upcoming Itp Expiry Date in MORE than daysUntilItpExpires
        victorsCarWithoutUpcomingItp = new Car("B 725 MIH", currentDateForTest.plus(daysToNotifyInAdvance).plus(Days.THREE), victor);
        victor.addCar(victorsCarWithoutUpcomingItp);

        // Set up cars with upcoming Itp Expiry Date in EXACTLY daysUntilItpExpires
        mihneasCarWithUpcomingItp = new Car("B 23 BUB", currentDateForTest.plus(daysToNotifyInAdvance), mihnea);
        mihneasOtherCarWithUpcomingItp = new Car("B 45 MIC", currentDateForTest.plus(daysToNotifyInAdvance), mihnea);
        mihnea.addCar(mihneasCarWithUpcomingItp);
        mihnea.addCar(mihneasOtherCarWithUpcomingItp);
        bunusCar = new Car("AG 88 VEE", currentDateForTest.plus(daysToNotifyInAdvance), bunu);
        bunu.addCar(bunusCar);

        // Set up cars with upcoming Itp Expiry Date in LESS than daysUntilItpExpires
        danielsCar = new Car("B 33 DPT", currentDateForTest.plus(Days.ONE), daniel);
        daniel.addCar(danielsCar);

        cars = Arrays.asList(victorsCarWithoutUpcomingItp,
                mihneasCarWithUpcomingItp,
                mihneasOtherCarWithUpcomingItp,
                bunusCar,
                danielsCar);
    }

    private static void setUpCarOwners() {
        victor = new CarOwner("Victor", "Patentasu", "1111111111");
        mihnea = new CarOwner("Mihnea", "Patentasu", "2222222222");
        bunu = new CarOwner("Bunu", "Patentasu", "3333333333");
        daniel = new CarOwner("Daniel", "Patentasu", "4444444444");

        carOwners = Arrays.asList(victor, mihnea, daniel, bunu);
    }

    protected static void setUpCarsAndOwnersInDb() {
        LOGGER.info(" ---->>>> DB SETUP: Persisting Cars and Owners to DB <<<<----");

        sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction txt = session.beginTransaction();
        try {

            for (CarOwner carOwner : carOwners) {
                session.save(carOwner);
            }
            txt.commit();
            LOGGER.info("---->>>> FINISHED DB SETUP: All owners and cars persisted to DB <<<<----");

        } catch (Exception e) {

            LOGGER.error("---->>>> Error occurred when adding clients !! Rolling back. <<<<----");
            e.printStackTrace();
            txt.rollback();

        }
    }

    protected static void removeCarsAndOwnersFromDb() {
        if (sessionFactory == null) {
            throw new RuntimeException("SessionFactory is null!! Have you called setUpCarsAndOwnersInDb?");
        }

        LOGGER.info(" ---->>>> Removing Cars and Owners from DB <<<<----");
        Session session = sessionFactory.getCurrentSession();
        Transaction txt = session.beginTransaction();
        try {

            for (CarOwner carOwner : carOwners) {
                session.delete(carOwner);
            }
            txt.commit();
            LOGGER.info("---->>>> All cars and owners removed from DB! <<<<----");

        } catch (Exception e) {

            LOGGER.error("---->>>> Error occurred while removing clients !! Rolling back. <<<<----");
            e.printStackTrace();
            txt.rollback();
            session.close();
        }
    }
}
