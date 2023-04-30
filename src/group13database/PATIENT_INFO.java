/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group13database;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author naman
 */
public class PATIENT_INFO {

    CONNECTION connection = new CONNECTION();

    public boolean addPatient(String fname, String lname, String phone, String email) {
        PreparedStatement st;
        ResultSet rs;
        String addQuery = "INSERT INTO `patientinfo`(`id`, `first_name`, `last_name`, `phone`, `email`) VALUES (?,?,?,?,?)";

        try {

            st = connection.createConnection().prepareStatement(addQuery);
            
            st.setString(1, UUID.randomUUID().toString());
            st.setString(2, fname);
            st.setString(3, lname);
            st.setString(4, phone);
            st.setString(5, email);

            if (st.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }
    
    public void fillPatientTable(JTable table){
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM `patientinfo`";
        
        try {
            ps = connection.createConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();
            DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
            Object[] row;
            
            while(rs.next()) {
                row = new Object[5];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                
                tableModel.addRow(row);
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PATIENT_INFO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean editPatient(String id, String fname, String lname, String phone, String email){
        PreparedStatement st;
        String editQuery = "UPDATE `patientinfo` SET `first_name`=?,`last_name`=?,`phone`=?,`email`=? WHERE `id`=?";

        try {

            st = connection.createConnection().prepareStatement(editQuery);

            st.setString(1, fname);
            st.setString(2, lname);
            st.setString(3, phone);
            st.setString(4, email);
            st.setString(5, id);

            return (st.executeUpdate() > 0);
            
            } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
    
    public boolean removePatient(String id) {
        
        PreparedStatement st;
        ResultSet rs;
        String deleteQuery = "DELETE FROM `patientinfo` WHERE `id`=?";

        try {

            st = connection.createConnection().prepareStatement(deleteQuery);

            st.setString(1, id);

            return (st.executeUpdate() > 0);
            
            } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        
    }

}
