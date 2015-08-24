package org.phantom.notificator.gui;

import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mihne_000 on 6/30/2015.
 */
public class AddCar extends JFrame{
    private JPanel panel1;
    private JCheckBox existingClientCheckBox;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton addCarButton;
    private JButton backButton;
    private JButton nextButton;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    public AddCar(){
        super("Add Cars");
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
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfirmDetails(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
    }
}
