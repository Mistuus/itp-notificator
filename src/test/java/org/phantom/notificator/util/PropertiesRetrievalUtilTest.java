package org.phantom.notificator.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Victor on 26/08/2015.
 */
public class PropertiesRetrievalUtilTest {

    @Test
    public void testPropertiesRetrieval() {
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("email_username"));
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("email_password"));
    }
}