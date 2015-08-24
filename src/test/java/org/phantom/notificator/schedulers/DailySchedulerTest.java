package org.phantom.notificator.schedulers;

import org.hamcrest.CoreMatchers;
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
    public void testRetrieveCarsWithUpcomingItp() {
        Map<CarOwner, List<Car>> clientCarMap = spyDailyScheduler.retrieveClientsToNotifyOfUpcomingItp();

        Assert.assertEquals(2, clientCarMap.size());

        Set<CarOwner> carOwners = clientCarMap.keySet();
        Assert.assertThat(carOwners, CoreMatchers.hasItems(mihnea, bunu));

        Collection<List<Car>> listOfClientCars = clientCarMap.values();
        List<Car> mihneasCars = Arrays.asList(mihneasCarWithUpcomingItp, mihneasOtherCarWithUpcomingItp);
        List<Car> bunusCars = Collections.singletonList(bunusCar);
        Assert.assertThat(listOfClientCars, CoreMatchers.hasItems(mihneasCars, bunusCars));
    }
}