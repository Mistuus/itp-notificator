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
    private Client client;

    public Car() {
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, Client client) {
        this.carRegistrationNumber = carRegistrationNumber;
        this.itpExpiryDate = itpExpiryDate;
        this.client = client;
    }

    public Car(String carRegistrationNumber, LocalDate itpExpiryDate, String firstName, String lastName, String telephoneNo) {
        this(carRegistrationNumber, itpExpiryDate, new Client(firstName, lastName, telephoneNo));
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
