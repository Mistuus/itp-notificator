package org.phantom.notificator.gui;

import org.phantom.notificator.Constants;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;

/**
 * Created by mihne on 8/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MainMenu extends JFrame {
    private JButton viewCarsButton1;
    private JButton addButton;
    private JPanel panel;
    private CarMapper carMapper;
    private CarOwnerMapper carOwnerMapper;

    public MainMenu(CarMapper carMapper, CarOwnerMapper carOwnerMapper) {
        super("Main Menu");
        this.carMapper = carMapper;
        this.carOwnerMapper = carOwnerMapper;
        add(panel);
        setUpButtonListeners();
        setPreferredSize(Constants.PREFERRED_SIZE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setUpButtonListeners() {
        viewCarsButton1.addActionListener(e -> {
            new ViewCars(carMapper, carOwnerMapper);
            setVisible(false);
        });

        addButton.addActionListener(e -> {
            new AddCar(carMapper, carOwnerMapper);
            setVisible(false);
        });
    }

    private void createUIComponents() {
        addButton = new JButton();
        viewCarsButton1 = new JButton();
    }
}
