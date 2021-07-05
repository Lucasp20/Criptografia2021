/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.senac.controle;

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
		String url = "jdbc:mysql://localhost:3306/secret_messages";
		String user = "root";
		String passwd = "root";
                try {
                    if (con == null || con.isClosed()) {
                        //Class.forName("com.mysql.jdbc.Driver");
                        Class.forName("com.mysql.cj.jdbc.Driver");                      
                        url +="?autoReconnect=true&useSSL=false&serverTimezone=UTC";//for mysql 8
                        con = DriverManager.getConnection(url, user, passwd);
                        System.out.println("Database: Connected in database");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    System.err.println(e.getMessage());		
                }
	}

}
