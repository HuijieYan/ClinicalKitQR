package ClinicalKitQR.file_management.models;

import ClinicalKitQR.Constant;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "files")
@Entity(name = "FileData")
public class FileData {
    @Id
    private String id;
    //using uuid since there may be large amount of files uploaded

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;
    //file extension ie. png, mp4

    public FileData(){}

    public FileData(String name,String extension){
        this.name = name;
        this.extension = extension;
        this.id = UUID.randomUUID().toString()+extension;
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
