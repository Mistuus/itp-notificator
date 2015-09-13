package org.phantom.notificator.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Master Victor on 23/06/2015.
 */
@Entity
@Table(name = "CAR_OWNERS")
public class CarOwner {

    @Column(name = "FIRST_NAME")
    @NotNull(message = "Prenumele nu trebuie sa fie null.")
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotNull(message = "Numele nu trebuie sa fie gol.")
    @Length(min = 1, message = "Numele trebuie sa aiba cel putin 1 caracter.")
    private String lastName;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "EMAIL")
    @Email(message = "Email-ul trebuie sa fie de forma *@*.com (Exemplu: vector@yahoo.com)")
    private String email;

    @Id
    @Column(name = "TELEPHONE_NO")
    @NotNull(message = "Numarul de telefon nu trebuie sa fie gol.")
    @Length(min = 10, max = 13,
            message = "Numarul de telefon trebuie sa fie intre 10 si 13 caractere.")
    private String telephoneNumber;

    @OneToMany(mappedBy = "carOwner",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Set<Car> cars = new HashSet<>();

    protected CarOwner() {
    }

    public CarOwner(String firstName, String lastName, String telephoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    private void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car car) {
        car.setCarOwner(this);
        getCars().add(car);
    }

    public void removeCar(Car car) {
        car.setCarOwner(null);
        getCars().remove(car);
    }

    @Override
    public String toString() {
        return "CarOwner{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", cars=" + cars +
                '}';
    }

    public String toStringWithoutOwnedCars() {
        return "CarOwner{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getTelephoneNumber() == null) {
            return false;
        }

        if (!(obj instanceof CarOwner)) {
            return false;
        }

        return ((CarOwner) obj).getTelephoneNumber().equals(this.getTelephoneNumber());

    }
}
