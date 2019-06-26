package it.polito.tdp.poweroutages.db;

import java.sql.Connection;

public class TestConnection {

	public static void main(String[] args) {
		
		try {
			Connection connection = ConnectDB.getConnection();
			connection.close();
			System.out.println("Connection Test PASSED");
			
			//PowerOutageDAO dao = new PowerOutageDAO() ;
			
			//System.out.println(dao.getNercList()) ;

		} catch (Exception e) {
			System.err.println("Test FAILED");
		}
	}

}
