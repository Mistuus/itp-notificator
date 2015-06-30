package org.phantom.notificator.schedulers;

import org.hamcrest.CoreMatchers;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;

import java.util.*;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by Master Victor on 29/06/2015.
 */
@SuppressWarnings("unchecked")
public class ItpNotificatorTest {

    private List<Car> cars;
    private ItpNotificator spyItpNotificator;
    private Days daysToNotifyInAdvance;
    private Car victorsCarWithoutUpcomingItp;
    private Car mihneasCarWithUpcomingItp;
    private CarOwner mihnea;
    private CarOwner bunu;
    private Car bunusCar;
    private Car mihneasOtherCar;
    private CarOwner victor;
    private CarOwner daniel;
    private Car danielsCar;

    @Before
    public void setUp() {
        this.daysToNotifyInAdvance = Days.FIVE;
        LocalDate currentDateForTest = new LocalDate(2015, 6, 29);

        setUpClients();
        setUpCars(currentDateForTest);

        this.cars = new ArrayList<>();
        this.cars.add(victorsCarWithoutUpcomingItp);
        this.cars.add(mihneasCarWithUpcomingItp);
        this.cars.add(bunusCar);
        this.cars.add(mihneasOtherCar);
        this.cars.add(danielsCar);

        // Create a mock of ItpNotificator to return a predefined date.
        this.spyItpNotificator = spy(new ItpNotificator(cars, daysToNotifyInAdvance));
        doReturn(currentDateForTest).when(this.spyItpNotificator).getCurrentDate();
    }

    private void setUpCars(LocalDate currentDateForTest) {
        // Set up cars with upcoming Itp Expiry Date in MORE than daysUntilItpExpires
        this.victorsCarWithoutUpcomingItp = new Car("B 725 MIH", currentDateForTest.plus(this.daysToNotifyInAdvance).plus(Days.THREE), victor);

        // Set up cars with upcoming Itp Expiry Date in EXACTLY daysUntilItpExpires
        this.mihneasCarWithUpcomingItp = new Car("B 23 BUB", currentDateForTest.plus(this.daysToNotifyInAdvance), mihnea);
        this.bunusCar = new Car("AG 88 VEE", currentDateForTest.plus(this.daysToNotifyInAdvance), bunu);
        this.mihneasOtherCar = new Car("B 45 MIC", currentDateForTest.plus(this.daysToNotifyInAdvance), mihnea);

        // Set up cars with upcoming Itp Expiry Date in LESS than daysUntilItpExpires
        this.danielsCar = new Car("B 33 DPT", currentDateForTest.plus(Days.ONE), daniel);
    }

    private void setUpClients() {
        this.victor = new CarOwner("Victor", "Patentasu", "1234");
        this.mihnea = new CarOwner("Mihnea", "Patentasu", "3452");
        this.bunu = new CarOwner("Bunu", "Patentasu", "6543");
        this.daniel = new CarOwner("Daniel", "Patentasu", "6894");
    }

    @Test
    public void testCarWithNotifiableClient() {
        boolean isNotifiable = this.spyItpNotificator.shouldNotifyOwner(mihneasCarWithUpcomingItp);
        Assert.assertTrue(isNotifiable);
    }

    @Test
    public void testCarWithoutNotifiableClient() {
        boolean isNotifiable = this.spyItpNotificator.shouldNotifyOwner(victorsCarWithoutUpcomingItp);
        Assert.assertFalse(isNotifiable);
    }

    @Test
    public void testRetrieveCarsWithUpcomingItp() {
        Map<CarOwner, List<Car>> clientCarMap = spyItpNotificator.retrieveClientsToNotifyOfUpcomingItp();

        Assert.assertEquals(2, clientCarMap.size());

        Set<CarOwner> carOwners = clientCarMap.keySet();
        Assert.assertThat(carOwners, CoreMatchers.hasItems(mihnea, bunu));

        Collection<List<Car>> listOfClientCars = clientCarMap.values();
        List<Car> mihneasCars = Arrays.asList(mihneasCarWithUpcomingItp, mihneasOtherCar);
        List<Car> bunusCars = Collections.singletonList(bunusCar);
        Assert.assertThat(listOfClientCars, CoreMatchers.hasItems(mihneasCars, bunusCars));
    }
}