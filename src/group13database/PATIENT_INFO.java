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
        PreparedStatement ps;
        String addQuery = "INSERT INTO `patientinfo`(`id`, `first_name`, `last_name`, `phone`, `email`) VALUES (?,?,?,?,?)";

        try {

            ps = connection.createConnection().prepareStatement(addQuery);

            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, fname);
            ps.setString(3, lname);
            ps.setString(4, phone);
            ps.setString(5, email);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }

    public void fillPatientTable(JTable table) {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM `patientinfo`";

        try {
            ps = connection.createConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            Object[] row;

            while (rs.next()) {
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

    public boolean editPatient(String id, String fname, String lname, String phone, String email) {
        PreparedStatement ps;
        String editQuery = "UPDATE `patientinfo` SET `first_name`=?,`last_name`=?,`phone`=?,`email`=? WHERE `id`=?";

        try {

            ps = connection.createConnection().prepareStatement(editQuery);

            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, id);

            return (ps.executeUpdate() > 0);

        } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public boolean removePatient(String id) {

        if (!canDeletePatient(id) || !canDeletePatient2(id)) {
            return false;
        }

        PreparedStatement ps;
        String deleteQuery = "DELETE FROM `patientinfo` WHERE `id`=?";

        try {

            ps = connection.createConnection().prepareStatement(deleteQuery);

            ps.setString(1, id);

            return (ps.executeUpdate() > 0);

        } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }

    public boolean canDeletePatient(String id) {
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT COUNT(*) FROM transcriptions WHERE patient_id=?";

        try {
            ps = connection.createConnection().prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // return true if no transcriptions exist for the patient
            }
            return false;

        } catch (SQLException ex) {
            Logger.getLogger(PATIENT_INFO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean canDeletePatient2(String id) {
        PreparedStatement ps;
        ResultSet rs;
        String query = "SELECT COUNT(*) FROM prescriptions WHERE patient_id=?";

        try {
            ps = connection.createConnection().prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // return true if no transcriptions exist for the patient
            }
            return false;

        } catch (SQLException ex) {
            Logger.getLogger(PATIENT_INFO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
