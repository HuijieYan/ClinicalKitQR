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
import team7.demo.issue.models.Issue;
import team7.demo.issue.repositories.IssueRepository;
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
    private final IssueRepository issueRepository;
    @Autowired
    public EquipmentService(EquipmentRepository repository,IssueRepository issueRepository){
        this.repository = repository;
        this.issueRepository = issueRepository;
    }

    public void save(Equipment equipment){
        if (equipment.getEquipmentId()>=Constant.MAX_EQUIPMENT){
        //we only stores 2^36 equipments for readability of the string in qr code
            return;
        }
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
        int width = 300;
        int height = 300;
        Equipment equipment = this.get(id);
        if (equipment==null){
            return null;
        }
        String url = Constant.URL + "/equipment/id="+Long.toString(id);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return addText(image,id);
    }

    public BufferedImage addText(BufferedImage image,long id){
        StringBuffer buffer = new StringBuffer(Long.toHexString(id));
        int zeros = 9-buffer.length();
        for (int i =0;i<zeros;i++){
            buffer.insert(0,"0");
        }
        int[] position = {3,7};
        int height = image.getHeight();
        for (int pos:position){
            buffer.insert(pos,"-");
        }
        String text = buffer.toString();

        BufferedImage outputImage = new BufferedImage(image.getWidth(), height + 40, BufferedImage.TYPE_INT_ARGB);
        Graphics g = outputImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.setFont(new Font("Century Schoolbook", Font.BOLD, 20));
        Color textColor = Color.BLACK;
        g.setColor(textColor);
        FontMetrics fm = g.getFontMetrics();
        int startingYposition = height - 10;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        g.setFont(new Font("Neue Haas Grotesk Text Pro", Font.BOLD, 14));
        fm = g.getFontMetrics();
        text ="ENTER THE CODE ABOVE IF YOU";
        startingYposition += 18;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        text ="CANNOT SCAN THE QR CODE";
        startingYposition += 15;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        g.setFont(new Font("Neue Haas Grotesk Text Pro", Font.BOLD, 16));
        fm = g.getFontMetrics();
        text ="EQUIPMENT EDUCATION QR CODE";
        startingYposition = 23;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        return outputImage;
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
        for (Issue issue:repository.findById(id).getIssueList()){
            issueRepository.deleteById(issue.getIssueId());
        }
        repository.deleteById(id);
    }
}
