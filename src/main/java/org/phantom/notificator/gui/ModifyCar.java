package org.phantom.notificator.gui;

import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mihne_000 on 7/6/2015.
 */
//TODO: Add column for "Expirare Tahograf"
public class ModifyCar extends JFrame {
    private JButton backToMainMenuButton;
    private JCheckBox forAllCarsCheckBox;
    private JButton backButton;
    private JButton nextButton;
    private JPanel panel1;
    private JScrollPane scrollpane1;
    private JTable table1;
    private JTable table2;
    private JScrollPane scrollpane2;
    private CarOwnerMapper carOwnerMapper;
    private CarMapper carMapper;
    public ModifyCar(CarMapper carMapper,CarOwnerMapper carOwnerMapper) {

        super("Modify Car");
        add(panel1);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        backButton.addActionListener(e -> {
            new SelectCars(carMapper, carOwnerMapper);
            setVisible(false);
        });
        backToMainMenuButton.addActionListener(e -> {
            new MainMenu(carMapper, carOwnerMapper);
            setVisible(false);
        });
        /*nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfirmDetails(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });*/

    }

    private void createUIComponents() {
        Object rowData[][]=new Object[1][5];
        Object columnNames[]={"First Name","Last Name","Company Name","Email","Telephone No"};
        table1=new JTable(rowData,columnNames);
        Object rowData1[][]=new Object[1][2];
        Object columnNames1[]={"Reg. No","ITP date"};
        table2=new JTable(rowData1,columnNames1);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width/2, height/2);
    }
}
