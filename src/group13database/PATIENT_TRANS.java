/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group13database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author naman
 */
public class PATIENT_TRANS {

    CONNECTION connection = new CONNECTION();
    PreparedStatement ps;
    ResultSet rs;

    public boolean addTrans(String patientid, String des, String medspec, String sname, String trans, String key) {

        String addQuery = "INSERT INTO `transcriptions`(`patient_id`, `description`, `medical_specialty`, `sample_name`, `transcription`, `keywords`) VALUES (?,?,?,?,?,?)";
        try {

            ps = connection.createConnection().prepareStatement(addQuery);

            ps.setString(1, patientid);
            ps.setString(2, des);
            ps.setString(3, medspec);
            ps.setString(4, sname);
            ps.setString(5, trans);
            ps.setString(6, key);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return false;
            } else {
                Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }
        }

    }

    public void fillTransTable(JTable table) {
        String selectQuery = "SELECT * FROM `transcriptions`";

        try {
            ps = connection.createConnection().prepareStatement(selectQuery);
            rs = ps.executeQuery();
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            Object[] row;

            while (rs.next()) {
                row = new Object[7];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);

                tableModel.addRow(row);

            }

        } catch (SQLException ex) {
            Logger.getLogger(PATIENT_INFO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean editTrans(int transid, String patientid, String des, String medspec, String sname, String trans, String key) {
        String editQuery = "UPDATE `transcriptions` SET `description`=?,`medical_specialty`=?,`sample_name`=?,`transcription`=?,`keywords`=? WHERE `trans_id`=?";

        try {

            ps = connection.createConnection().prepareStatement(editQuery);

            ps.setString(1, des);
            ps.setString(2, medspec);
            ps.setString(3, sname);
            ps.setString(4, trans);
            ps.setString(5, key);
            ps.setString(4, patientid);
            ps.setInt(5, transid);

            return (ps.executeUpdate() > 0);

        } catch (SQLException e) {
            Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public boolean removeTrans(int transid) {
        int opt = JOptionPane.showConfirmDialog(null, "Do you want to remove this transcription?", "", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            String deleteQuery = "DELETE FROM `transcriptions` WHERE `trans_id`=?";

            try {

                ps = connection.createConnection().prepareStatement(deleteQuery);

                ps.setInt(1, transid);

                return (ps.executeUpdate() > 0);

            } catch (SQLException e) {
                Logger.getLogger(CONNECTION.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }

        }
        return false;
    }

}
