//PROJECT 3
package project3;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class javasql{
	private MysqlDataSource  connect;
	private ResultSetMetaData data;
	private Vector<String> col;
	
	public javasql(MysqlDataSource  connect){
		this.connect = connect;
	}
	
	public Vector<Vector<String>> run(String query) throws SQLException{
		Vector<Vector<String>> results = new Vector<Vector<String>>();
		Statement currentresult = (Statement) this.connect.createStatement();
		ResultSet setresult = currentresult.executeQuery(query);
		data = setresult.getMetaData();
		int numcol = data.getColumnCount();
		setcol(numcol, data);
		
		while(setresult.next()){
			Vector<String> row = new Vector<String>();
			for(int i=1; i<=numcol; i++){
				row.add(setresult.getString(i));
			}
			results.add(row);
		}
		return results;
	}
	
	public Vector<String> getcol() throws SQLException{
		return this.col;
	}
	
	public int updating(String query) throws SQLException{
		Statement currentresult = this.connect.createStatement();
		return currentresult.executeUpdate(query);
	}
	
	public void setcol(int numcol, ResultSetMetaData data) throws SQLException{
		col = new Vector<String>();
		for(int i=1; i<=numcol; i++){
			col.add(data.getColumnName(i));
		}
	}
	
}