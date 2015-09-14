package org.phantom.notificator.gui;

import org.phantom.notificator.domain.Car;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
    public SelectCars(CarMapper carMapper,CarOwnerMapper carOwnerMapper) {
        super("Select Cars");
        this.carMapper=carMapper;
        this.carOwnerMapper=carOwnerMapper;
        add(panel1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        backButton.addActionListener(e -> {
            new MainMenu(carMapper, carOwnerMapper);
            setVisible(false);
        });
        backToSeeCarsButton.addActionListener(e -> {
            new ViewCars(carMapper, carOwnerMapper);
            setVisible(false);
        });

        nextButton.addActionListener(e -> {
            new ModifyCar(carMapper, carOwnerMapper);
            setVisible(false);
        });
    }

    private void createUIComponents() {
        panel1=new JPanel();
        panel1.setPreferredSize(new Dimension(500,500));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width / 2, height / 2);
        Object rowData[][]=new Object[0][3];
        Object columnNames[]={"Car Reg. No.","Date","Owner"};
        table1=new JTable(new DefaultTableModel(rowData,columnNames) {
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        });
        table1.setPreferredSize(new Dimension(1000, 1000));
        DefaultTableModel model=(DefaultTableModel) table1.getModel();
        model.addRow(columnNames);
        for(Car car :carMapper.retrieveAllCars())
        {
            model.addRow(car.getRowData());
        }
    }
}
