package team7.demo.mail.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.equipment.models.Equipment;
import team7.demo.equipment.services.EquipmentService;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.UserGroupService;
import team7.demo.mail.models.Mail;
import team7.demo.mail.services.MailService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup"})
@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailService service;
    private final UserGroupService groupService;
    private final EquipmentService equipmentService;

    @Autowired
    public MailController(MailService service,UserGroupService groupService,EquipmentService equipmentService){
        this.service = service;
        this.groupService = groupService;
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
            Mail mail = new Mail(senderHospitalId,senderUsername,time,title,description);
            for (int i=0;i<receivers.size();i+=2){
            //because right now when frontend passes 2d array as receivers' data, the backend will just recognise it as a 1d array
            //we have to handle this as a 1d array where each data is two consecutive items in the array
                long id = Long.parseLong(receivers.get(i));
                String username = receivers.get(i+1);
                UserGroup receiver = groupService.findByPK(id,username);
                if (receiver==null){
                    return false;
                }
                mail.addReceiver(receiver);
            }
            for(String id:ids){
                Equipment equipment = equipmentService.get(Long.parseLong(id));
                if (equipment==null){
                    return false;
                }
                mail.addEquipment(equipment);
            }
            service.save(mail);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
