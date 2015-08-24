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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    public ViewCars(CarMapper carMapper,CarOwnerMapper carOwnerMapper){
        super("View Cars");
        this.carMapper=carMapper;
        this.carOwnerMapper=carOwnerMapper;
        add(panel1);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCar();
                setVisible(false);
            }
        });
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ModifyCar();
                setVisible(false);
            }
        });
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // System.out.println(carMapper.retrieveCar(table1.getValueAt(table1.getSelectedRow(), 0).toString()).getCarOwner().getTelephoneNumber());
                // print first column value from selected row
                // System.out.println(table1.getValueAt(table1.getSelectedRow(), 0).toString());
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
       /* for(Iterator iterator=carMapper.retrieveAllCars().iterator();iterator.hasNext();)
        {
            Car car=(Car) iterator.next();
            System.out.println(car.toString());
        }*/
//        System.out.println(carMapper.retrieveAllCars().size());
        panel1=new JPanel();
        panel1.setPreferredSize(new Dimension(500,500));
        Object rowData[][]=new Object[0][3];
        Object columnNames[]={"Car Reg. No.","Date","Owner"};
        //  CarOwner mihCarOwner = new CarOwner("Mihnea", "Patentasu", "077777777");
        //Car mihsCar = new Car( "B 725 MIH", new LocalDate(2014, 10, 5),mihCarOwner);
        table1=new JTable(new DefaultTableModel (rowData,columnNames) {
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
            model.addRow(car.setDetailsVector());
        }

        //model.addRow(mihsCar.setDetailsVector());

                //System.out.println(carMapper.retrieveAllCars().get(0).toString());
    }
}

