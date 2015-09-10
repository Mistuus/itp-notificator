package org.phantom.notificator.gui;

import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mihne on 8/26/2015.
 */
public class MainMenu2 extends JFrame {
    private JButton modifyButton;
    private JButton viewCarsButton1;
    private JButton addButton;
    private JPanel panel1;
    private CarMapper carMapper;
    private CarOwnerMapper carOwnerMapper;
    public MainMenu2 ( CarMapper carMapper, CarOwnerMapper carOwnerMapper)  {
        super("Main Menu");
        this.carMapper=carMapper;
        this.carOwnerMapper=carOwnerMapper;
        add(panel1);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewCarsButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewCars(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCar(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelectCars(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        addButton=new JButton();
        viewCarsButton1=new JButton();
        modifyButton=new JButton();
        addButton.setPreferredSize(new Dimension(10,10));
        viewCarsButton1.setPreferredSize(new Dimension(10, 10));
        modifyButton.setPreferredSize(new Dimension(10, 10));
        panel1=new JPanel();
        panel1.setPreferredSize(new Dimension(500, 500));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width / 2, height / 2);
    }
}
