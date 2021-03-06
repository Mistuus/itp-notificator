package org.phantom.notificator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.phantom.notificator.mappers.CarMapperTest;
import org.phantom.notificator.mappers.CarOwnerMapperTest;
import org.phantom.notificator.schedulers.DailySchedulerTest;
import org.phantom.notificator.services.EmailSenderTest;
import org.phantom.notificator.util.HibernateUtilTest;
import org.phantom.notificator.util.PropertiesRetrievalUtilTest;

/**
 * Created by Victor on 26/08/2015 15:21.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CarMapperTest.class,
        CarOwnerMapperTest.class,
        DailySchedulerTest.class,
        EmailSenderTest.class,
        HibernateUtilTest.class,
        PropertiesRetrievalUtilTest.class
        })
public class TestSuites {
}
