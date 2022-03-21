package ClinicalKitQR.mail.controllers;


import ClinicalKitQR.Constant;
import ClinicalKitQR.mail.models.Mail;
import ClinicalKitQR.mail.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ClinicalKitQR.equipment.models.Equipment;
import ClinicalKitQR.equipment.models.SentEquipment;
import ClinicalKitQR.equipment.services.EquipmentService;
import ClinicalKitQR.equipment.services.SentEquipmentService;
import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.services.UserGroupService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {Constant.FRONTEND_URL})
@RestController
@RequestMapping(Constant.API_PREFIX+"/mail")
public class MailController {
    private final MailService service;
    private final UserGroupService groupService;
    private final EquipmentService equipmentService;
    private final SentEquipmentService sentEquipmentService;

    @Autowired
    public MailController(MailService service,UserGroupService groupService,EquipmentService equipmentService,SentEquipmentService sentEquipmentService){
        this.service = service;
        this.groupService = groupService;
        this.sentEquipmentService = sentEquipmentService;
        this.equipmentService = equipmentService;
    }

    @PostMapping("/receiver")
    public List<List<Object>> getAllReceivedMails(@RequestParam("hospitalId") long hospitalId,@RequestParam("username") String username){
        List<List<Object>> result = new ArrayList<>();
        List<Mail> mails = service.getAllReceived(hospitalId,username);
        for (Mail mail:mails){
            List<Object> mailInfo = new ArrayList<>();
            mailInfo.add(mail);
            UserGroup sender = groupService.findByPK(mail.getSenderHospitalId(),mail.getSenderUsername());
            mailInfo.add(sender);
            result.add(mailInfo);
        }
        return result;
    }

    @PostMapping("/sender")
    public List<List<Object>> getAllSentMails(@RequestParam("hospitalId") long hospitalId,@RequestParam("username") String username){
        List<List<Object>> result = new ArrayList<>();
        List<Mail> mails = service.getAllSent(hospitalId,username);
        for (Mail mail:mails){
            List<Object> mailInfo = new ArrayList<>();
            mailInfo.add(mail);
            UserGroup sender = groupService.findByPK(mail.getSenderHospitalId(),mail.getSenderUsername());
            mailInfo.add(sender);
            result.add(mailInfo);
        }
        return result;
    }

    @PostMapping("/delete")
    public void delete(@RequestParam("id") String id,
                       @RequestParam("hospitalId")long hospitalId,@RequestParam("username")String username){
        if (groupService.findByPK(hospitalId,username)==null){
            return;
        }
        Mail mail = service.getByPK(id);
        if (mail == null) {
            return;
        }
        service.delete(id);
    }

    @PostMapping("/sending")
    public boolean send(@RequestParam("senderHospitalId") long senderHospitalId,@RequestParam("senderUsername") String senderUsername,
                        @RequestParam("receivers") List<String>receivers,
                        @RequestParam("title")String title,@RequestParam("description")String description,@RequestParam("time")String timeString,
                        @RequestParam("equipmentIds")List<String>ids){

        try{
            UserGroup sender = groupService.findByPK(senderHospitalId,senderUsername);
            if (sender==null||receivers.size()==0||ids.size()==0){
                return false;
            }

            ZonedDateTime zonedTime = ZonedDateTime.parse(timeString, DateTimeFormatter.RFC_1123_DATE_TIME);
            LocalDateTime time = zonedTime.toLocalDateTime();

            for (int i=0;i<receivers.size();i+=2){
            //because right now when frontend passes 2d array as receivers' data, the backend will just recognise it as a 1d array
            //we have to handle this as a 1d array where each data is two consecutive items in the array
                long hospitalId = Long.parseLong(receivers.get(i));
                String username = receivers.get(i+1);
                UserGroup receiver = groupService.findByPK(hospitalId,username);

                if (receiver==null){
                    return false;
                }

                Mail mail = new Mail(senderHospitalId,senderUsername,time,title,description,receiver);
                boolean result = addEquipments(mail,ids);
                if (!result){
                    return false;
                }
                service.save(mail);
            }

            Mail mail = new Mail(senderHospitalId,senderUsername,time,title,description,null);
            addEquipments(mail,ids);
            service.save(mail);
            //save a copy for the sender
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean addEquipments(Mail mail,List<String> ids){
        for(String id:ids){
            Equipment equipment = equipmentService.get(Long.parseLong(id));
            if (equipment==null){
                return false;
            }
            SentEquipment sentEquipment = new SentEquipment(equipment);
            mail.addEquipment(sentEquipment);
        }
        return true;
    }


}
