package org.phantom.notificator.util;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Master Victor on 03/07/2015.
 */
public class HibernateUtilTest {

    @Test
    public void testGetSessionFactory() throws Exception {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Assert.assertNotNull(sessionFactory);
    }
}