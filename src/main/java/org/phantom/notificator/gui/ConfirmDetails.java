package org.phantom.notificator.gui;

import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mihne_000 on 7/6/2015.
 */
public class ConfirmDetails extends JFrame{
    private JButton backToMainMenuButton;
    private JTable table1;
    private JTable table2;
    private JTable table3;
    private JTable table4;
    private JButton backButton;
    private JButton confirmDetailsButton;
    private JPanel panel1;
    private JScrollPane jscroll1;
    private JScrollPane jscroll2;
    private JScrollPane jscroll3;
    private JScrollPane jscroll4;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    public ConfirmDetails(CarMapper carMapper,CarOwnerMapper carOwnerMapper) {
        super("Confirm Details");
        createUIComponents();
        add(panel1);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ModifyCar();
                setVisible(false);
            }
        });
        confirmDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Popup();
                setVisible(false);
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        Object rowData[][]=new Object[1][5];
        Object columnNames[]={"First Name","Last Name","Company Name","Email","Telephone No"};
        table1=new JTable(rowData,columnNames);
        table2=new JTable(rowData,columnNames);
        Object rowData1[][]=new Object[1][2];
        Object columnNames1[]={"Reg. No","ITP date"};
        table3=new JTable(rowData1,columnNames1);
        table4=new JTable(rowData1,columnNames1);
    }
}
