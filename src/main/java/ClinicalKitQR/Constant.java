package ClinicalKitQR;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constant {
    public static final String URL = "http://13.87.78.62:8080";
    public static final long MAX_EQUIPMENT = (long)Math.pow(16,9);
    public static final Path uploadedFileRoot = Paths.get("uploadedFiles");
    public static final char[] substitutionCharacters = {'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3',
    '4','5','6','7','8','9','-'};
    public static final String[] types = {"A","B","C"};
    public static final String[] categories = {"Adult","Neonatal","Children"};
    public static final String FRONTEND_URL = "http://localhost:3000";
    public static final String VMFRONTEND_URL = "http://13.87.78.62:3000";
}
