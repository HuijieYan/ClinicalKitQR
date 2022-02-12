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
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.HospitalService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    public void update(long id,String name,String content,String type,String category) throws Exception{
        repository.update(id,name,name.toLowerCase(),content,type,category);
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
        return addText(image,generateEquipmentCode(equipment));
    }

    public BufferedImage addText(BufferedImage image,String text){
        int height = image.getHeight();

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
        text ="ENTER THE 9-DIGIT CODE ABOVE IF";
        startingYposition += 18;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        text ="YOU CANNOT SCAN THE QR CODE";
        startingYposition += 15;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        g.setFont(new Font("Neue Haas Grotesk Text Pro", Font.BOLD, 16));
        fm = g.getFontMetrics();
        text ="EQUIPMENT EDUCATION QR CODE";
        startingYposition = 23;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        return outputImage;
    }

    private String generateEquipmentCode(Equipment equipment){
        StringBuffer buffer = new StringBuffer(Long.toHexString(equipment.getEquipmentId()));
        int zeros = 9-buffer.length();
        for (int i =0;i<zeros;i++){
            buffer.insert(0,"0");
        }
        int[] position = {3,7};

        for (int pos:position){
            buffer.insert(pos,"-");
        }
        buffer.insert(0,getTypeAndCategoryStr(equipment));
        String text = buffer.toString();
        return text;
    }

    private String getTypeAndCategoryStr(Equipment equipment){
        String type="";
        switch (equipment.getType()){
            case "Adult":
                type = "A";
                break;
            case "Neonatal":
                type = "N";
                break;
            case "Children":
                type = "C";
        }
        return type+equipment.getCategory()+Integer.toString(equipment.getDate().getYear()).substring(2)+" ";
    }


    public List<Equipment> getAll(){
        return repository.findAll();
    }

    public void delete(long id){
        for (Issue issue:repository.findById(id).getIssueList()){
            issueRepository.deleteById(issue.getIssueId());
        }
        repository.deleteById(id);
    }

    public List<Equipment> search(UserGroup group, String type, String category, String text){
        String txt = text.toLowerCase();
        List<Equipment> equipments = new ArrayList<>();
        try {
            String s = txt.replaceAll("-","");
            s = s.replaceAll(" ","");
            long id = Long.parseLong(s,16);
            equipments.add(repository.findById(id));
            //try to find the equipment with the input id
        }catch (Exception e){
            equipments = new ArrayList<>();
        }

        List<String> possibleStrings = generatePossibleOutcomes(txt);
        //covert the search text to lower for searching

        for (String str:possibleStrings){
            List<Equipment> result = new ArrayList<>();
            if (group.getHospitalId().getHospitalName().equals("Trust Admin")){
                result = trustAdminCallSearchFunction(group.getHospitalId().getTrust().getTrustId(),type,category,str);
            }else{
                result = normalUserAndHospitalAdminCallSearchFunction(group.getHospitalId().getHospitalId(),type,category,str);
            }
            for (Equipment equipment:result){
                if (!equipments.contains(equipment)){
                    equipments.add(equipment);
                }
            }
        }
        return equipments;
    }

    private List<Equipment> normalUserAndHospitalAdminCallSearchFunction(long hospitalId,String type, String category, String text){
        if (!category.equals("") && type.equals("")){
            return repository.findByCategoryAndNameAndHospital(hospitalId,category,text);
        }else if (category.equals("") && !type.equals("")){
            return repository.findByTypeAndNameAndHospital(hospitalId,type,text);
        }else if (!category.equals("") && !type.equals("")){
            return repository.findByCategoryAndTypeAndNameAndHospital(hospitalId, type, category, text);
        }else{
            return repository.findByNameAndHospital(hospitalId,text);
        }
    }

    private List<Equipment> trustAdminCallSearchFunction(long trustId,String type, String category, String text){
        if (!category.equals("") && type.equals("")){
            return repository.findByCategoryAndNameAndTrust(trustId,category,text);
        }else if (category.equals("") && !type.equals("")){
            return repository.findByTypeAndNameAndTrust(trustId,type,text);
        }else if (!category.equals("") && !type.equals("")){
            return repository.findByCategoryAndTypeAndNameAndTrust(trustId, type, category, text);
        }else{
            return repository.findByNameAndTrust(trustId,text);
        }
    }

    private List<String> generatePossibleOutcomes(String str){
    //for this function we only generates possible outcomes that are of Levenshtein distance 1
        List<String> possibleOutcomes = new ArrayList<>();
        char[] subCharacters = Constant.substitutionCharacters;
        for (int i =0;i<str.length();i++){
            StringBuilder deleteAtPos = new StringBuilder(str);
            deleteAtPos.deleteCharAt(i);
            possibleOutcomes.add(deleteAtPos.toString());
            for (char ch:subCharacters){
                StringBuilder insertAtPos = new StringBuilder(str);
                insertAtPos.insert(i,ch);
                possibleOutcomes.add(insertAtPos.toString());
                StringBuilder subAtPos = new StringBuilder(str);
                subAtPos.setCharAt(i,ch);
                possibleOutcomes.add(subAtPos.toString());
                if (i==str.length()-1){
                    StringBuilder appendAtPos = new StringBuilder(str);
                    appendAtPos.append(ch);
                    possibleOutcomes.add((appendAtPos.toString()));
                }
            }
        }
        return possibleOutcomes;
        //this algorithm is of time complexity O(n)
    }

    public String[] getTypes(){
        return Constant.types;
    }

    public String[] getCategories(){
        return Constant.categories;
    }
}
