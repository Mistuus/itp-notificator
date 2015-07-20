package org.phantom.notificator.mappers;

import org.hibernate.SessionFactory;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.util.ValidationUtil;

/**
 * Created by Master Victor on 06/07/2015.
 */
public class CarMapper {

    private final SessionFactory sessionFactory;

    public CarMapper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //todo: Do we need getValidationError() method
    public boolean isValidCar(Car carToValidate) {
        return ValidationUtil.getValidator().validate(carToValidate).isEmpty();
    }

    public boolean isRegistrationNumberInDb(String carRegistrationNo) {
        return false;
    }

    public boolean isCarInDb(Car car) {
        return false;
    }

    public Car retrieveCar(String carRegistrationNo) {
        return null;
    }

    public boolean addCarToOwner(CarOwner carOwner, Car car) {
        return false;
    }

    public boolean removeCar(Car car) {
        return false;
    }

    public boolean removeCar(String carRegistrationNo) {
        return false;
    }

    public boolean changeCarDetails(Car modifiedCar) {
        return false;
    }
}
