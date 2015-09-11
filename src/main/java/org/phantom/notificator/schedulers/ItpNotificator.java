package org.phantom.notificator.schedulers;

import org.hibernate.SessionFactory;
import org.phantom.notificator.gui.MainMenu;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;
import org.phantom.notificator.util.HibernateUtil;

/**
 * Created by mihne on 8/18/2015.
 */
public class ItpNotificator {

    public ItpNotificator(SessionFactory sessionFactory) {
        // Create the mappers
        CarMapper carMapper = new CarMapper(sessionFactory);
        CarOwnerMapper carOwnerMapper = new CarOwnerMapper(sessionFactory);

        // Initialise the GUI
        new MainMenu(carMapper, carOwnerMapper);

    }

    public static void main(String[] args) {
        new ItpNotificator(HibernateUtil.getSessionFactory());
    }
}
