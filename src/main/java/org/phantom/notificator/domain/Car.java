package org.phantom.notificator.domain;


import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;

/**
 * Created by Master Victor on 23/06/2015.
 */
@Entity
@Table(name = "CARS")
public class Car {

    @Id
    @Column(name = "CAR_REGISTRATION_NUMBER")
    private String carRegistrationNumber;

    @Column(name = "ITP_EXPIRY_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate itpExpiryDate;
    @Embedded
    private CarOwner thisCarOwner;

    public Car() {
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, CarOwner carOwner) {
        this.carRegistrationNumber = carRegistrationNumber;
        this.itpExpiryDate = itpExpiryDate;
        this.thisCarOwner = carOwner;
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, String firstName, String lastName, String telephoneNo) {
        this(carRegistrationNumber, itpExpiryDate, new CarOwner(firstName, lastName, telephoneNo));
    }

    @Override
    public String toString() {
        return "Car{" +
                "carRegistrationNumber='" + carRegistrationNumber + '\'' +
                ", itpExpiryDate=" + itpExpiryDate +
                ", thisCarOwner=" + thisCarOwner +
                '}';
    }

    public String getCarRegistrationNumber() {
        return carRegistrationNumber;
    }

    public void setCarRegistrationNumber(String carRegistrationNumber) {
        this.carRegistrationNumber = carRegistrationNumber;
    }

    public LocalDate getItpExpiryDate() {
        return itpExpiryDate;
    }

    public void setItpExpiryDate(LocalDate itpExpiryDate) {
        this.itpExpiryDate = itpExpiryDate;
    }

    public CarOwner getCarOwner() {
        return thisCarOwner;
    }

    public void setCarOwner(CarOwner carOwner) {
        this.thisCarOwner = carOwner;
    }
}
