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
 * <p>
 * todo: Instead of catching the exceptions or the validation errors
 * todo: make the methods throw them and catch them when needed.
 */
public class CarOwnerMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarOwnerMapper.class);
    private final SessionFactory sessionFactory;

    public CarOwnerMapper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean isValidCarOwner(CarOwner carOwner) {
        Set<ConstraintViolation<CarOwner>> validationErrorsSet = ValidationUtil.getValidator().validate(carOwner);
        return validationErrorsSet.isEmpty();
    }

    public boolean isCarOwnerInDb(CarOwner carOwner) {
        return isValidCarOwner(carOwner) && isNumberInDb(carOwner.getTelephoneNumber());
    }

    public boolean isNumberInDb(String carOwnerTelephoneNo) {
        Set<ConstraintViolation<CarOwner>> telephoneNumberValidationErrors =
                ValidationUtil.getValidator().validateValue(CarOwner.class, "telephoneNumber", carOwnerTelephoneNo);

        if (telephoneNumberValidationErrors.size() != 0) {
            return false;
        }

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        CarOwner carOwner;
        try {
            transaction = currentSession.beginTransaction();
            carOwner = (CarOwner) currentSession.get(CarOwner.class, carOwnerTelephoneNo);
            transaction.rollback();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while searching for {}! Error: {}", carOwnerTelephoneNo, e.getMessage());
            throw e;
        }

        return carOwner != null;
    }

    public CarOwner retrieveCarOwnerWithTelephoneNo(String telephoneNo) {
        if (!isNumberInDb(telephoneNo)) {
            return null;
        }

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        CarOwner carOwner = null;
        try {
            transaction = currentSession.beginTransaction();
            carOwner = (CarOwner) currentSession.get(CarOwner.class, telephoneNo);
            transaction.rollback();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while retrieving {}! Error: {}", carOwner, e.getMessage());
        }

        return carOwner;
    }

    public boolean addCarOwner(CarOwner carOwnerToAdd) {
        // is car owner already in DB
        if (isCarOwnerInDb(carOwnerToAdd)) {
            return false;
        }

        // create session, transaction, commit or rollback if error
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.persist(carOwnerToAdd);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while adding {}! Error: {}", carOwnerToAdd, e.getMessage());
            return false;
        }
    }

    public boolean removeCarOwner(CarOwner carOwner) {
        if (!isCarOwnerInDb(carOwner)) {
            return false;
        }

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.delete(carOwner);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public boolean removeCarOwner(String telephoneNo) {
        CarOwner carOwner = retrieveCarOwnerWithTelephoneNo(telephoneNo);
        return carOwner != null && removeCarOwner(carOwner);
    }

    public boolean changeDetails(CarOwner modifiedCarOwner) {
        if (!isCarOwnerInDb(modifiedCarOwner)) {
            return false;
        }

        // create session, transaction, commit or rollback if error
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = currentSession.beginTransaction();
            currentSession.update(modifiedCarOwner);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error while modifying {}! Error: {}", modifiedCarOwner, e.getMessage());
            return false;
        }
    }

    public boolean addCarToOwner(CarOwner carOwner, Car car) {
        CarMapper carMapper = new CarMapper(this.sessionFactory);

        // Case 1: Car already in DB for different Owner => Do nothing. Must delete existing car first and then add to new owner
        if (carMapper.isCarInDb(car)) {
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
}
