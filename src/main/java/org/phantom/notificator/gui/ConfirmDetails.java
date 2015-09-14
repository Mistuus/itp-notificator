package org.phantom.notificator.gui;

import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.validation.ConstraintViolation;
import java.awt.*;
import java.util.Set;

/**
 * Created by mihne_000 on 7/6/2015.
 */
public class ConfirmDetails extends JFrame {
    private static final String NEW_LINE = "\n";
    private static final Dimension PREFERRED_SIZE = new Dimension(500, 500);
    private JButton backToMainMenuButton;
    private JTable oldClientDetailsTable;
    private JTable newClientDetailsTable;
    private JTable oldCarDetailsTable;
    private JTable newCarDetailsTable;
    private JButton backButton;
    private JButton confirmDetailsButton;
    private JPanel panel;
    private JScrollPane jscroll1;
    private JScrollPane jscroll2;
    private JScrollPane jscroll3;
    private JScrollPane jscroll4;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    private CarOwner owner;
    private Car car;

    public ConfirmDetails(CarMapper carMapper, CarOwnerMapper carOwnerMapper, CarOwner owner, Car car) {
        super("Confirm Details");
        this.carMapper = carMapper;
        this.carOwnerMapper = carOwnerMapper;
        this.owner = owner;
        this.car = car;
        add(panel);
        setUpButtonListeners();
        setPreferredSize(PREFERRED_SIZE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setUpButtonListeners() {
        backToMainMenuButton.addActionListener(e -> {
            new MainMenu(carMapper, carOwnerMapper);
            setVisible(false);
        });
        backButton.addActionListener(e -> {
            new ViewCars(carMapper, carOwnerMapper);
            setVisible(false);
        });
        confirmDetailsButton.addActionListener(e -> {
            String firstName = (String) newClientDetailsTable.getModel().getValueAt(0, 0);
            String lastName = (String) newClientDetailsTable.getModel().getValueAt(0, 1);
            String companyName = (String) newClientDetailsTable.getModel().getValueAt(0, 2);
            String email = (String) newClientDetailsTable.getModel().getValueAt(0, 3);
            LocalDate itpDate;
            try {
                itpDate = LocalDate.parse(newCarDetailsTable.getModel().getValueAt(0, 2).toString());
            } catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(panel, "Data Expirare ITP este incorecta!\n" +
                                "Data trebuie sa fie de forma yyyy-MM-dd (Exemplu: 2015-01-24)",
                        "Data Expirare ITP incorecta",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            if (companyName != null) {
                owner.setCompanyName(companyName);
            }
            if (email != null) {
                owner.setEmail(email);
            }
            car.setItpExpiryDate(itpDate);
            Set<ConstraintViolation<CarOwner>> carOwnerValidationErrors = carOwnerMapper.getValidationErrorSet(owner);
            Set<ConstraintViolation<Car>> carValidationErrors = carMapper.getValidationErrorSet(car);
            if (carOwnerValidationErrors.isEmpty() && carValidationErrors.isEmpty()) {
                boolean isCarModified = carMapper.changeCarDetails(car);
                displayModificationStatus(isCarModified, car.getCarRegistrationNumber());
                setVisible(false);
                new ViewCars(carMapper, carOwnerMapper);
            } else {
                StringBuilder errorMessageForDisplay = getValidationErrorMessageForDisplay(carOwnerValidationErrors, carValidationErrors);
                JOptionPane.showMessageDialog(panel, errorMessageForDisplay,
                        "Detalii Client/Masina incorecte!",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
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

    private void displayModificationStatus(boolean isCarAdded, String carRegistrationNumber) {
        if (isCarAdded) {
            JOptionPane.showMessageDialog(panel,
                    "Masina cu numarul " + carRegistrationNumber + " a fost modificata in sistem",
                    "Modificare reusita!!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel,
                    "Masina cu numarul " + carRegistrationNumber + " NU a fost modificata in sistem.\n" +
                            "Verifica log-urile pentru mai multe detalii.",
                    "Modificare nereusita!!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void createUIComponents() {
        panel = new JPanel(new BorderLayout());

        Object rowDataWithInitialClientDetails[][] = {{owner.getFirstName(), owner.getLastName(), owner.getCompanyName(), owner.getEmail(), owner.getTelephoneNumber()}};//new Object[0][5];
        Object clientDetailsColumnNames[] = {"Prenume", "Nume", "Firma", "Email", "Nr. Telefon"};

        // Create non-editable table for displaying the old CAR OWNER values
        NonEditableTableModel oldClientDetailsModel = new NonEditableTableModel(rowDataWithInitialClientDetails, clientDetailsColumnNames);
        oldClientDetailsTable = new JTable(oldClientDetailsModel);

        // Create the editable table for displaying the user-modified CAR OWNER values
        EditableClientDetailsModel modifiableClientDetailsModel = new EditableClientDetailsModel(rowDataWithInitialClientDetails, clientDetailsColumnNames);
        newClientDetailsTable = new JTable(modifiableClientDetailsModel);

        Object rowDataForInitialCarDetails[][] = {{car.getCarRegistrationNumber(), "", car.getItpExpiryDate()}};
        Object carDetailsColumnNames[] = {"Nr. Inmatriculare", "Data exp. tahograf", "Data exp. ITP"};

        // Create the non-editable table for displaying the old CAR values
        NonEditableTableModel oldCarDetailsModel = new NonEditableTableModel(rowDataForInitialCarDetails, carDetailsColumnNames);
        oldCarDetailsTable = new JTable(oldCarDetailsModel);

        // Create the non-editable table for displaying the user-modified CAR values
        EditableCarDetailsModel modifiableCarDetailsModel = new EditableCarDetailsModel(rowDataForInitialCarDetails, carDetailsColumnNames);
        newCarDetailsTable = new JTable(modifiableCarDetailsModel);
    }

    private static class EditableClientDetailsModel extends DefaultTableModel {
        public EditableClientDetailsModel(Object[][] rowData, Object[] clientDetailsColumnNames) {
            super(rowData, clientDetailsColumnNames);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 4:
                    return false;
                default:
                    return true;
            }
        }
    }

    private static class EditableCarDetailsModel extends DefaultTableModel {
        public EditableCarDetailsModel(Object[][] rowData1, Object[] carDetailsColumnNames) {
            super(rowData1, carDetailsColumnNames);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return false;
                case 1:
                    return true;
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }

    private class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}

