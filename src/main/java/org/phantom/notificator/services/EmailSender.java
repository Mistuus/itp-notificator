package org.phantom.notificator.services;

import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.util.PropertiesRetrievalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * Created by Master Victor on 23/06/2015.
 */
public class EmailSender {

    public static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    private static final String NEW_LINE = "\n";
    private static final String DOUBLE_NEW_LINE = "\n\n";
    private static final String SPACE = " ";
    public static String USERNAME;
    public static String EMAIL_SUBJECT = "Vector Truck Management - Va expira ITP-ul";
    private static String PASSWORD;

    public EmailSender() {
        USERNAME = PropertiesRetrievalUtil.getProperty("email_username");
        PASSWORD = PropertiesRetrievalUtil.getProperty("email_password");
    }

    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();
        CarOwner daniel = new CarOwner("D", "P", "0000000000");
        daniel.setEmail("daniel.patentasu@yahoo.com");
        Car car = new Car("B 123 WWJ", new LocalDate(), daniel);
        Car car1 = new Car("B 725 MIH", new LocalDate(), daniel);
        HashMap<CarOwner, List<Car>> carOwnerToCar = new HashMap<>();
        carOwnerToCar.put(daniel, Arrays.asList(car, car1));
        emailSender.sendEmailTo(carOwnerToCar);
    }

    public void sendEmailTo(Map<CarOwner, List<Car>> ownerToCarsMap) {
        LOGGER.info("--->> Sending email to {} clients <<----", ownerToCarsMap.size());

        ownerToCarsMap.forEach((carOwner, cars) -> {

            String clientEmailAddress = carOwner.getEmail();
            if (clientEmailAddress != null) {
                LOGGER.info("--->> Sending email to client = {}, emailAddress = {} <<----", carOwner.getLastName(), clientEmailAddress);
                String messageForClient = createEmailBodyFrom(cars);
                sendEmailTo(clientEmailAddress, messageForClient);
            } else {
                LOGGER.info("--->> Could not send email to " + carOwner.getFirstName() + ", " + carOwner.getLastName()
                        + " as there is no email address <<----");
            }

        });
    }

    private String createEmailBodyFrom(List<Car> cars) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Draga client,").append(DOUBLE_NEW_LINE);
        stringBuilder.append("Urmatoarelor vehicule le va expira ITP-ul/verificarea tahografului in curand: ").append(DOUBLE_NEW_LINE);

        for (Car car : cars) {
            stringBuilder.append("- Nr. Inmatriculare: ").append(car.getCarRegistrationNumber()).append(SPACE);
            stringBuilder.append(" Data expirare: ").append(car.getItpExpiryDate()).append(NEW_LINE);
        }

        stringBuilder.append(NEW_LINE);
        stringBuilder.append("Va asteptam la noi la firma pentru efectuarea ITP-ului/verificare tahograf.").append(NEW_LINE);
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("Sa aveti o zi buna,").append(NEW_LINE);
        stringBuilder.append("Vector Truck Management SRL").append(DOUBLE_NEW_LINE);
        stringBuilder.append("-----------------------------------------------").append(DOUBLE_NEW_LINE);
        stringBuilder.append("Str. Ecologiei, Nr. 793D, Comuna Albota, judetul Arges, CP 117030").append(NEW_LINE);
        stringBuilder.append("Nota: Ne aflam la iesirea din Pitesti spre Craiova.").append(NEW_LINE);
        stringBuilder.append("Telefon: 0248615056").append(NEW_LINE);
        stringBuilder.append("E-mail: vectortruckmanag@yahoo.ro");

        return stringBuilder.toString();
    }

    private void sendEmailTo(String emailAddress, String messageBody) {
        Session session = getSession(USERNAME, PASSWORD, getProperties());

        try {
            Message msg = createEmail(emailAddress, messageBody, session);
            Transport.send(msg);
            LOGGER.info("-->> Email sent to {}", emailAddress);
        } catch (MessagingException e) {
            LOGGER.error("-->> Email was NOT sent to {}. Error: {}", emailAddress, e.getMessage());
            e.printStackTrace();
        }
    }

    private Message createEmail(String emailAddress, String messageBody, Session session) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("vectortruckmanag@gmail.com"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        msg.setSubject(EMAIL_SUBJECT);
        msg.setText(messageBody);
        return msg;
    }

    private Session getSession(final String username, final String password, Properties props) {
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    // protected for JUnit Tests
    protected Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }
}
