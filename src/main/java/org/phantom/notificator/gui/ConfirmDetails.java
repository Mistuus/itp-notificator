package org.phantom.notificator.gui;

import org.joda.time.LocalDate;
import org.mockito.cglib.core.Local;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
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
    private CarOwner prop;
    private Car car;
    public ConfirmDetails(CarMapper carMapper,CarOwnerMapper carOwnerMapper,CarOwner prop, Car car) {
        super("Confirm Details");
        this.prop=prop;
        this.car=car;
        add(panel1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu2(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewCars(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });
        confirmDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname= (String) table2.getModel().getValueAt(0,0);
                String lname= (String) table2.getModel().getValueAt(0,1);
                String compname=(String) table2.getModel().getValueAt(0,2);
                String email=(String) table2.getModel().getValueAt(0,3);
                String telNo=(String) table2.getModel().getValueAt(0,4);
                //String itpString=(String) table4.getModel().getValueAt(0,1);
                prop.setFirstName(fname);
                prop.setLastName(lname);
                prop.setCompanyName(compname);
                prop.setEmail(email);
                //LocalDate itpDate = LocalDate.parse(itpString);
                //car.setItpExpiryDate(itpDate);
                carMapper.changeCarDetails(car);
                setVisible(false);
                new MainMenu2(carMapper,carOwnerMapper);
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1=new JPanel(new BorderLayout());
        panel1.setPreferredSize(new Dimension(500, 500));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width / 2, height / 2);
        Object rowData[][]=new Object[0][5];
        Object columnNames[]={"First Name","Last Name","Company Name","Email","Telephone No"};
        table1=new JTable(new DefaultTableModel(rowData,columnNames){
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        });
        DefaultTableModel model1=(DefaultTableModel) table1.getModel();
        model1.addRow(new Object[]{prop.getFirstName(),prop.getLastName(),prop.getCompanyName(),prop.getEmail(),prop.getTelephoneNumber()});
        table2=new JTable(new DefaultTableModel(rowData,columnNames));
        DefaultTableModel model2=(DefaultTableModel) table2.getModel();
        model2.addRow(new Object[]{prop.getFirstName(),prop.getLastName(),prop.getCompanyName(),prop.getEmail(),prop.getTelephoneNumber()});
        Object rowData1[][]=new Object[0][2];
        Object columnNames1[]={"Reg. No","ITP date"};
        table3=new JTable(new DefaultTableModel(rowData1,columnNames1){
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        });
        DefaultTableModel model3=(DefaultTableModel) table3.getModel();
        model3.addRow(new Object[]{car.getCarRegistrationNumber(),car.getItpExpiryDate()});
        table4=new JTable(new DefaultTableModel(rowData1,columnNames1));
        DefaultTableModel model4=(DefaultTableModel) table4.getModel();
        model4.addRow(new Object[]{car.getCarRegistrationNumber(), car.getItpExpiryDate()});

    }
}
