package org.phantom.notificator.gui;

import org.hibernate.SessionFactory;
import org.phantom.notificator.resources.AbstractTestEnvironmentSetup;
import org.phantom.notificator.resources.MockedHibernateUtil;
import org.phantom.notificator.schedulers.ItpNotificator;

/**
 * Created by mihne on 8/19/2015.
 */
public class ItpNotificatorTest extends AbstractTestEnvironmentSetup{

    public void testGUI() {
        SessionFactory sessionFactoryForTest = MockedHibernateUtil.getSessionFactory();
        // setup DB
        setUpCarsAndOwnersWithoutPersistingToDb();
        setUpCarsAndOwnersInDbAndOpenSession(sessionFactoryForTest);

        // start GUI
        new ItpNotificator(sessionFactoryForTest);
    }

    public static void main(String[] args) {
        new ItpNotificatorTest().testGUI();
    }
}