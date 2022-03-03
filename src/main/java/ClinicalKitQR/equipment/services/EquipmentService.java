package ClinicalKitQR.equipment.services;

import ClinicalKitQR.Constant;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.issue.models.Issue;
import ClinicalKitQR.issue.repositories.IssueRepository;
import ClinicalKitQR.login.models.UserGroup;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ClinicalKitQR.equipment.repositories.EquipmentRepository;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        if (equipment.getEquipmentId()>= Constant.MAX_EQUIPMENT){
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

    /**
     * Generates a QR code png image of the given equipment
     */
    public BufferedImage generateQRCodeImage(long id) throws Exception {
        int width = 300;
        int height = 300;
        Equipment equipment = this.get(id);
        if (equipment==null){
            return null;
        }
        String url = Constant.FRONTEND_URL + "/viewEquipment/id="+Long.toString(id);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return addText(image,generateEquipmentCode(equipment));
    }


    /**
     * Adding strings:
     *  -id hex string
     *  -instructions
     *  -title
     * to the image
     */
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

    /**
     * Returns a string with first letter representing an equipment's type, second letter represent
     * the category and last two digits representing the year of this equipment was generated
     */
    private String getTypeAndCategoryStr(Equipment equipment){

        String category="";
        switch (equipment.getCategory()){
            case "Adult":
                category = "A";
                break;
            case "Neonatal":
                category = "N";
                break;
            case "Children":
                category = "C";
        }
        return category+equipment.getType()+Integer.toString(equipment.getDate().getYear()).substring(2)+" ";
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


    /**
     * Searching for equipment with matched type, category and name given by the
     * parameters
     *
     * @param group the user that is currently using search function
     * @param type selected type of the equipment
     * @param category selected category of the equipment
     * @param text searching name
     * @return the list of equipments found using search term, type and categories
     */
    public List<Equipment> search(UserGroup group, String type, String category, String text){
        String txt = text.toLowerCase();
        List<Equipment> equipments = new ArrayList<>();
        try {
            String s = txt.replaceAll("-","");
            s = s.replaceAll(" ","");
            long id = Long.parseLong(s,16);
            Equipment equipment = repository.findById(id);
            if (equipment!=null){
                equipments.add(equipment);
            }

            //try to find the equipment with the input id
        }catch (Exception e){
            equipments = new ArrayList<>();
        }

        List<String> possibleStrings = generatePossibleOutcomes(txt);
        //convert the search text to lower for searching

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


    /**
     * Generates possible search terms that are of Levenshtein distance 1
     * https://en.wikipedia.org/wiki/Levenshtein_distance
     *
     * @param str the search term entered by the user
     * @return a list of possible search terms
     */
    private List<String> generatePossibleOutcomes(String str){
        List<String> possibleOutcomes = new ArrayList<>();
        possibleOutcomes.add(str);
        char[] subCharacters = Constant.substitutionCharacters;
        for (int i =0;i<str.length();i++){
            StringBuilder deleteAtPos = new StringBuilder(str);
            deleteAtPos.deleteCharAt(i);
            if(deleteAtPos.length()>0) {
                possibleOutcomes.add(deleteAtPos.toString());
            }
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

    public long findNextCopyIndex(){
        Equipment equipment = repository.findTop();
        long id = equipment.getEquipmentId();
        if (id> Constant.MAX_EQUIPMENT){
            return id+1;
        }else{
            return Constant.MAX_EQUIPMENT;
        }
    }

    public void updateId(long id,long newId){
        repository.updateId(id,newId);
    }
}