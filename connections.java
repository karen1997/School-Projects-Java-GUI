package project3;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class connections{
		private String url;
		private String username;
		private String userpassword;
		private MysqlDataSource  userconnection;
		
		public connections(String url, String username, String userpassword){
			this.url = url;
			this.username = username;
			this.userpassword = userpassword;
		}
		
		public void connecting() throws SQLException{
			userconnection = (MysqlDataSource ) DriverManager.getConnection(this.url, this.username, this.userpassword);
		}
		
		public void endconnection() throws SQLException{
			userconnection.close();
		}
		
		public MysqlDataSource  gettingconnection(){
			return this.userconnection;
		}
	}