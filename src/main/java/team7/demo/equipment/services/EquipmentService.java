package team7.demo.equipment.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.Constant;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.repositories.EquipmentRepository;

import java.awt.image.BufferedImage;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository repository;

    @Autowired
    public EquipmentService(EquipmentRepository repository){
        this.repository = repository;
    }

    public void save(Equipment equipment){
        repository.save(equipment);
    }

    public Equipment get(long id){
        return repository.findById(id);
    }

    public BufferedImage generateQRCodeImage(long id) throws Exception {
        Equipment equipment = this.get(id);
        if (equipment==null){
            return null;
        }
        String url = Constant.URL + "/equipment/id="+Long.toString(id);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public List<Equipment> getAll(){
        return repository.findAll();
    }

    public void delete(long id){
        repository.deleteById(id);
    }
}
