package org.phantom.notificator.gui;

import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mihne_000 on 7/6/2015.
 */
public class Popup extends JFrame{
    private JButton okButton;
    private JPanel panel1;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    public Popup(CarMapper carMapper,CarOwnerMapper carOwnerMapper) {
        super("PopUp");
        add(panel1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1=new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width / 2, height / 2);
    }
}