package org.phantom.notificator.schedulers;

import org.hamcrest.CoreMatchers;
import org.joda.time.Days;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.resources.AbstractTestEnvironmentSetup;

import java.util.*;

/**
 * Created by Master Victor on 29/06/2015.
 */
@SuppressWarnings("unchecked")
public class DailySchedulerTest extends AbstractTestEnvironmentSetup {

    @BeforeClass
    public static void setUpBeforeClass() {
        setUpCarsAndOwnersWithoutPersistingToDb();
    }

    @Test
    public void testCarWithNotifiableClient() {
        boolean isNotifiable = spyDailyScheduler.shouldNotifyOwner(mihneasCarWithUpcomingItp);
        Assert.assertTrue(isNotifiable);
    }

    @Test
    public void testCarWithoutNotifiableClient() {
        boolean isNotifiable = spyDailyScheduler.shouldNotifyOwner(victorsCarWithoutUpcomingItp);
        Assert.assertFalse(isNotifiable);
    }

    @Test
    public void testNotifySundayClient() throws Exception {
        Car carNotifiableOnSunday = new Car("B 123 BCD", saturdayCurrentDateForTest.plus(daysToNotifyInAdvance).plus(Days.ONE), victor);
        boolean isNotifiable = spyDailyScheduler.shouldNotifyOwner(carNotifiableOnSunday);
        Assert.assertTrue(isNotifiable);
    }

    @Test
    public void testRetrieveCarsWithUpcomingItp() {
        Map<CarOwner, List<Car>> clientCarMap = spyDailyScheduler.retrieveClientsToNotifyOfUpcomingItp();

        Assert.assertEquals(2, clientCarMap.size());

        Set<CarOwner> carOwners = clientCarMap.keySet();
        Assert.assertThat(carOwners, CoreMatchers.hasItems(mihnea, bunu));

        Collection<List<Car>> listOfClientCars = clientCarMap.values();
        List<Car> mihneasCars = Arrays.asList(mihneasCarWithUpcomingItp, mihneasCarWithUpcomingTahograf);
        List<Car> bunusCars = Collections.singletonList(bunusCarWithUpcomingItp);
        Assert.assertThat(listOfClientCars, CoreMatchers.hasItems(mihneasCars, bunusCars));
    }

}