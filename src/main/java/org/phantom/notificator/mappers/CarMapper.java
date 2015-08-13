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

    public boolean isValidCar(Car carToValidate) {
        Set<ConstraintViolation<Car>> validationErrorsSet = ValidationUtil.getValidator().validate(carToValidate);
        return validationErrorsSet.isEmpty();
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
        return isValidCar(car) && isRegistrationNumberInDb(car.getCarRegistrationNumber());
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

    public boolean addCarToOwner(CarOwner carOwner, Car car) {
        CarOwnerMapper carOwnerMapper = new CarOwnerMapper(this.sessionFactory);

        // Case 1: Car already in DB for different Owner => Do nothing. Must delete existing car first and then add to new owner
        if (isCarInDb(car)) {
            return false;
        }

        // Case 2: No Owner & No Car in DB => add both
        // Case 3: Owner exists & No Car in DB => add car to owner
        carOwner.addCar(car);
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.saveOrUpdate(carOwner);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while adding {} to {}! {}", car, carOwner, e.getMessage());
            return false;
        }
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
}
