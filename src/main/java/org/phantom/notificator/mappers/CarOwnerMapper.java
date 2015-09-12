package org.phantom.notificator.mappers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

    public Set<ConstraintViolation<CarOwner>> getValidationErrorSet(CarOwner carOwner) {
        return ValidationUtil.getValidator().validate(carOwner);
    }

    public CarOwner retrieveCarOwnerWithTelephoneNo(String telephoneNo) {
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
            return null;
        }

        return carOwner;
    }

    public boolean addCarOwner(CarOwner carOwnerToAdd) {
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

    private boolean removeCarOwner(CarOwner carOwner) {
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

}
