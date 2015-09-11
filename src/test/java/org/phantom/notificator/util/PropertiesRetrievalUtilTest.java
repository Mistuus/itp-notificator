package org.phantom.notificator.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Victor on 26/08/2015.
 */
public class PropertiesRetrievalUtilTest {

    @Test
    public void testPropertiesRetrieval() {
        // Check testing credentials
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("test_db"));
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("test_db_password"));
        // Check prod credentials
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("db"));
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("db_password"));
        // Check email properties
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("email_username"));
        Assert.assertNotNull(PropertiesRetrievalUtil.getProperty("email_password"));
    }
}