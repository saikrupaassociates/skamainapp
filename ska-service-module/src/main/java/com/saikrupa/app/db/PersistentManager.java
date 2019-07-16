package com.saikrupa.app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class PersistentManager {
	
	private static PersistentManager persistentManager;
	
	private Connection connection;
	
	private static final Logger LOG = Logger.getLogger(PersistentManager.class);

	private PersistentManager() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("database");
			createConnection(bundle);
		} catch(Exception e) {
			LOG.error(e);
		}
	}
	
	private void createConnection(ResourceBundle bundle) {
		try {
			Class.forName(bundle.getString("database.driver.class.name"));
			connection = DriverManager.getConnection(
					bundle.getString("database.connection.string"), 
					bundle.getString("database.db.user"), 
					bundle.getString("database.db.password"));
			
			if(connection == null) {
				throw new Exception("Could not be Connected to database");
			}		
			LOG.info("Database connection established with database : "+bundle.getString("database.connection.string"));
		} catch(Exception e) {
			LOG.error(e);
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public static PersistentManager getPersistentManager() {
		if(persistentManager == null) {
			persistentManager = new PersistentManager();
		}
		return persistentManager;
	}
	
	public String getDatabaseName() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("database");
			String entry = bundle.getString("database.connection.string");
			return entry.substring(entry.lastIndexOf("/")+1, entry.length());
		} catch(Exception e) {
			LOG.error(e);
		}
		return null;
	}
	
	public void closeConnection() {
		if(connection != null) {
			try {
				connection.close();				
			} catch (SQLException e) {
				LOG.error(e);
			}
		}
	}
}
