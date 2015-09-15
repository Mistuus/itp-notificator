package org.phantom.notificator.schedulers;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.services.EmailSender;
import org.phantom.notificator.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Master Victor on 23/06/2015.
 */
// TODO: vic: Backup DB
public class DailyScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyScheduler.class);
    private static final Days DAYS_TO_NOTIFY_IN_ADVANCE = Days.SIX;
    private List<Car> cars;

    public DailyScheduler(List<Car> cars) {
        this.cars = cars;
    }

    public static void main(String[] args) {
        LOGGER.info("---->> Daily Scheduler started... <<<---");
        CarMapper carMapper = new CarMapper(HibernateUtil.getSessionFactory());
        DailyScheduler dailyScheduler = new DailyScheduler(carMapper.retrieveAllCars());
        dailyScheduler.notifyClientsOfUpcomingItp();
        HibernateUtil.getSessionFactory().close();
        LOGGER.info("--->>> Daily Scheduler finished. <<<---");
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    /**
     * Returns true if the current date + {@link #DAYS_TO_NOTIFY_IN_ADVANCE}
     * equals the itpExpiryDate OR the tahografExpiryDate of the car.
     * This tells us the car owner needs to be notified of the upcoming ITP or Tahograf Inspection.
     *
     * @param car Car to see if we need to notify client of upcoming ITP or Tahograf.
     * @return If the condition is true. False, otherwise.
     */
    public boolean shouldNotifyOwner(Car car) {
        LocalDate itpExpiryDate = car.getItpExpiryDate();
        LocalDate tahografExpiryDate = car.getTahografExpiryDate();
        LocalDate currentDate = getCurrentDate();
        LocalDate expectedExpiryDate = currentDate.plus(DAYS_TO_NOTIFY_IN_ADVANCE);
        return expectedExpiryDate.equals(itpExpiryDate) || expectedExpiryDate.equals(tahografExpiryDate);
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
        LOGGER.info("--->>> Preparing to notify clients ... <<<---");
        Map<CarOwner, List<Car>> ownerToCarsMap = retrieveClientsToNotifyOfUpcomingItp();
        LOGGER.info("--->>> Clients to notify: " + ownerToCarsMap.size() + " <<<---");

        EmailSender emailSender = new EmailSender();
        emailSender.sendEmailTo(ownerToCarsMap);
//        SmsSender smsSender = new SmsSender();
//        smsSender.sendSmsTo(ownerToCarsMap);

        LOGGER.info("--->>> Finished notifying clients by email/SMS. <<<---");
    }
}
