package org.phantom.notificator.mappers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Master Victor on 06/07/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
// TODO: Minimize the number of DB lookups we do when removing/adding cars/Owners
public class CarMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarMapper.class);
    private final SessionFactory sessionFactory;

    public CarMapper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public  Set<ConstraintViolation<Car>> isValidCar(Car carToValidate) {
        return ValidationUtil.getValidator().validate(carToValidate);
    }

    public boolean isRegistrationNumberInDb(String carRegistrationNo) {
        Set<ConstraintViolation<CarMapper>> validationErrors = ValidationUtil.getValidator()
                .validateValue(CarMapper.class, "carRegistrationNumber", carRegistrationNo);

        if (!validationErrors.isEmpty()) {
            return true;
        }

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Car car;
        try {
            transaction = currentSession.beginTransaction();
            car = (Car) currentSession.get(Car.class, carRegistrationNo);
            transaction.rollback();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while searching for {}! Error: {}", carRegistrationNo, e.getMessage());
            throw e;
        }

        return car != null;
    }

    public boolean isCarInDb(Car car) {
        return isValidCar(car).isEmpty() && isRegistrationNumberInDb(car.getCarRegistrationNumber());
    }

    public Car retrieveCar(String carRegistrationNo) {
        if (!isRegistrationNumberInDb(carRegistrationNo)) {
            return null;
        }

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Car car;
        try {
            transaction = currentSession.beginTransaction();
            car = (Car) currentSession.get(Car.class, carRegistrationNo);
            transaction.rollback();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while retrieving {}! Error: {}", carRegistrationNo, e.getMessage());
            throw e;
        }

        return car;
    }

    public boolean removeCar(Car car) {
        if (!isCarInDb(car)) {
            return false;
        }

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.delete(car);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while removing {}! {}", car, e.getMessage());
            return false;
        }
    }

    public boolean removeCar(String carRegistrationNo) {
        Car car = retrieveCar(carRegistrationNo);
        return car != null && removeCar(car);
    }

    public boolean changeCarDetails(Car modifiedCar) {
        if (!isCarInDb(modifiedCar)) {
            return false;
        }

        // create session, transaction, commit or rollback if error
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.update(modifiedCar);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while modifying {}! Error: {}", modifiedCar, e.getMessage());
            return false;
        }
    }
    public List<Car> retrieveAllCars()
    {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            List<Car> list = currentSession.createCriteria(Car.class).list();
            transaction.commit();
            return list;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while retrieving all cars! Returning empty list! Error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
