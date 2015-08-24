package org.phantom.notificator.gui;

import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mihne_000 on 7/6/2015.
 */
public class SelectCars extends JFrame {
    private JButton backButton;
    private JTable table1;
    private JButton nextButton;
    private JButton backToSeeCarsButton;
    private JPanel panel1;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    public SelectCars() {
        super("Select Cars");
        this.carMapper=carMapper;
        this.carOwnerMapper=carOwnerMapper;
        add(panel1);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        backToSeeCarsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewCars(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ModifyCar();
                setVisible(false);
            }
        });
    }
}
