package team7.demo.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.demo.login.models.Hospital;
import team7.demo.login.services.HospitalService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService service;

    @Autowired
    public HospitalController(HospitalService service){
        this.service = service;
    }

    @GetMapping
    public List<String> getHospitals(){
        List<Hospital> hospitals = service.getAll();
        List<String> list = new ArrayList<>();
        for (Hospital hospital:hospitals){
            list.add(hospital.toString());
        }
        return list;
    }
}
