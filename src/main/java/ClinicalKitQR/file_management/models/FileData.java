package ClinicalKitQR.file_management.models;

import ClinicalKitQR.Constant;

import javax.persistence.*;
import java.util.UUID;

/**
 * Represents uploaded file that are locally stored in the database,
 * the actual file is stored under /uploadedFiles
 *
 * @value id a uuid that identifies the file
 * @value name the name of the file
 * @value extension file extension i.e. png, mp4
 */
@Table(name = "files")
@Entity(name = "FileData")
public class FileData {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    public FileData(){}

    public FileData(String name,String extension){
        id = UUID.randomUUID().toString()+extension;
        this.name = name;
        this.extension = extension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return Constant.uploadedFileRoot.toString()+"/"+id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getExtension() {
        return extension;
    }
}
