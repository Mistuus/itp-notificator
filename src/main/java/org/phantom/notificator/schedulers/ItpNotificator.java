package org.phantom.notificator.schedulers;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.senders.EmailSender;
import org.phantom.notificator.senders.SmsSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Master Victor on 23/06/2015.
 */
public class ItpNotificator {

    private final Days daysToNotifyInAdvance;
    private List<Car> cars;

    public ItpNotificator(List<Car> cars, Days daysToNotifyInAdvance) {
        this.cars = cars;
        this.daysToNotifyInAdvance = daysToNotifyInAdvance;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    /**
     * Returns true if the current date + {@link #daysToNotifyInAdvance}
     * equals the itpExpiryDate of the car.
     * This tells us the car owner needs to be notified of the upcoming ITP.
     *
     * @param car Car to see if we need to notify client of upcoming ITP.
     * @return If the condition is true. False, otherwise.
     */
    public boolean shouldNotifyOwner(Car car) {
        LocalDate itpExpiryDate = car.getItpExpiryDate();
        LocalDate currentDate = getCurrentDate();
        LocalDate expectedExpiryDate = currentDate.plus(daysToNotifyInAdvance);
        return expectedExpiryDate.equals(itpExpiryDate);
    }

    public Map<CarOwner, List<Car>> retrieveClientsToNotifyOfUpcomingItp() {
        Stream<Car> carsWithUpcomingItp = this.cars.stream().filter(this::shouldNotifyOwner);
        return carsWithUpcomingItp.collect(
                Collectors.groupingBy(
                        Car::getCarOwner,
                        Collectors.mapping(Function.identity(), Collectors.toList())));
    }

    public LocalDate getCurrentDate() {
        // todo: keep an eye on this to see if it takes too long to execute.
        // See thread: https://stackoverflow.com/questions/6280829/jodatimes-localdatetime-is-slow-when-used-the-first-time
        return LocalDate.now();
    }

    public void notifyClientsOfUpcomingItp() {
        Map<CarOwner, List<Car>> ownerToCarsMap = retrieveClientsToNotifyOfUpcomingItp();

        SmsSender smsSender = new SmsSender();
        EmailSender emailSender = new EmailSender();

        smsSender.sendSmsTo(ownerToCarsMap);
        emailSender.sendEmailTo(ownerToCarsMap);
    }
}
