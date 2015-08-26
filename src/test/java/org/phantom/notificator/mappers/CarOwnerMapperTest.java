package org.phantom.notificator.mappers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.resources.AbstractTestEnvironmentSetup;
import org.phantom.notificator.resources.MockedHibernateUtil;
import org.phantom.notificator.util.ValidationUtil;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by Master Victor on 06/07/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CarOwnerMapperTest extends AbstractTestEnvironmentSetup {

    private static SessionFactory sessionFactory = MockedHibernateUtil.getSessionFactory();
    private CarOwnerMapper mapper = new CarOwnerMapper(sessionFactory);

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
    public void testIsValidCarOwner() {
        Assert.assertTrue(mapper.isValidCarOwner(victor));
    }

    @Test
    public void testNoValidationErrors() {
        Assert.assertEquals(0, ValidationUtil.getValidator().validate(victor).size());
    }

    @Test
    public void testIsInvalidCarOwner() {
        CarOwner noFirstName = new CarOwner(null, "Popescu", "0123456789");
        CarOwner noLastName = new CarOwner("Andrei", null, "0123456789");
        CarOwner noPhoneNumber = new CarOwner("Andrei", "Popescu", null);
        CarOwner lessThan10Digits = new CarOwner("Andrei", "Popescu", "01234567");
        CarOwner moreThan13Digits = new CarOwner("Andrei", "Popescu", "01234567898765");
        CarOwner invalidEmail = new CarOwner("V", "P", "0123456789");
        invalidEmail.setEmail("vpyahoo.com");

        Assert.assertFalse(mapper.isValidCarOwner(noFirstName));
        Assert.assertFalse(mapper.isValidCarOwner(noLastName));
        Assert.assertFalse(mapper.isValidCarOwner(noPhoneNumber));
        Assert.assertFalse(mapper.isValidCarOwner(lessThan10Digits));
        Assert.assertFalse(mapper.isValidCarOwner(moreThan13Digits));
        Assert.assertFalse(mapper.isValidCarOwner(invalidEmail));
    }

    @Test
    public void testNoFirstName() {
        CarOwner noFirstName = new CarOwner(null, "Popescu", "0123456789");
        Set<ConstraintViolation<CarOwner>> validationErrors = ValidationUtil.getValidator().validate(noFirstName);
        Assert.assertEquals(1, validationErrors.size());
    }

    @Test
    public void testNoLastName() {
        CarOwner noLastName = new CarOwner("Andrei", null, "0123456789");
        Set<ConstraintViolation<CarOwner>> validationErrors = ValidationUtil.getValidator().validate(noLastName);
        Assert.assertEquals(1, validationErrors.size());
    }

    @Test
    public void testNoPhoneNumber() {
        CarOwner noPhoneNumber = new CarOwner("Andrei", "Popescu", null);
        Set<ConstraintViolation<CarOwner>> validationErrors = ValidationUtil.getValidator().validate(noPhoneNumber);
        Assert.assertEquals(1, validationErrors.size());
    }

    @Test
    public void testInvalidPhoneNumber() {
        CarOwner lessThan10Digits = new CarOwner("Andrei", "Popescu", "01234567");
        CarOwner moreThan13Digits = new CarOwner("Andrei", "Popescu", "01234567898765");
        Set<ConstraintViolation<CarOwner>> validationErrorsLessThan10 = ValidationUtil.getValidator().validate(lessThan10Digits);
        Set<ConstraintViolation<CarOwner>> validationErrorsMoreThan10 = ValidationUtil.getValidator().validate(moreThan13Digits);
        Assert.assertEquals(1, validationErrorsLessThan10.size());
        Assert.assertEquals(1, validationErrorsMoreThan10.size());
    }

    @Test
    public void testValidEmail() {
        CarOwner validEmail = new CarOwner("V", "P", "0123456789");
        Set<ConstraintViolation<CarOwner>> validationErrors = ValidationUtil.getValidator().validate(validEmail);
        Assert.assertEquals(0, validationErrors.size());
    }

    @Test
    public void testInvalidEmail() {
        CarOwner invalidEmail = new CarOwner("V", "P", "0123456789");
        invalidEmail.setEmail("vpyahoo.com");
        Set<ConstraintViolation<CarOwner>> validationErrors = ValidationUtil.getValidator().validate(invalidEmail);
        Assert.assertEquals(1, validationErrors.size());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testPersistOwnerWithSameTelephoneNo() {
        CarOwner ownerWithAlreadyPersistedTelephoneNo = new CarOwner("V", "P", victor.getTelephoneNumber());
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.persist(ownerWithAlreadyPersistedTelephoneNo);
            currentSession.flush();
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Test
    public void testNumberExistsInDb() throws Exception {
        Assert.assertTrue(mapper.isNumberInDb(victor.getTelephoneNumber()));
    }

    @Test
    public void testNumberDoesNotExistsInDb() {
        Assert.assertFalse(mapper.isNumberInDb("9876543210"));
    }

    @Test
    public void testCarOwnerExistsInDb() {
        Assert.assertTrue(mapper.isCarOwnerInDb(victor));
    }

    @Test
    public void testCarOwnerDoesNotExistInDb() {
        CarOwner notPersistedCarOwner = new CarOwner("V", "P", "9876543210");
        Assert.assertFalse(mapper.isCarOwnerInDb(notPersistedCarOwner));
    }

    @Test
    public void testRetrieveCarOwnerWithTelephoneNo() {
        Assert.assertEquals(victor, mapper.retrieveCarOwnerWithTelephoneNo(victor.getTelephoneNumber()));
    }

    @Test
    public void testRetrieveNoCarOwnerWithTelephoneNo() throws Exception {
        Assert.assertNull(mapper.retrieveCarOwnerWithTelephoneNo("9876543210"));
    }

    @Test
    public void testAddExistingCarOwner() throws Exception {
        Assert.assertFalse(mapper.addCarOwner(victor));
    }

    @Test
    public void testAddNewCarOwner() throws Exception {
        CarOwner newCarOwner = new CarOwner("V", "P", "9876543210");
        Assert.assertTrue(mapper.addCarOwner(newCarOwner));
        deleteCarOwner(newCarOwner);
    }

    @Test
    public void testRemoveExistingCarOwner() throws Exception {
        Assert.assertTrue(mapper.removeCarOwner(victor));
        CarOwner deletedVictor = retrieveCarOwner(victor);
        Assert.assertNull(deletedVictor);
        addToDb(victor);
    }

    @Test
    public void testDoNotRemoveNonExistentCarOwner() throws Exception {
        CarOwner newCarOwner = new CarOwner("V", "P", "9876543210");
        Assert.assertFalse(mapper.removeCarOwner(newCarOwner));
    }

    @Test
    public void testDoNotRemoveCarOwnerWithNonExistingTelephoneNo() {
        Assert.assertFalse(mapper.removeCarOwner("12345678910"));
    }

    @Test
    public void testRemoveExistingCarOwnerByTelephoneNo() {
        Assert.assertTrue(mapper.removeCarOwner(victor.getTelephoneNumber()));
        CarOwner deletedVictor = retrieveCarOwner(victor);
        Assert.assertNull(deletedVictor);
        addToDb(victor);
    }

    @Test
    public void testChangeDetailsForExistingOwner() {
        String oldEmail = victor.getEmail();
        String newEmail = "vp@yahoo.com";

        victor.setEmail(newEmail);
        Assert.assertTrue(mapper.changeDetails(victor));

        CarOwner updatedVictor = retrieveCarOwner(victor);
        Assert.assertEquals(newEmail, updatedVictor.getEmail());

        victor.setEmail(oldEmail);
        updateCarOwner(victor);
    }

    @Test
    public void testChangeDetailsForNonExistingOwner() {
        CarOwner newCarOwner = new CarOwner("V", "P", "9876543210");
        Assert.assertFalse(mapper.changeDetails(newCarOwner));
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

    private void deleteCarOwner(CarOwner newCarOwner) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.delete(newCarOwner);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private CarOwner retrieveCarOwner(CarOwner carOwner) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Object nonExistentOwner;
        try {
            transaction = currentSession.beginTransaction();
            nonExistentOwner = currentSession.get(CarOwner.class, carOwner.getTelephoneNumber());
            transaction.commit();
            return (CarOwner) nonExistentOwner;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private void addToDb(CarOwner carOwner) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.persist(carOwner);
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

    private void removeCarOwner(CarOwner carOwner) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.delete(carOwner);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private void updateCarOwner(CarOwner carOwner) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.update(carOwner);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
