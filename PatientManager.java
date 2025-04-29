import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PatientManager {
    
    public boolean addPatient(String fname, String lname, String phone, String email) {
        String id = UUID.randomUUID().toString();
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.addPatient(id, fname, lname, phone, email);
        } else {
            Map<String, Object> patient = new HashMap<>();
            patient.put("id", id);
            patient.put("first_name", fname);
            patient.put("last_name", lname);
            patient.put("phone", phone);
            patient.put("email", email);
            DatabaseConnection.getPatients().put(id, patient);
            return true;
        }
    }
    
    public void fillPatientTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        
        Map<String, Map<String, Object>> patients;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            patients = JdbcDatabaseConnection.getPatients();
        } else {
            patients = DatabaseConnection.getPatients();
        }
        
        for (Map<String, Object> patient : patients.values()) {
            Object[] row = new Object[5];
            row[0] = patient.get("id");
            row[1] = patient.get("first_name");
            row[2] = patient.get("last_name");
            row[3] = patient.get("phone");
            row[4] = patient.get("email");
            model.addRow(row);
        }
    }
    
    public boolean editPatient(String id, String fname, String lname, String phone, String email) {
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.updatePatient(id, fname, lname, phone, email);
        } else {
            if (DatabaseConnection.getPatients().containsKey(id)) {
                Map<String, Object> patient = DatabaseConnection.getPatients().get(id);
                patient.put("first_name", fname);
                patient.put("last_name", lname);
                patient.put("phone", phone);
                patient.put("email", email);
                return true;
            }
            return false;
        }
    }
    
    public boolean removePatient(String id) {
        if (!canDeletePatient(id)) {
            return false;
        }
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.deletePatient(id);
        } else {
            DatabaseConnection.getPatients().remove(id);
            return true;
        }
    }
    
    public boolean canDeletePatient(String id) {
        Map<Integer, Map<String, Object>> transcriptions;
        Map<Integer, Map<String, Object>> prescriptions;
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            transcriptions = JdbcDatabaseConnection.getTranscriptions();
            prescriptions = JdbcDatabaseConnection.getPrescriptions();
        } else {
            transcriptions = DatabaseConnection.getTranscriptions();
            prescriptions = DatabaseConnection.getPrescriptions();
        }
        
        for (Map<String, Object> trans : transcriptions.values()) {
            if (trans.get("patient_id").equals(id)) {
                return false;
            }
        }
        
        for (Map<String, Object> pres : prescriptions.values()) {
            if (pres.get("patient_id").equals(id)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean addTranscription(String patientId, String description, String medSpec, 
                                   String sampleName, String transcription, String keywords) {
        int id;
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            id = JdbcDatabaseConnection.getNextTransId();
            return JdbcDatabaseConnection.addTranscription(id, patientId, description, medSpec, 
                                                          sampleName, transcription, keywords);
        } else {
            id = DatabaseConnection.getNextTransId();
            Map<String, Object> trans = new HashMap<>();
            trans.put("trans_id", id);
            trans.put("patient_id", patientId);
            trans.put("description", description);
            trans.put("medical_specialty", medSpec);
            trans.put("sample_name", sampleName);
            trans.put("transcription", transcription);
            trans.put("keywords", keywords);
            DatabaseConnection.getTranscriptions().put(id, trans);
            return true;
        }
    }
    
    public void fillTranscriptionTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        
        Map<Integer, Map<String, Object>> transcriptions;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            transcriptions = JdbcDatabaseConnection.getTranscriptions();
        } else {
            transcriptions = DatabaseConnection.getTranscriptions();
        }
        
        for (Map<String, Object> trans : transcriptions.values()) {
            Object[] row = new Object[7];
            row[0] = trans.get("trans_id");
            row[1] = trans.get("patient_id");
            row[2] = trans.get("description");
            row[3] = trans.get("medical_specialty");
            row[4] = trans.get("sample_name");
            row[5] = trans.get("transcription");
            row[6] = trans.get("keywords");
            model.addRow(row);
        }
    }
    
    public boolean editTranscription(int transId, String patientId, String description, 
                                    String medSpec, String sampleName, String transcription, 
                                    String keywords) {
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.updateTranscription(transId, patientId, description, 
                                                             medSpec, sampleName, transcription, 
                                                             keywords);
        } else {
            if (DatabaseConnection.getTranscriptions().containsKey(transId)) {
                Map<String, Object> trans = DatabaseConnection.getTranscriptions().get(transId);
                trans.put("patient_id", patientId);
                trans.put("description", description);
                trans.put("medical_specialty", medSpec);
                trans.put("sample_name", sampleName);
                trans.put("transcription", transcription);
                trans.put("keywords", keywords);
                return true;
            }
            return false;
        }
    }
    
    public boolean removeTranscription(int transId) {
        int opt = JOptionPane.showConfirmDialog(null, "Do you want to remove this transcription?", "", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            if (JdbcDatabaseConnection.isUsingJdbc()) {
                return JdbcDatabaseConnection.deleteTranscription(transId);
            } else {
                DatabaseConnection.getTranscriptions().remove(transId);
                return true;
            }
        }
        return false;
    }
    
    public boolean addPrescription(String patientId, String firstName, String medicineName, 
                                  String dosage, String storageCondition, int medicineId) {
        int id;
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            id = JdbcDatabaseConnection.getNextPresId();
            return JdbcDatabaseConnection.addPrescription(id, patientId, firstName, medicineName, 
                                                         dosage, storageCondition, medicineId);
        } else {
            id = DatabaseConnection.getNextPresId();
            Map<String, Object> pres = new HashMap<>();
            pres.put("pres_id", id);
            pres.put("patient_id", patientId);
            pres.put("first_name", firstName);
            pres.put("medicine_name", medicineName);
            pres.put("dosage", dosage);
            pres.put("storage_cond", storageCondition);
            pres.put("medicine_id", medicineId);
            DatabaseConnection.getPrescriptions().put(id, pres);
            return true;
        }
    }
    
    public void fillPrescriptionTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        
        Map<Integer, Map<String, Object>> prescriptions;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            prescriptions = JdbcDatabaseConnection.getPrescriptions();
        } else {
            prescriptions = DatabaseConnection.getPrescriptions();
        }
        
        for (Map<String, Object> pres : prescriptions.values()) {
            Object[] row = new Object[7];
            row[0] = pres.get("pres_id");
            row[1] = pres.get("patient_id");
            row[2] = pres.get("first_name");
            row[3] = pres.get("medicine_name");
            row[4] = pres.get("dosage");
            row[5] = pres.get("storage_cond");
            row[6] = pres.get("medicine_id");
            model.addRow(row);
        }
    }
    
    public boolean removePrescription(int presId) {
        int opt = JOptionPane.showConfirmDialog(null, "Do you want to remove this prescription?", "", JOptionPane.YES_NO_OPTION);
        if (opt == 0) {
            if (JdbcDatabaseConnection.isUsingJdbc()) {
                return JdbcDatabaseConnection.deletePrescription(presId);
            } else {
                DatabaseConnection.getPrescriptions().remove(presId);
                return true;
            }
        }
        return false;
    }
    
    public void searchPatientsByName(JTable table, String name) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        
        Map<String, Map<String, Object>> patients;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            patients = JdbcDatabaseConnection.searchPatientsByName(name);
        } else {
            patients = new HashMap<>();
            for (Map.Entry<String, Map<String, Object>> entry : DatabaseConnection.getPatients().entrySet()) {
                String firstName = (String) entry.getValue().get("first_name");
                if (firstName.toLowerCase().contains(name.toLowerCase())) {
                    patients.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        for (Map<String, Object> patient : patients.values()) {
            Object[] row = new Object[5];
            row[0] = patient.get("id");
            row[1] = patient.get("first_name");
            row[2] = patient.get("last_name");
            row[3] = patient.get("phone");
            row[4] = patient.get("email");
            model.addRow(row);
        }
    }
    
    public void searchTranscriptionsByPatientId(JTable table, String patientId) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        
        Map<Integer, Map<String, Object>> transcriptions;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            transcriptions = JdbcDatabaseConnection.searchTranscriptionsByPatientId(patientId);
        } else {
            transcriptions = new HashMap<>();
            for (Map.Entry<Integer, Map<String, Object>> entry : DatabaseConnection.getTranscriptions().entrySet()) {
                String id = (String) entry.getValue().get("patient_id");
                if (id.contains(patientId)) {
                    transcriptions.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        for (Map<String, Object> trans : transcriptions.values()) {
            Object[] row = new Object[7];
            row[0] = trans.get("trans_id");
            row[1] = trans.get("patient_id");
            row[2] = trans.get("description");
            row[3] = trans.get("medical_specialty");
            row[4] = trans.get("sample_name");
            row[5] = trans.get("transcription");
            row[6] = trans.get("keywords");
            model.addRow(row);
        }
    }
    
    public void searchPrescriptionsByPatientId(JTable table, String patientId) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        
        Map<Integer, Map<String, Object>> prescriptions;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            prescriptions = JdbcDatabaseConnection.searchPrescriptionsByPatientId(patientId);
        } else {
            prescriptions = new HashMap<>();
            for (Map.Entry<Integer, Map<String, Object>> entry : DatabaseConnection.getPrescriptions().entrySet()) {
                String id = (String) entry.getValue().get("patient_id");
                if (id.contains(patientId)) {
                    prescriptions.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        for (Map<String, Object> pres : prescriptions.values()) {
            Object[] row = new Object[7];
            row[0] = pres.get("pres_id");
            row[1] = pres.get("patient_id");
            row[2] = pres.get("first_name");
            row[3] = pres.get("medicine_name");
            row[4] = pres.get("dosage");
            row[5] = pres.get("storage_cond");
            row[6] = pres.get("medicine_id");
            model.addRow(row);
        }
    }
}