//java sql gui
package project3;
import java.awt.*;
import java.util.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.*;
import java.lang.*;
import java.text.*;
import java.io.*;
import javax.swing.table.*;

public class javasqlgui{
	private  JFrame frame;
	private  JSeparator separator;
	private  JPanel panel;
	private  JTextArea commandarea;
	private  JTable resultarea;
	private  JScrollPane resultscroll;
	private  JScrollPane commandscroll;
	private  JTextField usernametext;
	private  JLabel usernamelabel;
	private  JTextField userpasswordtext;
	private  JLabel userpasswordlabel;
	private  JLabel connectlabel;
	private  JLabel informationlabel;
	private  JLabel commandslabel;
	private  JLabel executelabel;
	private  JLabel datalabel; 
	private  JLabel driverlabel;
	private  JComboBox databasebox;
	private  JComboBox driverbox;
	private  JButton executebutton;
	private  JButton clearcommandsbutton;
	private  JButton connectbutton;
	private  JButton clearresultsbutton;
	private  SpringLayout layout;
	private  connections connection= null;
	private  javasql sql;
	
	public  void main(String[] args){
		sqldata();
	}
	private  void sqldata(){
		frame = new JFrame("SQL Client GUI");
		panel =  new JPanel();
		frame.setSize(750,450);
		separator = new JSeparator();
		Dimension sizes = separator.getPreferredSize();
		sizes.height = 1;
		separator.setPreferredSize(sizes);
		panel.add(separator);
		
		guilabels();
		guitexts();
		guibuttons();
		guiplaces();
		
		frame.add(panel);
		frame.addWindowListener(new WindowListener(){
			@Override
			public void windowActivated(WindowEvent x){}
			@Override
			public void windowClosed(WindowEvent x){}
			@Override
			public void windowClosing(WindowEvent x){
				if(connection != null){
					try{
						connection.endconnection();
					}catch (SQLException e){
						System.out.println("Connection Still open");
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				}else{
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
			@Override
			public void windowDeactivated(WindowEvent x){}
			@Override
			public void windowDeiconified(WindowEvent x){}
			@Override
			public void windowIconified(WindowEvent x){}
			@Override
			public void windowOpened(WindowEvent x){}
		});
		frame.setVisible(true);
	}
	private  void guilabels(){
		usernamelabel = new JLabel("Username");
		userpasswordlabel = new JLabel("Password");
		connectlabel = new JLabel("No Connection Now");
		informationlabel = new JLabel("Enter Database Information");
		commandslabel = new JLabel("Enter SQL Command");
		executelabel = new JLabel("SQL Execution Results");
		driverlabel = new JLabel("JBDC Driver");
		datalabel = new JLabel("Database URL");
		panel.add(usernamelabel);
		panel.add(userpasswordlabel);
		panel.add(connectlabel);
		panel.add(informationlabel);
		panel.add(commandslabel);
		panel.add(executelabel);
		panel.add(driverlabel);
		panel.add(datalabel);
	}
	private  void guitexts(){
		usernametext= new JTextField(20);
		userpasswordtext= new JTextField(20);
		commandarea= new JTextArea(5,30);
		resultarea= new JTable();
		driverbox= new JComboBox(new String[]{"com.mysql.jbdc.Driver"});
		databasebox = new JComboBox(new String[]{"jdbc:mysql://localhost:3312/project3"});
		resultscroll= new JScrollPane(resultarea);
		commandscroll = new JScrollPane(commandarea);
		panel.add(usernametext);
		panel.add(userpasswordtext);
		panel.add(commandscroll);
		panel.add(resultscroll);
		panel.add(driverbox);
		panel.add(databasebox);
	}
	private  void guibuttons(){
		connectbutton= new JButton("Connect to Database");
		executebutton= new JButton("Execute SQL Command");
		executebutton.setEnabled(false);
		clearcommandsbutton= new JButton("Clear SQL Command");
		clearresultsbutton= new JButton("Clear Result Window");
		
		connectbutton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent x){
				if(!usernametext.getText().isEmpty()){
					connection= new connections("jdbc:mysql://localhost:3312/project3",usernametext.getText(),userpasswordtext.getText());
					try{
						connection.connecting();
					}catch (SQLException e) {
						JOptionPane.showMessageDialog(panel, "Could not connect \nUsername and Password do not match");
					}
					if(connection.gettingconnection() != null){
						connectlabel.setText("jdbc:mysql://localhost:3312/project3");
						executebutton.setEnabled(true);
					}
				}
			}
		});
		executebutton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent x){
				String query = commandarea.getText();
				sql = new javasql(connection.gettingconnection());
				Vector<String> col = new Vector<String>();
				Vector<Vector<String>> result = new Vector<Vector<String>>();

				if(query.toLowerCase().startsWith("select")){
					try {
						result = sql.run(query);
						col = sql.getcol();
						resultarea.setModel(new DefaultTableModel(result,col));
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(panel, "Could not execute query");
					}
				}
				else{
					try {
						sql.updating(query);
						resultarea.setModel(new DefaultTableModel(new String[][]{new String[]{"Row Updated!"}},new String[]{""}));
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(panel, "Could not execute query!");
					}
				}
				
			}
			
		});
		clearcommandsbutton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent x){
				commandarea.setText("");
			}
		});
		clearresultsbutton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent x){
				resultarea.setModel(new DefaultTableModel(new String[][]{new String[]{""}},new String[]{""}));
			}
		});
		panel.add(connectbutton);
		panel.add(executebutton);
		panel.add(clearcommandsbutton);
		panel.add(clearresultsbutton);
	}
	private  void guiplaces(){
		layout = new SpringLayout();
		
		layout.putConstraint(SpringLayout.NORTH, informationlabel, 10, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, informationlabel, 5, SpringLayout.WEST, panel);
		
		layout.putConstraint(SpringLayout.NORTH, driverlabel,20, SpringLayout.SOUTH,informationlabel);
		layout.putConstraint(SpringLayout.WEST, driverlabel,5, SpringLayout.WEST,panel);
		
		layout.putConstraint(SpringLayout.NORTH, driverbox, 15, SpringLayout.SOUTH, informationlabel);
		layout.putConstraint(SpringLayout.WEST, driverbox, 0, SpringLayout.WEST, usernametext);
		layout.putConstraint(SpringLayout.EAST, driverbox, -10, SpringLayout.HORIZONTAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.NORTH, datalabel,18, SpringLayout.SOUTH,driverlabel);
		layout.putConstraint(SpringLayout.WEST, datalabel,5, SpringLayout.WEST,panel);
		
		layout.putConstraint(SpringLayout.NORTH, databasebox, 10, SpringLayout.SOUTH, driverbox);
		layout.putConstraint(SpringLayout.WEST, databasebox, 0, SpringLayout.WEST, usernametext);
		layout.putConstraint(SpringLayout.EAST, databasebox, -10, SpringLayout.HORIZONTAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.NORTH, usernamelabel, 20, SpringLayout.SOUTH, datalabel);
		layout.putConstraint(SpringLayout.WEST, usernamelabel, 5, SpringLayout.WEST, panel);
		
		layout.putConstraint(SpringLayout.NORTH, usernametext, 15, SpringLayout.SOUTH, databasebox);
		layout.putConstraint(SpringLayout.WEST, usernametext, 30, SpringLayout.EAST, usernamelabel);
		layout.putConstraint(SpringLayout.EAST, usernametext, -10, SpringLayout.HORIZONTAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.NORTH, userpasswordlabel, 15, SpringLayout.SOUTH, usernamelabel);
		layout.putConstraint(SpringLayout.WEST, userpasswordlabel, 5, SpringLayout.WEST, panel);
		
		layout.putConstraint(SpringLayout.WEST, userpasswordtext, 0, SpringLayout.WEST, usernametext);
		layout.putConstraint(SpringLayout.NORTH, userpasswordtext, 10, SpringLayout.SOUTH, usernametext);
		layout.putConstraint(SpringLayout.EAST, userpasswordtext, -10, SpringLayout.HORIZONTAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.SOUTH, connectlabel, -10, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, connectlabel, 10, SpringLayout.WEST, panel);
		
		layout.putConstraint(SpringLayout.SOUTH, connectbutton, -10, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, connectbutton, -10, SpringLayout.HORIZONTAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.WEST, commandslabel, 160, SpringLayout.EAST, informationlabel);
		layout.putConstraint(SpringLayout.NORTH, commandslabel, 10, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, commandslabel, 10, SpringLayout.HORIZONTAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.WEST, commandscroll, 10, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, commandscroll, 10, SpringLayout.SOUTH, commandslabel);
		layout.putConstraint(SpringLayout.EAST, commandscroll, -10, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.SOUTH, commandscroll, -10, SpringLayout.NORTH, clearcommandsbutton);
		
		layout.putConstraint(SpringLayout.EAST, clearcommandsbutton, -10, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.SOUTH, clearcommandsbutton, -10, SpringLayout.VERTICAL_CENTER, panel);
		
		layout.putConstraint(SpringLayout.WEST, executebutton, 0, SpringLayout.WEST, commandscroll);
		layout.putConstraint(SpringLayout.SOUTH, executebutton, -10, SpringLayout.VERTICAL_CENTER, panel);
		
		
		layout.putConstraint(SpringLayout.SOUTH, resultscroll, -10, SpringLayout.SOUTH, panel);
		layout.putConstraint(SpringLayout.WEST, resultscroll, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, resultscroll, 10, SpringLayout.SOUTH, clearresultsbutton);
		layout.putConstraint(SpringLayout.EAST, resultscroll, -10, SpringLayout.EAST, panel);

		layout.putConstraint(SpringLayout.NORTH, clearresultsbutton, 10, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, clearresultsbutton, 0, SpringLayout.EAST, resultscroll);
		
		layout.putConstraint(SpringLayout.NORTH, executelabel, 10, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, executelabel, 5, SpringLayout.WEST, panel);
		
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, separator, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, separator, 0, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, panel);
		
		panel.setLayout(layout);
	}
}