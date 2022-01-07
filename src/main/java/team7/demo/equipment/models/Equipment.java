package team7.demo.equipment.models;

import javax.imageio.ImageIO;
import javax.persistence.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import team7.demo.Constant;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import team7.demo.login.models.Hospital;

@Entity(name = "Equipment")
@Table(name = "Equipment")
@IdClass(EquipmentPrimaryKey.class)
public class Equipment {
    @Id
    @SequenceGenerator(
            name = "EquipmentidSeqGen",
            sequenceName = "EquipmentidSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator = "EquipmentidSeqGen")
    @Column(name = "equipmentId",columnDefinition = "bigint not null")
    private long equipmentId;

    @Id
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "hospitalId",
            referencedColumnName = "hospitalId",
            columnDefinition = "bigint not null"
    )
    public Hospital hospitalId;


    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Equipment(){}

    public Equipment(String name,String content){
        this.name = name;
        this.content = content;
    }

    public Equipment(String name,String content,Hospital hospitalId){
        this.name = name;
        this.content = content;
        this.hospitalId = hospitalId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHospital(Hospital hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public Hospital getHospitalId() {
        return hospitalId;
    }
}
