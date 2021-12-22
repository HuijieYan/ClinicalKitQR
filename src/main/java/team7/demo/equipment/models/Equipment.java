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

@Entity(name = "Equipment")
@Table(name = "Equipment")
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

    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Equipment(){}

    public Equipment(String name,String content){
        this.name = name;
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
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
}
