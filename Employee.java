import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;           // 1> import the package

public class Employee {
    private JPanel Main;
    private JLabel label;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JButton saveButton;
    private JTable table1;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;
    private JTextField txtid;
    private JScrollPane table_1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee Management System");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection com;    // 3) To create the connection we have to instantiate the Connection interface
    PreparedStatement pst;   //4) This is a interface

    public void connect(){
        try{
            //forName is belong to the Class class
            Class.forName("com.mysql.cj.jdbc.Driver");    //2) Lord the driver... Register the driver is done by the forName method
            //default username is root and password is empty
            com = DriverManager.getConnection("jdbc:mysql://localhost:/rbcompany","root","");
            System.out.println("Success");
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //take the data in the employee table and set that data in to the JTable
    void table_load(){
        try{
            pst = com.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //constructor of the class
    public Employee() {
        connect();    //calling the connect() method and then this application will connect with the data base
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //there variables to store the values of the text fields
                String empname, salary , mobile;
                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();

                try{
                    //creting the prepare statement
                    pst = com.prepareStatement("insert into employee(employeeName,salary,mobile)value(?,?,?)");
                    //setting values in to the database
                    pst.setString(1,empname);
                    pst.setString(2,salary);
                    pst.setString(3,mobile);
                    pst.executeUpdate();

                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                    table_load();
                    JOptionPane.showMessageDialog(null,"Record Added correctly!!!!!");
                }
                catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String id = txtid.getText();

                    pst = com.prepareStatement("Select employeeName, salary, mobile from employee where id=?");
                    pst.setString(1,id);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next() == true){
                        String employeeName = rs.getString(1);
                        String salary = rs.getString(2);
                        String mobile = rs.getString(3);

                        txtName.setText(employeeName);
                        txtSalary.setText(salary);
                        txtMobile.setText(mobile);
                    }
                    else{
                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");
                        txtid.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Employee number");
                    }
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }


            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid, empname, salary, mobile;

                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();
                empid = txtid.getText();

                try{
                    pst = com.prepareStatement("update employee set employeeName = ?, salary = ?, mobile = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2,salary);
                    pst.setString(3,mobile);
                    pst.setString(4,empid);

                    pst.executeUpdate();

                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtid.setText("");
                    JOptionPane.showMessageDialog(null,"The data updated successfully !!!!!!");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid;

                empid = txtid.getText();

                try {
                    pst = com.prepareStatement("Delete from employee where id = ?");
                    pst.setString(1,empid);
                    pst.executeUpdate();

                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtid.setText("");
                    JOptionPane.showMessageDialog(null,"The data got deleted !!!!!!");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
    }


}
