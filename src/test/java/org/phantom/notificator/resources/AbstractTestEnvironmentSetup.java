package org.phantom.notificator.resources;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.schedulers.DailyScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by Victor on 06/07/2015.
 */
public abstract class AbstractTestEnvironmentSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestEnvironmentSetup.class);

    public static DailyScheduler spyDailyScheduler;
    public static Days daysToNotifyInAdvance;

    public static CarOwner victor;
    public static CarOwner mihnea;
    public static CarOwner daniel;
    public static CarOwner bunu;

    public static Car victorsCarWithoutUpcomingItp;
    public static Car mihneasCarWithUpcomingItp;
    public static Car mihneasCarWithUpcomingTahograf;
    public static Car bunusCarWithUpcomingItp;
    public static Car danielsCar;

    public static List<CarOwner> carOwners;
    public static List<Car> expectedCars;
    public static LocalDate currentDateForTest;

    public static void setUpCarsAndOwnersWithoutPersistingToDb() {
        LOGGER.info("---->>>> SETTING UP TEST ENVIRONMENT (No DB Setup)<<<<----");
        daysToNotifyInAdvance = Days.FIVE;
        currentDateForTest = new LocalDate(2015, 6, 29);

        setUpCarOwners();
        LOGGER.info("---->>>> Car Owners Configured!! <<<<----");
        setUpCars(currentDateForTest);
        LOGGER.info("---->>>> Cars Configured!! <<<<----");

        // Create a mock of DailyScheduler to return a predefined date.
        spyDailyScheduler = spy(new DailyScheduler(expectedCars, daysToNotifyInAdvance));
        doReturn(currentDateForTest).when(spyDailyScheduler).getCurrentDate();
        LOGGER.info("---->>>> Itp Notificator Mocked!! <<<<----");

        LOGGER.info("---->>>> FINISHED SETTING UP TEST ENVIRONMENT (No DB Setup)<<<<----");
    }

    private static void setUpCars(LocalDate currentDateForTest) {
        // Set up cars with upcoming Itp Expiry Date in MORE than daysUntilItpExpires
        victorsCarWithoutUpcomingItp = new Car("B 725 MIH", currentDateForTest.plus(daysToNotifyInAdvance).plus(Days.THREE), victor);

        // Set up cars with upcoming Itp & Tahograf Expiry Date in EXACTLY daysUntilItpExpires
        mihneasCarWithUpcomingItp = new Car("B 23 BUB", currentDateForTest.plus(daysToNotifyInAdvance), mihnea);
        mihneasCarWithUpcomingTahograf = new Car("B 45 MIC", currentDateForTest, mihnea);
        mihneasCarWithUpcomingTahograf.setTahografExpiryDate(currentDateForTest.plus(daysToNotifyInAdvance));
        bunusCarWithUpcomingItp = new Car("AG 88 VEE", currentDateForTest.plus(daysToNotifyInAdvance), bunu);

        // Set up cars with upcoming Itp Expiry Date in LESS than daysUntilItpExpires
        danielsCar = new Car("B 33 DPT", currentDateForTest.plus(Days.ONE), daniel);

        expectedCars = Arrays.asList(victorsCarWithoutUpcomingItp,
                mihneasCarWithUpcomingItp,
                mihneasCarWithUpcomingTahograf,
                bunusCarWithUpcomingItp,
                danielsCar);
    }

    private static void setUpCarOwners() {
        victor = new CarOwner("Victor", "Patentasu", "1111111111");
        mihnea = new CarOwner("Mihnea", "Patentasu", "2222222222");
        bunu = new CarOwner("Bunu", "Patentasu", "3333333333");
        daniel = new CarOwner("Daniel", "Patentasu", "4444444444");

        carOwners = Arrays.asList(victor, mihnea, daniel, bunu);
    }

    protected static void setUpCarsAndOwnersInDbAndOpenSession(SessionFactory sessionFactory) {
        LOGGER.info(" ---->>>> DB SETUP: Persisting Cars and Owners to DB <<<<----");

        Session session = sessionFactory.getCurrentSession();
        Transaction txt = null;
        try {
            txt = session.beginTransaction();
            for (CarOwner carOwner : carOwners) {
                session.persist(carOwner);
            }
            txt.commit();
            LOGGER.info("---->>>> FINISHED DB SETUP: All owners and cars persisted to DB <<<<----");

        } catch (Exception e) {

            LOGGER.error("---->>>> Error occurred when adding clients !! Rolling back. {}<<<<----" );
            e.printStackTrace();
            if (txt != null) {
                txt.rollback();
            }
        }
    }

    protected static void removeCarsAndOwnersFromDbAndCloseSession(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new RuntimeException("SessionFactory is null!! Have you called setUpCarsAndOwnersInDbAndOpenSession?");
        }

        LOGGER.info(" ---->>>> Removing Cars and Owners from DB <<<<----");
        Session session = sessionFactory.getCurrentSession();
        Transaction txt = null;
        try {
            txt = session.beginTransaction();
            String deleteAllCarsDataString = "delete from Car";
            String deleteAllCarOwnersDataString = "delete from CarOwner";
            session.createQuery(deleteAllCarsDataString).executeUpdate();
            session.createQuery(deleteAllCarOwnersDataString).executeUpdate();
            txt.commit();
            LOGGER.info("---->>>> All cars and owners removed from DB! <<<<----");
        } catch (Exception e) {

            LOGGER.error("---->>>> Error occurred while removing clients !! Rolling back. <<<<----");
            e.printStackTrace();
            if (txt != null) {
                txt.rollback();
            }
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}
