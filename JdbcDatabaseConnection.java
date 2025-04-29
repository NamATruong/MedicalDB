import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcDatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(JdbcDatabaseConnection.class.getName());
    
    // JDBC Database URL for XAMPP MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/medicalDB-javaProject";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Default XAMPP password is empty
    
    private static Connection connection = null;
    private static boolean useJdbc = true;
    
    // Connection
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            LOGGER.info("Successfully connected to MySQL database");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to connect to database. Using mock data instead.", e);
            useJdbc = false;
        }
    }
    
    // Check if we're using JDBC or mock data
    public static boolean isUsingJdbc() {
        return useJdbc && connection != null;
    }
    
    // Get connection
    public static Connection getConnection() {
        return connection;
    }
    
    // Close connection when application exits
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection", e);
            }
        }
    }
    
    // Validate login
    public static boolean validateLogin(String username, String password) {
        if (!isUsingJdbc()) {
            return DatabaseConnection.validateLogin(username, password);
        }
        
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If there's a result, login is valid
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating login", e);
            return false;
        }
    }
    
    // Patient operations
    public static Map<String, Map<String, Object>> getPatients() {
        if (!isUsingJdbc()) {
            return DatabaseConnection.getPatients();
        }
        
        Map<String, Map<String, Object>> patients = new HashMap<>();
        String query = "SELECT * FROM patients";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, Object> patient = new HashMap<>();
                String id = rs.getString("id");
                patient.put("id", id);
                patient.put("first_name", rs.getString("first_name"));
                patient.put("last_name", rs.getString("last_name"));
                patient.put("phone", rs.getString("phone"));
                patient.put("email", rs.getString("email"));
                patients.put(id, patient);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving patients", e);
        }
        
        return patients;
    }
    
    public static boolean addPatient(String id, String fname, String lname, String phone, String email) {
        if (!isUsingJdbc()) {
            Map<String, Object> patient = new HashMap<>();
            patient.put("id", id);
            patient.put("first_name", fname);
            patient.put("last_name", lname);
            patient.put("phone", phone);
            patient.put("email", email);
            DatabaseConnection.getPatients().put(id, patient);
            return true;
        }
        
        String query = "INSERT INTO patients (id, first_name, last_name, phone, email) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding patient", e);
            return false;
        }
    }
    
    public static boolean updatePatient(String id, String fname, String lname, String phone, String email) {
        if (!isUsingJdbc()) {
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
        
        String query = "UPDATE patients SET first_name = ?, last_name = ?, phone = ?, email = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating patient", e);
            return false;
        }
    }
    
    public static boolean deletePatient(String id) {
        if (!isUsingJdbc()) {
            DatabaseConnection.getPatients().remove(id);
            return true;
        }
        
        String query = "DELETE FROM patients WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting patient", e);
            return false;
        }
    }
    
    public static Map<Integer, Map<String, Object>> getTranscriptions() {
        if (!isUsingJdbc()) {
            return DatabaseConnection.getTranscriptions();
        }
        
        Map<Integer, Map<String, Object>> transcriptions = new HashMap<>();
        String query = "SELECT * FROM transcriptions";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, Object> trans = new HashMap<>();
                int id = rs.getInt("trans_id");
                trans.put("trans_id", id);
                trans.put("patient_id", rs.getString("patient_id"));
                trans.put("description", rs.getString("description"));
                trans.put("medical_specialty", rs.getString("medical_specialty"));
                trans.put("sample_name", rs.getString("sample_name"));
                trans.put("transcription", rs.getString("transcription"));
                trans.put("keywords", rs.getString("keywords"));
                transcriptions.put(id, trans);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving transcriptions", e);
        }
        
        return transcriptions;
    }
    
    // Prescription methods
    public static Map<Integer, Map<String, Object>> getPrescriptions() {
        if (!isUsingJdbc()) {
            return DatabaseConnection.getPrescriptions();
        }
        
        Map<Integer, Map<String, Object>> prescriptions = new HashMap<>();
        String query = "SELECT * FROM prescriptions";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, Object> pres = new HashMap<>();
                int id = rs.getInt("pres_id");
                pres.put("pres_id", id);
                pres.put("patient_id", rs.getString("patient_id"));
                pres.put("first_name", rs.getString("first_name"));
                pres.put("medicine_name", rs.getString("medicine_name"));
                pres.put("dosage", rs.getString("dosage"));
                pres.put("storage_cond", rs.getString("storage_cond"));
                pres.put("medicine_id", rs.getInt("medicine_id"));
                prescriptions.put(id, pres);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving prescriptions", e);
        }
        
        return prescriptions;
    }
    
    // Medicine methods
    public static Map<Integer, Map<String, Object>> getMedicines() {
        if (!isUsingJdbc()) {
            return DatabaseConnection.getMedicines();
        }
        
        Map<Integer, Map<String, Object>> medicines = new HashMap<>();
        String query = "SELECT * FROM medicines";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, Object> med = new HashMap<>();
                int id = rs.getInt("id");
                med.put("id", id);
                med.put("name", rs.getString("name"));
                med.put("drug_class", rs.getString("drug_class"));
                med.put("indication", rs.getString("indication"));
                med.put("indication_description", rs.getString("indication_description"));
                med.put("dosage", rs.getString("dosage"));
                med.put("admin_description", rs.getString("admin_description"));
                med.put("contraindication", rs.getString("contraindication"));
                med.put("side_effect", rs.getString("side_effect"));
                med.put("pregnancy_lactation", rs.getString("pregnancy_lactation"));
                med.put("precautious", rs.getString("precautious"));
                med.put("treatment_duration", rs.getString("treatment_duration"));
                med.put("storage_condition", rs.getString("storage_condition"));
                med.put("link", rs.getString("link"));
                medicines.put(id, med);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving medicines", e);
        }
        
        return medicines;
    }
    
    // ID generation methods
    public static int getNextTransId() {
        if (!isUsingJdbc()) {
            return DatabaseConnection.getNextTransId();
        }
        
        int nextId = 1;
        String query = "SELECT MAX(trans_id) AS max_id FROM transcriptions";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                nextId = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting next transcription ID", e);
        }
        
        return nextId;
    }
    
    public static int getNextPresId() {
        if (!isUsingJdbc()) {
            return DatabaseConnection.getNextPresId();
        }
        
        int nextId = 1;
        String query = "SELECT MAX(pres_id) AS max_id FROM prescriptions";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                nextId = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting next prescription ID", e);
        }
        
        return nextId;
    }

    public static boolean addTranscription(int transId, String patientId, String description, 
                                     String medSpec, String sampleName, String transcription, 
                                     String keywords) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "INSERT INTO transcriptions (trans_id, patient_id, description, medical_specialty, " +
                    "sample_name, transcription, keywords) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transId);
            stmt.setString(2, patientId);
            stmt.setString(3, description);
            stmt.setString(4, medSpec);
            stmt.setString(5, sampleName);
            stmt.setString(6, transcription);
            stmt.setString(7, keywords);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding transcription", e);
            return false;
        }
    }

    public static boolean updateTranscription(int transId, String patientId, String description, 
                                            String medSpec, String sampleName, String transcription, 
                                            String keywords) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "UPDATE transcriptions SET patient_id = ?, description = ?, medical_specialty = ?, " +
                    "sample_name = ?, transcription = ?, keywords = ? WHERE trans_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patientId);
            stmt.setString(2, description);
            stmt.setString(3, medSpec);
            stmt.setString(4, sampleName);
            stmt.setString(5, transcription);
            stmt.setString(6, keywords);
            stmt.setInt(7, transId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transcription", e);
            return false;
        }
    }

    public static boolean deleteTranscription(int transId) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "DELETE FROM transcriptions WHERE trans_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transcription", e);
            return false;
        }
    }

    public static boolean addPrescription(int presId, String patientId, String firstName, 
                                    String medicineName, String dosage, String storageCondition, 
                                    int medicineId) {
    if (!isUsingJdbc()) {
        return false;
    }
    
    String query = "INSERT INTO prescriptions (pres_id, patient_id, first_name, medicine_name, " +
                  "dosage, storage_cond, medicine_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, presId);
        stmt.setString(2, patientId);
        stmt.setString(3, firstName);
        stmt.setString(4, medicineName);
        stmt.setString(5, dosage);
        stmt.setString(6, storageCondition);
        stmt.setInt(7, medicineId);
        
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error adding prescription", e);
        return false;
    }
}

    public static boolean updatePrescription(int presId, String patientId, String firstName, 
                                        String medicineName, String dosage, String storageCondition, 
                                        int medicineId) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "UPDATE prescriptions SET patient_id = ?, first_name = ?, medicine_name = ?, " +
                    "dosage = ?, storage_cond = ?, medicine_id = ? WHERE pres_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patientId);
            stmt.setString(2, firstName);
            stmt.setString(3, medicineName);
            stmt.setString(4, dosage);
            stmt.setString(5, storageCondition);
            stmt.setInt(6, medicineId);
            stmt.setInt(7, presId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating prescription", e);
            return false;
        }
    }

    public static boolean deletePrescription(int presId) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "DELETE FROM prescriptions WHERE pres_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, presId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting prescription", e);
            return false;
        }
    }

    // Medicine operations
    public static boolean addMedicine(String name, String drugClass, String indication, 
                                    String indicationDesc, String dosage, String adminDesc,
                                    String contraindication, String sideEffect, String pregnancyLactation,
                                    String precautions, String duration, String storage, String link) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "INSERT INTO medicines (name, drug_class, indication, indication_description, " +
                    "dosage, admin_description, contraindication, side_effect, pregnancy_lactation, " +
                    "precautious, treatment_duration, storage_condition, link) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, drugClass);
            stmt.setString(3, indication);
            stmt.setString(4, indicationDesc);
            stmt.setString(5, dosage);
            stmt.setString(6, adminDesc);
            stmt.setString(7, contraindication);
            stmt.setString(8, sideEffect);
            stmt.setString(9, pregnancyLactation);
            stmt.setString(10, precautions);
            stmt.setString(11, duration);
            stmt.setString(12, storage);
            stmt.setString(13, link);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding medicine", e);
            return false;
        }
    }

    public static boolean updateMedicine(int id, String name, String drugClass, String indication, 
                                    String indicationDesc, String dosage, String adminDesc,
                                    String contraindication, String sideEffect, String pregnancyLactation,
                                    String precautions, String duration, String storage, String link) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        String query = "UPDATE medicines SET name = ?, drug_class = ?, indication = ?, " +
                    "indication_description = ?, dosage = ?, admin_description = ?, " +
                    "contraindication = ?, side_effect = ?, pregnancy_lactation = ?, " +
                    "precautious = ?, treatment_duration = ?, storage_condition = ?, link = ? " +
                    "WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, drugClass);
            stmt.setString(3, indication);
            stmt.setString(4, indicationDesc);
            stmt.setString(5, dosage);
            stmt.setString(6, adminDesc);
            stmt.setString(7, contraindication);
            stmt.setString(8, sideEffect);
            stmt.setString(9, pregnancyLactation);
            stmt.setString(10, precautions);
            stmt.setString(11, duration);
            stmt.setString(12, storage);
            stmt.setString(13, link);
            stmt.setInt(14, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating medicine", e);
            return false;
        }
    }

    public static boolean deleteMedicine(int id) {
        if (!isUsingJdbc()) {
            return false;
        }
        
        // First, check if there are any prescriptions using this medicine
        String checkQuery = "SELECT COUNT(*) FROM prescriptions WHERE medicine_id = ?";
        
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, id);
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // There are prescriptions using this medicine
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking medicine usage", e);
            return false;
        }
        
        // If no prescriptions use this medicine, delete it
        String deleteQuery = "DELETE FROM medicines WHERE id = ?";
        
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, id);
            
            int rowsAffected = deleteStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting medicine", e);
            return false;
        }
    }

    // Search methods
    public static Map<String, Map<String, Object>> searchPatientsByName(String name) {
        if (!isUsingJdbc()) {
            return null;
        }
        
        Map<String, Map<String, Object>> patients = new HashMap<>();
        String query = "SELECT * FROM patients WHERE first_name LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> patient = new HashMap<>();
                    String id = rs.getString("id");
                    patient.put("id", id);
                    patient.put("first_name", rs.getString("first_name"));
                    patient.put("last_name", rs.getString("last_name"));
                    patient.put("phone", rs.getString("phone"));
                    patient.put("email", rs.getString("email"));
                    patients.put(id, patient);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching patients by name", e);
        }
        
        return patients;
    }

    public static Map<Integer, Map<String, Object>> searchTranscriptionsByPatientId(String patientId) {
        if (!isUsingJdbc()) {
            return null;
        }
        
        Map<Integer, Map<String, Object>> transcriptions = new HashMap<>();
        String query = "SELECT * FROM transcriptions WHERE patient_id LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + patientId + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> trans = new HashMap<>();
                    int id = rs.getInt("trans_id");
                    trans.put("trans_id", id);
                    trans.put("patient_id", rs.getString("patient_id"));
                    trans.put("description", rs.getString("description"));
                    trans.put("medical_specialty", rs.getString("medical_specialty"));
                    trans.put("sample_name", rs.getString("sample_name"));
                    trans.put("transcription", rs.getString("transcription"));
                    trans.put("keywords", rs.getString("keywords"));
                    transcriptions.put(id, trans);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching transcriptions by patient ID", e);
        }
        
        return transcriptions;
    }

    public static Map<Integer, Map<String, Object>> searchPrescriptionsByPatientId(String patientId) {
        if (!isUsingJdbc()) {
            return null;
        }
        
        Map<Integer, Map<String, Object>> prescriptions = new HashMap<>();
        String query = "SELECT * FROM prescriptions WHERE patient_id LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + patientId + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> pres = new HashMap<>();
                    int id = rs.getInt("pres_id");
                    pres.put("pres_id", id);
                    pres.put("patient_id", rs.getString("patient_id"));
                    pres.put("first_name", rs.getString("first_name"));
                    pres.put("medicine_name", rs.getString("medicine_name"));
                    pres.put("dosage", rs.getString("dosage"));
                    pres.put("storage_cond", rs.getString("storage_cond"));
                    pres.put("medicine_id", rs.getInt("medicine_id"));
                    prescriptions.put(id, pres);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching prescriptions by patient ID", e);
        }
        
        return prescriptions;
    }

    public static Map<Integer, Map<String, Object>> searchMedicinesByName(String name) {
        if (!isUsingJdbc()) {
            return null;
        }
        
        Map<Integer, Map<String, Object>> medicines = new HashMap<>();
        String query = "SELECT * FROM medicines WHERE name LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> med = new HashMap<>();
                    int id = rs.getInt("id");
                    med.put("id", id);
                    med.put("name", rs.getString("name"));
                    med.put("drug_class", rs.getString("drug_class"));
                    med.put("indication", rs.getString("indication"));
                    med.put("indication_description", rs.getString("indication_description"));
                    med.put("dosage", rs.getString("dosage"));
                    med.put("admin_description", rs.getString("admin_description"));
                    med.put("contraindication", rs.getString("contraindication"));
                    med.put("side_effect", rs.getString("side_effect"));
                    med.put("pregnancy_lactation", rs.getString("pregnancy_lactation"));
                    med.put("precautious", rs.getString("precautious"));
                    med.put("treatment_duration", rs.getString("treatment_duration"));
                    med.put("storage_condition", rs.getString("storage_condition"));
                    med.put("link", rs.getString("link"));
                    medicines.put(id, med);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching medicines by name", e);
        }
        
        return medicines;
    }

    // Get by ID methods
    public static Map<String, Object> getPatientById(String id) {
        if (!isUsingJdbc()) {
            return null;
        }
        
        String query = "SELECT * FROM patients WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> patient = new HashMap<>();
                    patient.put("id", rs.getString("id"));
                    patient.put("first_name", rs.getString("first_name"));
                    patient.put("last_name", rs.getString("last_name"));
                    patient.put("phone", rs.getString("phone"));
                    patient.put("email", rs.getString("email"));
                    return patient;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting patient by ID", e);
        }
        
        return null;
    }

    public static Map<String, Object> getMedicineById(int id) {
        if (!isUsingJdbc()) {
            return null;
        }
        
        String query = "SELECT * FROM medicines WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> med = new HashMap<>();
                    med.put("id", rs.getInt("id"));
                    med.put("name", rs.getString("name"));
                    med.put("drug_class", rs.getString("drug_class"));
                    med.put("indication", rs.getString("indication"));
                    med.put("indication_description", rs.getString("indication_description"));
                    med.put("dosage", rs.getString("dosage"));
                    med.put("admin_description", rs.getString("admin_description"));
                    med.put("contraindication", rs.getString("contraindication"));
                    med.put("side_effect", rs.getString("side_effect"));
                    med.put("pregnancy_lactation", rs.getString("pregnancy_lactation"));
                    med.put("precautious", rs.getString("precautious"));
                    med.put("treatment_duration", rs.getString("treatment_duration"));
                    med.put("storage_condition", rs.getString("storage_condition"));
                    med.put("link", rs.getString("link"));
                    return med;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting medicine by ID", e);
        }
        
        return null;
    }

}
