package team7.demo.mail.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team7.demo.login.models.UserGroup;
import team7.demo.login.services.UserGroupService;
import team7.demo.mail.models.Mail;
import team7.demo.mail.services.MailService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:3000/loginFail","http://localhost:3000/editUserGroup"})
@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailService service;
    private final UserGroupService groupService;

    @Autowired
    public MailController(MailService service,UserGroupService groupService){
        this.service = service;
        this.groupService = groupService;
    }

    @GetMapping("/hospitalId={hospitalId} username={username}")
    public List<List<Object>> getAllReceivedMails(@PathVariable long hospitalId,@PathVariable String username){
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

}
