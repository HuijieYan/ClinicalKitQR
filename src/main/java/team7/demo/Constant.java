package team7.demo;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constant {
    public static String URL = "http://localhost:8080";
    public static long MAX_EQUIPMENT = (long)Math.pow(16,9);
    public static Path uploadedFileRoot = Paths.get("uploadedFiles");
    public static char[] substitutionCharacters = {'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3',
    '4','5','6','7','8','9','-'};
    public static String[] types = {"A","B","C"};
    public static String[] categories = {"Adult","Neonatal","Children"};
}
