package ClinicalKitQR.equipment.services;

import ClinicalKitQR.Constant;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.EquipmentModel;
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
    @Autowired
    public EquipmentService(EquipmentRepository repository){
        this.repository = repository;
    }

    public void save(Equipment equipment){
        if (equipment.getEquipmentId()>= Constant.MAX_EQUIPMENT){
            //we only stores 2^36 equipments for readability of the string in qr code
            return;
        }
        equipment.getHospitalId().addEquipment(equipment);
        equipment.getModel().setEquipment(equipment);
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

    public void updateModel(long id,EquipmentModel model) throws Exception{
        repository.updateModel(id,model);
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
        String url = Constant.FRONTEND_URL + "/viewEquipment/id=" + id;
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return addText(image,generateEquipmentCode(equipment),equipment);
    }


    /**
     * Adding strings:
     *  -id hex string
     *  -instructions
     *  -title
     * to the image
     */
    public BufferedImage addText(BufferedImage image,String text,Equipment equipment){
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(image.getWidth(), height + 80, BufferedImage.TYPE_INT_ARGB);
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
        text = "MAKE: "+equipment.getModel().getManufacturer().getManufacturerName();
        startingYposition += 15;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        text = "MODEL: "+equipment.getModel().getModelName();
        startingYposition += 15;
        g.drawString(text, (outputImage.getWidth() / 2)   - (fm.stringWidth(text) / 2), startingYposition);
        text = "NAME: "+equipment.getName();
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
        return category+getTypeAbbrev(equipment.getType())+Integer.toString(equipment.getDate().getYear()).substring(2)+" ";
    }

    private String getTypeAbbrev(String type){
        String[] types = Constant.types;
        for (int i =0;i< types.length;i++){
            if(type.equals(types[i])){
                return Constant.typesAbbreviation[i];
            }
        }
        return "";
    }


    public List<Equipment> getAll(){
        return repository.findAll();
    }

    public void delete(long id){
        repository.deleteById(id);
    }


    /**
     * Searching for equipment with matched patient demographic, clinical system, manufacturer,
     * model and name given by the parameters
     *
     * @param group the user that is currently using search function
     * @param type selected type of the equipment
     * @param category selected category of the equipment
     * @param text searching name
     * @param manufacturerName the name of the manufacturer
     * @param modelName the name of the model
     * @return the list of equipments found using search term, type and categories
     *
     * These strings can be empty
     */
    public List<Equipment> search(UserGroup group, String type, String category, String text,String manufacturerName,String modelName){
        String txt = text.replaceAll(" ","").toLowerCase();
        List<Equipment> equipments = new ArrayList<>();
        try {
            long id = Long.parseLong(txt.replaceAll("-",""),16);
            Equipment equipment = repository.findById(id);
            if (equipment!=null){
                equipments.add(equipment);
            }
            //try to find the equipment with the input id
        }catch (Exception e){
            equipments = new ArrayList<>();
        }


        List<Equipment> result = new ArrayList<>();
        if (group.getHospitalId().getHospitalName().equals("Trust Admin")){
            result = repository.searchByTrust(group.getHospitalId().getTrust().getTrustId(),type,category,txt,manufacturerName,modelName);
        }else{
            result = repository.searchByHospital(group.getHospitalId().getHospitalId(),type,category,txt,manufacturerName,modelName);
        }
        for (Equipment equipment:result){
            if (!equipments.contains(equipment)){
                equipments.add(equipment);
            }
        }
        return equipments;
    }


    public String[] getTypes(){
        return Constant.types;
    }

    public String[] getCategories(){
        return Constant.categories;
    }
}