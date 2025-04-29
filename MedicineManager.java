import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MedicineManager {
    
    public void fillMedicineTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        Map<Integer, Map<String, Object>> medicines;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            medicines = JdbcDatabaseConnection.getMedicines();
        } else {
            medicines = DatabaseConnection.getMedicines();
        }
        
        for (Map<String, Object> medicine : medicines.values()) {
            Object[] row = new Object[14];
            row[0] = medicine.get("id");
            row[1] = medicine.get("name");
            row[2] = medicine.get("drug_class");
            row[3] = medicine.get("indication");
            row[4] = medicine.get("indication_description");
            row[5] = medicine.get("dosage");
            row[6] = medicine.get("admin_description");
            row[7] = medicine.get("contraindication");
            row[8] = medicine.get("side_effect");
            row[9] = medicine.get("pregnancy_lactation");
            row[10] = medicine.get("precautious");
            row[11] = medicine.get("treatment_duration");
            row[12] = medicine.get("storage_condition");
            row[13] = medicine.get("link");
            model.addRow(row);
        }
    }
    
    public Map<String, Object> getMedicineById(int id) {
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.getMedicineById(id);
        } else {
            return DatabaseConnection.getMedicines().get(id);
        }
    }
    
    public void searchMedicineByName(JTable table, String name) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        Map<Integer, Map<String, Object>> medicines;
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            medicines = JdbcDatabaseConnection.searchMedicinesByName(name);
        } else {
            medicines = new HashMap<>();
            for (Map.Entry<Integer, Map<String, Object>> entry : DatabaseConnection.getMedicines().entrySet()) {
                String medicineName = (String) entry.getValue().get("name");
                if (medicineName.toLowerCase().contains(name.toLowerCase())) {
                    medicines.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        for (Map<String, Object> medicine : medicines.values()) {
            Object[] row = new Object[14];
            row[0] = medicine.get("id");
            row[1] = medicine.get("name");
            row[2] = medicine.get("drug_class");
            row[3] = medicine.get("indication");
            row[4] = medicine.get("indication_description");
            row[5] = medicine.get("dosage");
            row[6] = medicine.get("admin_description");
            row[7] = medicine.get("contraindication");
            row[8] = medicine.get("side_effect");
            row[9] = medicine.get("pregnancy_lactation");
            row[10] = medicine.get("precautious");
            row[11] = medicine.get("treatment_duration");
            row[12] = medicine.get("storage_condition");
            row[13] = medicine.get("link");
            model.addRow(row);
        }
    }
    
    public boolean addPrescriptionForPatient(String patientId, int medicineId) {
        String firstName = "";
        Map<String, Object> patient;
        Map<String, Object> medicine;
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            patient = JdbcDatabaseConnection.getPatientById(patientId);
            medicine = JdbcDatabaseConnection.getMedicineById(medicineId);
        } else {
            patient = DatabaseConnection.getPatients().get(patientId);
            medicine = DatabaseConnection.getMedicines().get(medicineId);
        }
        
        if (patient != null) {
            firstName = (String) patient.get("first_name");
        } else {
            JOptionPane.showMessageDialog(null, "Patient not found", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (medicine == null) {
            JOptionPane.showMessageDialog(null, "Medicine not found", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String medicineName = (String) medicine.get("name");
        String dosage = (String) medicine.get("dosage");
        String storageCondition = (String) medicine.get("storage_condition");
        
        PatientManager patientManager = new PatientManager();
        return patientManager.addPrescription(
            patientId, 
            firstName, 
            medicineName, 
            dosage, 
            storageCondition, 
            medicineId
        );
    }
    
    public boolean addMedicine(String name, String drugClass, String indication, 
                              String indicationDesc, String dosage, String adminDesc,
                              String contraindication, String sideEffect, String pregnancyLactation,
                              String precautions, String duration, String storage, String link) {
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.addMedicine(name, drugClass, indication, 
                                                     indicationDesc, dosage, adminDesc,
                                                     contraindication, sideEffect, pregnancyLactation,
                                                     precautions, duration, storage, link);
        } else {
            int id = (int) DatabaseConnection.getMedicines().size() + 1;
            Map<String, Object> medicine = new HashMap<>();
            medicine.put("id", id);
            medicine.put("name", name);
            medicine.put("drug_class", drugClass);
            medicine.put("indication", indication);
            medicine.put("indication_description", indicationDesc);
            medicine.put("dosage", dosage);
            medicine.put("admin_description", adminDesc);
            medicine.put("contraindication", contraindication);
            medicine.put("side_effect", sideEffect);
            medicine.put("pregnancy_lactation", pregnancyLactation);
            medicine.put("precautious", precautions);
            medicine.put("treatment_duration", duration);
            medicine.put("storage_condition", storage);
            medicine.put("link", link);
            DatabaseConnection.getMedicines().put(id, medicine);
            return true;
        }
    }
    
    public boolean updateMedicine(int id, String name, String drugClass, String indication, 
                                 String indicationDesc, String dosage, String adminDesc,
                                 String contraindication, String sideEffect, String pregnancyLactation,
                                 String precautions, String duration, String storage, String link) {
        
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.updateMedicine(id, name, drugClass, indication, 
                                                        indicationDesc, dosage, adminDesc,
                                                        contraindication, sideEffect, pregnancyLactation,
                                                        precautions, duration, storage, link);
        } else {
            if (DatabaseConnection.getMedicines().containsKey(id)) {
                Map<String, Object> medicine = DatabaseConnection.getMedicines().get(id);
                medicine.put("name", name);
                medicine.put("drug_class", drugClass);
                medicine.put("indication", indication);
                medicine.put("indication_description", indicationDesc);
                medicine.put("dosage", dosage);
                medicine.put("admin_description", adminDesc);
                medicine.put("contraindication", contraindication);
                medicine.put("side_effect", sideEffect);
                medicine.put("pregnancy_lactation", pregnancyLactation);
                medicine.put("precautious", precautions);
                medicine.put("treatment_duration", duration);
                medicine.put("storage_condition", storage);
                medicine.put("link", link);
                return true;
            }
            return false;
        }
    }
    
    public boolean deleteMedicine(int id) {
        if (JdbcDatabaseConnection.isUsingJdbc()) {
            return JdbcDatabaseConnection.deleteMedicine(id);
        } else {
            DatabaseConnection.getMedicines().remove(id);
            return true;
        }
    }
}