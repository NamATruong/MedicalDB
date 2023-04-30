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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author naman
 */
public class PATIENT_TRANS {
    CONNECTION connection = new CONNECTION();

    public void fillPatientTable(JTable table) {
        PreparedStatement ps;
        ResultSet rs;
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
}
