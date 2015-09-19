package org.phantom.notificator.mappers;

import org.hamcrest.Matchers;
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
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.resources.AbstractTestEnvironmentSetup;
import org.phantom.notificator.resources.MockedHibernateUtil;

import java.util.List;

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
    public void testRetrieveAllCars() {
        List<Car> actualCars = mapper.retrieveAllCars();
        Assert.assertThat(actualCars, Matchers.containsInAnyOrder(expectedCars.toArray()));
    }

    @Test
    public void testIsValidCar() throws Exception {
        Assert.assertTrue(mapper.getValidationErrorSet(danielsCar).isEmpty());
    }

    @Test
    public void testIsInvalidCar() {
        CarOwner testCarOwner = new CarOwner("Test", "User", "0741234567");
        Car lessThan8Characters = new Car("abc", saturdayCurrentDateForTest, testCarOwner);
        Car moreThan10Characters = new Car("abc 123 abc", saturdayCurrentDateForTest, testCarOwner);
        Car noItpExpiryDate = new Car("AG 07 ABC", null, testCarOwner);

        Assert.assertFalse(mapper.getValidationErrorSet(lessThan8Characters).isEmpty());
        Assert.assertFalse(mapper.getValidationErrorSet(moreThan10Characters).isEmpty());
        Assert.assertFalse(mapper.getValidationErrorSet(noItpExpiryDate).isEmpty());
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
        Car nonExistentCar = new Car("AG 07 ABC", saturdayCurrentDateForTest, bunu);
        Assert.assertFalse(mapper.changeCarDetails(nonExistentCar));
    }

    @Test
    public void testAddCarToOwner() throws Exception {
        Car carToAdd = new Car("AG 07 ABC", saturdayCurrentDateForTest, daniel);
        Assert.assertTrue(mapper.addCar(carToAdd));
        removeCar(carToAdd);
    }

    @Test
    public void testDoNotAddExistingCarToOwner() {
        Assert.assertFalse(mapper.addCar(danielsCar));
    }


    private Car retrieveCar(Car car) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Car nonExistentCar;
        try {
            transaction = currentSession.beginTransaction();
            nonExistentCar = currentSession.get(Car.class, car.getCarRegistrationNumber());
            transaction.commit();
            return nonExistentCar;
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