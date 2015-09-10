package org.phantom.notificator.gui;

import org.joda.time.LocalDate;
import org.phantom.notificator.domain.Car;
import org.phantom.notificator.domain.CarOwner;
import org.phantom.notificator.mappers.CarMapper;
import org.phantom.notificator.mappers.CarOwnerMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * Created by mihne_000 on 6/30/2015.
 */
public class AddCar extends JFrame{
    private JPanel panel1;
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
    private JLabel detaliiClientLabel;

    public AddCar(CarMapper carMapper,CarOwnerMapper carOwnerMapper){
        super("Add Cars");
        add(panel1);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu2(carMapper, carOwnerMapper);
                setVisible(false);
            }
        });
        /*nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfirmDetails(carMapper,carOwnerMapper);
                setVisible(false);
            }
        });*/
        addCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCar(carOwnerMapper,carMapper);
            }
        });
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                            addCar(carOwnerMapper,carMapper);
                            return false;
                        }
                        return false;
                    }
                });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1=new JPanel(new BorderLayout());
        panel1.setPreferredSize(new Dimension(500, 500));
        panel1.setFocusable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width / 2, height / 2);
    }
    private void addCar(CarOwnerMapper carOwnerMapper, CarMapper carMapper)
    {
        String regNo = textField1.getText();
        String calendar = textField2.getText();
        String fname = textField3.getText();
        String lname = textField4.getText();
        String company = textField5.getText();
        String email = textField6.getText();
        String tel = textField7.getText();
        CarOwner prop = new CarOwner(fname, lname, tel);
        carOwnerMapper.addCarOwner(prop);
        carMapper.addCarToOwner(prop, new Car(regNo, new LocalDate(calendar), prop));
        if(carMapper.isRegistrationNumberInDb(regNo))
        {
            new Popup(carMapper,carOwnerMapper);
        }
        else
        {
            new MainMenu2(carMapper,carOwnerMapper);
            setVisible(false);
        }
    }
}
