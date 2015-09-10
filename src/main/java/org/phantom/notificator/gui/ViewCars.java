package org.phantom.notificator.gui;

import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.synth.SynthButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mihne_000 on 6/30/2015.
 */
public class ViewCars extends JFrame{
    private JTable table1;
    private JButton addButton;
    private JButton modifyButton;
    private JPanel panel1;
    private JLabel jlabel;
    private JButton backToMainMenuButton;
    private JButton removeButton;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    private Car selectedCar;
    private CarOwner prop;
    public ViewCars(CarMapper carMapper,CarOwnerMapper carOwnerMapper){
        super("View Cars");
        this.carMapper=carMapper;
        this.carOwnerMapper=carOwnerMapper;
        add(panel1);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCar(carMapper, carOwnerMapper);
                setVisible(false);
            }
        });
        // TODO: selection prototype
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
//                selectedCar = carMapper.retrieveCar(table1.getValueAt(table1.getSelectedRow(), 1).toString());
//                prop = selectedCar.getCarOwner();

            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCar = carMapper.retrieveCar(table1.getValueAt(table1.getSelectedRow(), 1).toString());
                prop = selectedCar.getCarOwner();
                carMapper.removeCar(selectedCar);
                setVisible(false);
                new ViewCars(carMapper,carOwnerMapper);
                new PopUpRemove(carMapper,carOwnerMapper);

            }
        });
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCar = carMapper.retrieveCar(table1.getValueAt(table1.getSelectedRow(), 1).toString());
                prop = selectedCar.getCarOwner();
                new ConfirmDetails(carMapper, carOwnerMapper, prop, selectedCar);
                setVisible(false);
            }
        });
        /*modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ModifyCar(carMapper, carOwnerMapper);
                setVisible(false);
            }
        });*/
        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu2(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1=new JPanel();
        panel1.setPreferredSize(new Dimension(500,500));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width / 2, height / 2);
        Object rowData[][]=new Object[0][3];
        Object columnNames[]={"Owner","Car Reg. No.","Tel. No.","Date"};
        table1=new JTable(new DefaultTableModel (rowData,columnNames) {
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        });
        table1.setPreferredSize(new Dimension(1000, 1000));
        DefaultTableModel model=(DefaultTableModel) table1.getModel();
        for(Car car :carMapper.retrieveAllCars())
        {
            model.addRow(car.setDetailsVector());
        }
    }
}

