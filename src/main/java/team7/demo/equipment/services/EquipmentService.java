package team7.demo.equipment.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.Constant;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.repositories.EquipmentRepository;
import team7.demo.login.services.HospitalService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository repository;
    @Autowired
    public EquipmentService(EquipmentRepository repository){
        this.repository = repository;
    }

    public void save(Equipment equipment){
        equipment.getHospitalId().addEquipment(equipment);
        repository.save(equipment);
    }

    public Equipment get(long id){
        return repository.findById(id);
    }

    public List<Equipment> getAllByHospital(long id){
        return repository.findByHospital(id);
    }

    public List<Equipment> getAllByTrust(long id){
        return repository.findByTrust(id);
    }

    public BufferedImage generateQRCodeImage(long id) throws Exception {
        Equipment equipment = this.get(id);
        if (equipment==null){
            return null;
        }
        String url = Constant.URL + "/equipment/id="+Long.toString(id);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return image;
    }

    /*
    public byte[] generateQRCode(String data, Integer width, Integer height, String[] text) throws Exception{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            //        If text is needed to display
            if (text.length > 0) {
                int totalTextLineToadd = text.length;
                InputStream in = new ByteArrayInputStream(pngData);
                BufferedImage image = ImageIO.read(in);

                BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight() + 25 * totalTextLineToadd, BufferedImage.TYPE_INT_ARGB);
                Graphics g = outputImage.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                g.setFont(new Font("Arial Black", Font.BOLD, 12));
                Color textColor = Color.BLACK;
                g.setColor(textColor);
                FontMetrics fm = g.getFontMetrics();
                int startingYposition = height + 5;
                for(String displayText : text) {
                    g.drawString(displayText, (outputImage.getWidth() / 2)   - (fm.stringWidth(displayText) / 2), startingYposition);
                    startingYposition += 20;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(outputImage, "PNG", baos);
                baos.flush();
                pngData = baos.toByteArray();
                baos.close();
            }

            return pngData;

    }
    */

    public List<Equipment> getAll(){
        return repository.findAll();
    }

    public void delete(long id){
        repository.deleteById(id);
    }
}
