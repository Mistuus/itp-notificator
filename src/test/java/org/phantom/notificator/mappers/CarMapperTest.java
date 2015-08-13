package org.phantom.notificator.mappers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.resources.AbstractTestEnvironmentSetup;
import org.phantom.notificator.util.MockedHibernateUtil;

/**
 * Created by Master Victor on 09/07/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CarMapperTest extends AbstractTestEnvironmentSetup {

    private static SessionFactory sessionFactory = MockedHibernateUtil.getSessionFactory();
    private CarMapper mapper = new CarMapper(sessionFactory);

    @BeforeClass
    public static void setUpBeforeClass() {
        setUpCarsAndOwnersWithoutPersistingToDb();
        setUpCarsAndOwnersInDbAndOpenSession(sessionFactory);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        removeCarsAndOwnersFromDbAndCloseSession(sessionFactory);
    }

    @Test
    public void testIsValidCar() throws Exception {
        Assert.assertTrue(mapper.isValidCar(danielsCar));
    }

    @Test
    public void testIsInvalidCar() {
        Car nullRegistrationNumber = new Car(null, currentDateForTest, daniel);
        Car lessThan8Characters = new Car("abc", currentDateForTest, daniel);
        Car moreThan10Characters = new Car("abc 123 abc", currentDateForTest, daniel);
        Car noItpExpiryDate = new Car("AG 07 ABC", null, daniel);
        Car noOwner = new Car("AG 07 ABC", currentDateForTest, null);

        Assert.assertFalse(mapper.isValidCar(nullRegistrationNumber));
        Assert.assertFalse(mapper.isValidCar(lessThan8Characters));
        Assert.assertFalse(mapper.isValidCar(moreThan10Characters));
        Assert.assertFalse(mapper.isValidCar(noItpExpiryDate));
        Assert.assertFalse(mapper.isValidCar(noOwner));
    }

    @Test
    public void testIsRegistrationNumberInDb() throws Exception {
        String existingRegistrationNumber = danielsCar.getCarRegistrationNumber();
        Assert.assertTrue(mapper.isRegistrationNumberInDb(existingRegistrationNumber));
    }

    @Test
    public void testRegistrationNumberIsNotInDb() {
        String nonExistentCarRegistrationNumber = "AG 123 ABC";
        Assert.assertFalse(mapper.isRegistrationNumberInDb(nonExistentCarRegistrationNumber));
    }

    @Test
    public void testIsCarInDb() throws Exception {
        Assert.assertTrue(mapper.isCarInDb(danielsCar));
    }

    @Test
    public void testCarIsNotInDb() {
        Car nonExistentCar = new Car("AG 124 ABC", currentDateForTest, daniel);
        Assert.assertFalse(mapper.isCarInDb(nonExistentCar));

    }

    @Test
    public void testRetrieveCar() throws Exception {
        String danielsCarRegistrationNo = danielsCar.getCarRegistrationNumber();
        Assert.assertEquals(danielsCar, mapper.retrieveCar(danielsCarRegistrationNo));
    }

    @Test
    public void testRetrieveNoCar() {
        String nonExistentRegistrationNo = "AG 123 ABC";
        Assert.assertNull(mapper.retrieveCar(nonExistentRegistrationNo));
    }

    @Test
    public void testAddCarToOwner() throws Exception {
        Car carToAdd = new Car("AG 07 ABC", currentDateForTest);
        Assert.assertTrue(mapper.addCarToOwner(daniel, carToAdd));
        removeCar(carToAdd);
    }

    @Test
    public void testDoNotAddExistingCarToOwner() {
        Assert.assertFalse(mapper.addCarToOwner(daniel, danielsCar));
    }

    @Test
    public void testRemoveExistingCar() throws Exception {
        Assert.assertTrue(mapper.removeCar(danielsCar));
        Car retrievedCar = retrieveCar(danielsCar);
        Assert.assertNull(retrievedCar);
        addToDb(danielsCar);
    }

    @Test
    public void testDoNotRemoveNonExistentCar() throws Exception {
        Car nonExistentCar = new Car("AG 07 ABC", currentDateForTest);
        Assert.assertFalse(mapper.removeCar(nonExistentCar));
    }

    @Test
    public void testRemoveExistingCarByRegistrationNo() {
        Assert.assertTrue(mapper.removeCar(danielsCar.getCarRegistrationNumber()));
        Car retrievedCar = retrieveCar(danielsCar);
        Assert.assertNull(retrievedCar);
        addToDb(danielsCar);
    }

    @Test
    public void testDoNotRemoveNonExistentCarByRegistrationNo() {
        String nonExistentCarRegistrationNo = "AG 07 ABC";
        Assert.assertFalse(mapper.removeCar(nonExistentCarRegistrationNo));
    }

    @Test
    public void testChangeCarDetails() throws Exception {
        Days daysToChangeItp = Days.ONE;
        LocalDate oldItpExpiryDate = danielsCar.getItpExpiryDate();
        LocalDate newItpExpiryDate = oldItpExpiryDate.plus(daysToChangeItp);

        danielsCar.setItpExpiryDate(newItpExpiryDate);
        Assert.assertTrue(mapper.changeCarDetails(danielsCar));

        Car danielsCarWithNewDetails = retrieveCar(danielsCar);
        Assert.assertEquals(oldItpExpiryDate.plus(daysToChangeItp), danielsCarWithNewDetails.getItpExpiryDate());

        updateCar(danielsCarWithNewDetails);
    }

    @Test
    public void testDoNotChangeDetailsForNonExistentCar() {
        Car nonExistentCar = new Car("AG 07 ABC", currentDateForTest);
        Assert.assertFalse(mapper.changeCarDetails(nonExistentCar));
    }


    private Car retrieveCar(Car car) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Object nonExistentCar;
        try {
            transaction = currentSession.beginTransaction();
            nonExistentCar = currentSession.get(Car.class, car.getCarRegistrationNumber());
            transaction.commit();
            return (Car) nonExistentCar;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private void addToDb(Car car) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.saveOrUpdate(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private void updateCar(Car carToUpdate) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.update(carToUpdate);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private void removeCar(Car carToDelete) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.delete(carToDelete);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}