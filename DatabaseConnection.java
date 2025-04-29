import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseConnection {
    // Mock data storage
    private static final Map<String, Map<String, Object>> patients = new HashMap<>();
    private static final Map<Integer, Map<String, Object>> transcriptions = new HashMap<>();
    private static final Map<Integer, Map<String, Object>> prescriptions = new HashMap<>();
    private static final Map<Integer, Map<String, Object>> medicines = new HashMap<>();
    private static final Map<String, String> users = new HashMap<>();
    
    private static int transId = 1;
    private static int presId = 1;
    private static int medId = 1;
    
    static {
        users.put("admin", "password");
        
        addSamplePatient("John", "Doe", "555-1234", "john@example.com");
        addSamplePatient("Jane", "Smith", "555-5678", "jane@example.com");
        
        addSampleMedicine("Aspirin", "Analgesic", "Pain relief", 
                "Used for mild to moderate pain relief", 
                "1-2 tablets every 4-6 hours", 
                "Take with food or water", 
                "Allergy to salicylates", 
                "Stomach upset, bleeding", 
                "Not recommended during pregnancy", 
                "Use with caution in elderly", 
                "As needed for pain", 
                "Store at room temperature", 
                "https://www.example.com/aspirin");
        
        addSampleMedicine("Amoxicillin", "Antibiotic", "Bacterial infections", 
                "Used for various bacterial infections", 
                "500mg three times daily", 
                "Take with or without food", 
                "Allergy to penicillins", 
                "Diarrhea, nausea", 
                "Generally safe during pregnancy", 
                "Complete full course of treatment", 
                "7-10 days typically", 
                "Store at room temperature", 
                "https://www.example.com/amoxicillin");
    }
    
    private static void addSamplePatient(String firstName, String lastName, String phone, String email) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> patient = new HashMap<>();
        patient.put("id", id);
        patient.put("first_name", firstName);
        patient.put("last_name", lastName);
        patient.put("phone", phone);
        patient.put("email", email);
        patients.put(id, patient);
    }
    
    private static void addSampleMedicine(String name, String drugClass, String indication, 
            String indicationDesc, String dosage, String adminDesc, String contraindication, 
            String sideEffect, String pregnancyLactation, String precautions, String duration, 
            String storage, String link) {
        
        Map<String, Object> medicine = new HashMap<>();
        medicine.put("id", medId);
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
        medicines.put(medId++, medicine);
    }
    
    public static boolean validateLogin(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
    
    public static Map<String, Map<String, Object>> getPatients() {
        return patients;
    }
    
    public static Map<Integer, Map<String, Object>> getTranscriptions() {
        return transcriptions;
    }
    
    public static Map<Integer, Map<String, Object>> getPrescriptions() {
        return prescriptions;
    }
    
    public static Map<Integer, Map<String, Object>> getMedicines() {
        return medicines;
    }
    
    public static int getNextTransId() {
        return transId++;
    }
    
    public static int getNextPresId() {
        return presId++;
    }
}