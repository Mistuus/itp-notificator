package org.phantom.notificator.gui;

import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import javax.validation.ConstraintViolation;
import java.awt.*;
import java.util.Set;

/**
 * Created by mihne_000 on 6/30/2015.
 */
public class AddCar extends JFrame {
    private static final String NEW_LINE = "\n";
    private final CarOwnerMapper carOwnerMapper;
    private final CarMapper carMapper;
    private JPanel panel;
    private JTextField carRegistrationNumberTextField;
    private JTextField itpExpiryDateTextField;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField companyNameTextField;
    private JTextField emailTextField;
    private JTextField telephoneNoTextField;
    private JButton addCarButton;
    private JButton backButton;
    private JButton adaugaMasinaButton;
    private JLabel detaliiClientLabel;
    private static final Dimension PREFERRED_SIZE = new Dimension(500, 500);

    public AddCar(CarMapper carMapper, CarOwnerMapper carOwnerMapper) {
        super("Add Cars");
        this.carMapper = carMapper;
        this.carOwnerMapper = carOwnerMapper;
        add(panel);
        setUpButtonListeners();
        setPreferredSize(PREFERRED_SIZE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setUpButtonListeners() {
        backButton.addActionListener(e -> {
            new MainMenu(carMapper, carOwnerMapper);
            setVisible(false);
        });
        adaugaMasinaButton.addActionListener(e -> addCarFromUserInput());
    }

    private void createUIComponents() {
        panel = new JPanel(new BorderLayout());
        panel.setFocusable(true);
    }

    private void addCarFromUserInput() {
        LocalDate itpExpiryDate;
        try {
            itpExpiryDate = LocalDate.parse(itpExpiryDateTextField.getText());
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(panel, "Data Expirare ITP este incorecta!\n" +
                            "Data trebuie sa fie de forma yyyy-MM-dd (Exemplu: 2015-01-24)",
                    "Data Expirare ITP incorecta",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the other text field values
        String carRegistrationNumber = carRegistrationNumberTextField.getText().toUpperCase();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String companyName = companyNameTextField.getText();
        String email = emailTextField.getText();
        String telephoneNo = telephoneNoTextField.getText();

        // Create owner and car
        CarOwner owner = new CarOwner(firstName, lastName, telephoneNo);
        if (!email.isEmpty()) {
            owner.setEmail(email);
        }
        if (!companyName.isEmpty()) {
            owner.setCompanyName(companyName);
        }
        Car carToAdd = new Car(carRegistrationNumber, itpExpiryDate, owner);

        validateAndInsert(owner, carToAdd);
    }

    private void validateAndInsert(CarOwner owner, Car carToAdd) {
        Set<ConstraintViolation<CarOwner>> carOwnerValidationErrors = carOwnerMapper.getValidationErrorSet(owner);
        Set<ConstraintViolation<Car>> carValidationErrors = carMapper.isValidCar(carToAdd);

        if (carOwnerValidationErrors.isEmpty() && carValidationErrors.isEmpty()) {
            boolean isCarAdded = carOwnerMapper.addCarToOwner(owner, carToAdd);
            displayInsertionStatus(isCarAdded, carToAdd.getCarRegistrationNumber());
        } else {
            StringBuilder errorMessageForDisplay = getValidationErrorMessageForDisplay(carOwnerValidationErrors, carValidationErrors);
            JOptionPane.showMessageDialog(panel, errorMessageForDisplay,
                    "Detalii Client/Masina incorecte!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private StringBuilder getValidationErrorMessageForDisplay(Set<ConstraintViolation<CarOwner>> carOwnerValidationErrors, Set<ConstraintViolation<Car>> carValidationErrors) {
        StringBuilder errorMessageForDisplay = new StringBuilder();
        for (ConstraintViolation<Car> a : carValidationErrors) {
            errorMessageForDisplay.append(a.getMessage()).append(NEW_LINE);
        }
        for (ConstraintViolation<CarOwner> a : carOwnerValidationErrors) {
            errorMessageForDisplay.append(a.getMessage()).append(NEW_LINE);
        }
        return errorMessageForDisplay;
    }

    private void displayInsertionStatus(boolean isCarAdded, String carRegistrationNumber) {
        if (isCarAdded) {
            JOptionPane.showMessageDialog(panel,
                    "Masina cu numarul " + carRegistrationNumber + " a fost adaugata in sistem",
                    "Adaugare reusita!!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel,
                    "Masina cu numarul " + carRegistrationNumber + " NU a fost adaugata in sistem.\n" +
                            "Verifica log-urile pentru mai multe detalii.",
                    "Adaugare nereusita!!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
