package edu.universidad.repo;

import java.util.HashMap;
import java.util.Map;

public class AdministradorRepository {
    private static AdministradorRepository instance;
    private Map<String,String> credentials = new HashMap<>();
    private AdministradorRepository() { credentials.put("admin","admin"); }
    public static synchronized AdministradorRepository getInstance() {
        if (instance==null) instance = new AdministradorRepository();
        return instance;
    }
    public boolean authenticate(String user, String pass) {
        return pass!=null && pass.equals(credentials.get(user));
    }
}
