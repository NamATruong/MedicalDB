/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group13database;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author naman
 */
public class CONNECTION {
    
    public Connection createConnection() {
		
		Connection connection = null;
		
		MysqlDataSource mds = new MysqlDataSource();
		mds.setServerName("localhost");
		mds.setPortNumber(3306);
		mds.setUser("root");
		mds.setPassword("");;
		mds.setDatabaseName("group13");
		
        try {
            connection = mds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
}
