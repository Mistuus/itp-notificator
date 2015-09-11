package org.phantom.notificator.services;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.resources.AbstractTestEnvironmentSetup;

import java.util.*;

/**
 * Created by Victor on 15/08/2015.
 */
public class EmailSenderTest extends AbstractTestEnvironmentSetup {

    private static Properties properties;
    private static EmailSender spyEmailSender;

    @BeforeClass
    public static void setUp() {
        // Set up users
        setUpCarsAndOwnersWithoutPersistingToDb();

        // Set up the email sender
        spyEmailSender = Mockito.spy(new EmailSender());

        // Set up email properties
        properties = new Properties();
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "25");

        // Stub the getProperties() method
        Mockito.when(spyEmailSender.getProperties()).thenReturn(properties);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        properties = null;
        spyEmailSender = null;
    }

    @Test
    public void testSendEmailTo() throws Exception {
        // Set persons to send email to
        String mihneasEmailAddress = "mihnea@email.com";
        String bunusEmailAddress = "bunu@email.com";
        mihnea.setEmail(mihneasEmailAddress);
        bunu.setEmail(bunusEmailAddress);
        HashMap<CarOwner, List<Car>> ownerToCarMap = new HashMap<>();
        ownerToCarMap.put(mihnea, Arrays.asList(mihneasCarWithUpcomingItp, mihneasOtherCarWithUpcomingItp));
        ownerToCarMap.put(bunu, Collections.singletonList(bunusCarWithUpcomingItp));

        // Start sending email
        SimpleSmtpServer server = SimpleSmtpServer.start();
        spyEmailSender.sendEmailTo(ownerToCarMap);
        server.stop();

        // Assert how many email were sent
        Assert.assertEquals(server.getReceivedEmailSize(), ownerToCarMap.size());

        // Grab the first email and Assert Content, Subject and Receivers
        SmtpMessage sentEmail = (SmtpMessage) server.getReceivedEmail().next();
        Assert.assertThat(sentEmail.getHeaderValue("To"), Matchers.oneOf(mihneasEmailAddress, bunusEmailAddress));
        Assert.assertEquals(EmailSender.USERNAME, sentEmail.getHeaderValue("From"));
        Assert.assertEquals(EmailSender.EMAIL_SUBJECT, sentEmail.getHeaderValue("Subject"));
        System.out.println(sentEmail.getBody());
    }
}