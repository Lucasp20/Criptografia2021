/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataSourceMySQL {
    
    private final String CONECTION_FAIL_MSG="Nao foi possivel se conectar com o banco de dados.";
    
    private static Connection con = null;

    public Connection getCon() {
        this.ConectaBD();
        return con;
    }

	private void ConectaBD() {
		String url = "jdbc:mysql://localhost:3306/criptografia";
		String user = "root";
		String passwd = "1107";
                try {
                    if (con == null || con.isClosed()) {
                       
                        Class.forName("com.mysql.cj.jdbc.Driver");                      
                        url +="?autoReconnect=true&useSSL=false&serverTimezone=UTC";
                        con = DriverManager.getConnection(url, user, passwd);
                        System.out.println("Database: Connected in database");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    System.err.println(e.getMessage());		
                }
	}

}
