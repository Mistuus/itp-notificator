package org.phantom.notificator.gui;

import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by mihne_000 on 6/30/2015.
 */
public class ViewCars extends JFrame {

    public static final Dimension PREFERRED_DIMENSION = new Dimension(500, 500);
    public static final String EMPTY_STRING = "";
    private JTable carsTable;
    private JButton addButton;
    private JButton modifyButton;
    private JPanel panel;
    private JLabel windowTitle;
    private JButton backToMainMenuButton;
    private JButton removeButton;
    private final CarOwnerMapper carOwnerMapper;
    private final CarMapper carMapper;
    private Car selectedCar;
    private CarOwner carOwner;

    public ViewCars(CarMapper carMapper, CarOwnerMapper carOwnerMapper) {
        super("View Cars");
        this.carMapper = carMapper;
        this.carOwnerMapper = carOwnerMapper;
        add(panel);
        setUpButtonListeners();
        setPreferredSize(PREFERRED_DIMENSION);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setUpButtonListeners() {
        addButton.addActionListener(e -> {
            new AddCar(carMapper, carOwnerMapper);
            setVisible(false);
        });

        removeButton.addActionListener(e -> {
            String carRegistrationNumber = getSelectedCarRegistrationNumber();
            boolean isCarDeleted = carMapper.removeCar(carRegistrationNumber);
            if (isCarDeleted) {
                JOptionPane.showMessageDialog(panel,
                        "Masina cu numarul " + carRegistrationNumber + " a fost stearsa din sistem!",
                        "Stergere indeplinita!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel, "Masina nu a fost stearsa din sistem!" +
                        "\nAti selectat masina pana sa apasati butonul Sterge?" +
                        "\n(Verifica log-urile pentru mai multe detalii.)", "Stergere incompleta!!",
                        JOptionPane.ERROR_MESSAGE);
            }
            refreshCarsTable();
        });

        modifyButton.addActionListener(e -> {
            String carRegistrationNumber = getSelectedCarRegistrationNumber();
            if (carRegistrationNumber.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Va rugam selectati o masina si apoi apasati butonul Modifica!",
                        "Nicio masina selectata!",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                selectedCar = carMapper.retrieveCar(carRegistrationNumber);
                carOwner = selectedCar.getCarOwner();
                // TODO: pass to ConfirmDetails only the selected car (from it, we can get the owner)
                new ConfirmDetails(carMapper, carOwnerMapper, carOwner, selectedCar);
                setVisible(false);
            }
        });

        backToMainMenuButton.addActionListener(e -> {
            new MainMenu2(carMapper, carOwnerMapper);
            setVisible(false);
        });
    }

    private String getSelectedCarRegistrationNumber() {
        int selectedRow = carsTable.getSelectedRow();
        int carRegistrationNumberColumn = 1;
        if (selectedRow < 0) {
            return EMPTY_STRING;
        } else {
            return (String) carsTable.getValueAt(selectedRow, carRegistrationNumberColumn);
        }
    }

    private void createUIComponents() {
        // Configure the panel to display the cars table
        panel = new JPanel();

        // Create the JTable to display the cars
        Object rowData[][] = new Object[0][3];
        Object columnNames[] = {"Proprietar", "Nr. inmatriculare", "Nr. Telefon.", "Data exp. ITP"};
        this.carsTable = new JTable(new NonEditableTableModel(rowData, columnNames));

        // Populate the cars table
        refreshCarsTable();
    }

    private void refreshCarsTable() {
        NonEditableTableModel model = (NonEditableTableModel) this.carsTable.getModel();
        // Empty the Table Model of cars
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        // Reload the cars in the table
        for (Car car : carMapper.retrieveAllCars()) {
            model.addRow(car.setDetailsVector());
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

