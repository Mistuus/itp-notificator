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
            message = "Numarul de inmatriculare trebuie sa fie intre 8 si 10 caractere.")
    @NotNull(message = "Numarul de inregistrare nu poate fi null.")
    private String carRegistrationNumber;

    @Column(name = "ITP_EXPIRY_DATE")
    @NotNull(message = "Data de expirare ITP nu trebuie sa fie goala.")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate itpExpiryDate;

    @Column(name = "TAHOGRAF_EXPIRY_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate tahografExpiryDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @NotNull(message = "Masina trebuie sa aiba un proprietar.")
    private CarOwner carOwner;

    public Car() {
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, CarOwner carOwner) {
        this.carRegistrationNumber = carRegistrationNumber.toUpperCase();
        this.itpExpiryDate = itpExpiryDate;
        carOwner.addCar(this);
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

    public Object[] getRowData() {
        return new Object[]{this.getCarOwner().getLastName() + " " + this.getCarOwner().getFirstName(), this.getCarRegistrationNumber(), this.getCarOwner().getTelephoneNumber(), this.getCarOwner().getCompanyName(), this.getCarOwner().getEmail(), this.getItpExpiryDate()};
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

    public LocalDate getTahografExpiryDate() {
        return tahografExpiryDate;
    }

    public void setTahografExpiryDate(LocalDate tahografExpiryDate) {
        this.tahografExpiryDate = tahografExpiryDate;
    }
}
