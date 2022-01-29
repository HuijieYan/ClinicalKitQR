package team7.demo;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constant {
    public static String URL = "http://localhost:8080";
    public static long MAX_EQUIPMENT = (long)Math.pow(4,9);
    public static Path uploadedFileRoot = Paths.get("uploadedFiles");
}
