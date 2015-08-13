package org.phantom.notificator.domain;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Master Victor on 23/06/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
@Entity
@Table(name = "CARS")
public class Car {

    @Id
    @Column(name = "CAR_REGISTRATION_NUMBER")
    @Length(min = 8, max = 10,
            message = "Car Registration Number must be between 8 and 10 characters")
    @NotNull(message = "Telephone Number cannot be null")
    private String carRegistrationNumber;

    @Column(name = "ITP_EXPIRY_DATE")
    @NotNull(message = "ITP Expiry Date must not be null")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate itpExpiryDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade =
            {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @NotNull(message = "Car must have an owner")
    private CarOwner carOwner;

    public Car() {
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, CarOwner carOwner) {
        this.carRegistrationNumber = carRegistrationNumber;
        this.itpExpiryDate = itpExpiryDate;
        this.carOwner = carOwner;
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate) {
        this(carRegistrationNumber, itpExpiryDate, null);
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, String firstName, String lastName, String telephoneNo) {
        this(carRegistrationNumber, itpExpiryDate, new CarOwner(firstName, lastName, telephoneNo));
    }

    @Override
    public String toString() {
        return "Car{" +
                "carRegistrationNumber='" + carRegistrationNumber + '\'' +
                ", itpExpiryDate=" + itpExpiryDate +
                ", carOwner=" + carOwner.toStringWithoutOwnedCars() +
                '}';
    }

    public String getCarRegistrationNumber() {
        return carRegistrationNumber;
    }

    private void setCarRegistrationNumber(String carRegistrationNumber) {
        this.carRegistrationNumber = carRegistrationNumber;
    }

    public LocalDate getItpExpiryDate() {
        return itpExpiryDate;
    }

    public void setItpExpiryDate(LocalDate itpExpiryDate) {
        this.itpExpiryDate = itpExpiryDate;
    }

    public CarOwner getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(CarOwner carOwner) {
        this.carOwner = carOwner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getCarRegistrationNumber() == null) {
            return false;
        }

        if (!(obj instanceof Car)) {
            return false;
        }

        Car other = (Car) obj;
        return this.getCarRegistrationNumber().equals(other.getCarRegistrationNumber());
    }
}
