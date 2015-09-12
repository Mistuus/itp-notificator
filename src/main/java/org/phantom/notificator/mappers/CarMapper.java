package org.phantom.notificator.mappers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.phantom.notificator.domain.Car;
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
public class CarMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarMapper.class);
    private final SessionFactory sessionFactory;

    public CarMapper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Set<ConstraintViolation<Car>> getValidationErrorSet(Car carToValidate) {
        return ValidationUtil.getValidator().validate(carToValidate);
    }

    public Car retrieveCar(String carRegistrationNo) {
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
            return null;
        }

        return car;
    }


    public boolean removeCar(String carRegistrationNo) {
        Car car = retrieveCar(carRegistrationNo);
        return car != null && removeCar(car);
    }

    private boolean removeCar(Car car) {
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

    public boolean changeCarDetails(Car modifiedCar) {
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

    public List<Car> retrieveAllCars() {
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

    public boolean addCar(Car car) {
        // Case 1: Car already in DB for different Owner => Do nothing. Must delete existing car first and then add to new owner
        if (retrieveCar(car.getCarRegistrationNumber()) != null) {
            LOGGER.error("Car already exists in the DB for another client. " +
                    "If this is incorrect, please delete it and insert it again.");
            return false;
        }

        // Case 2: No Owner & No Car in DB => add both
        // Case 3: Owner exists & No Car in DB => add car to owner
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.saveOrUpdate(car);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while adding {} to {}! {}", car, e.getMessage());
            return false;
        }
    }
}
