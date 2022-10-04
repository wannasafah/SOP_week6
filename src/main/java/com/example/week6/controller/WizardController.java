package com.example.week6.controller;

import com.example.week6.pojo.Wizard;
import com.example.week6.pojo.Wizards;
import com.example.week6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;
    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public Wizards getWizard(){
        Wizards wizards = new Wizards(wizardService.retrieveWizard());
        return wizards;
    }
    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public Wizard addWizard(@RequestBody MultiValueMap<String , String> n){
        Map<String, String> d = n.toSingleValueMap();
        Wizard w = wizardService.createWizard(
                new Wizard(null,
                        d.get("sex"),
                        d.get("name"),
                        d.get("school"),
                        d.get("house"),
                        Double.parseDouble(d.get("money")),
                        d.get("position")));
        return w;
    }
    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public boolean updateWizard(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        Wizard wz = wizardService.retrieveWizardByPeople(d.get("old"));
        if (wz != null){
            wizardService.updateWizard(new Wizard(wz.get_id(),
                    d.get("sex"),
                    d.get("name"),
                    d.get("school"),
                    d.get("house"),
                    Double.parseDouble(d.get("money")),
                    d.get("position")));
            return true;
        }
        else{
            return false;
        }
    }
    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public boolean deleteWizard(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        wizardService.deleteWizardById(d.get("id"));
        return true;
    }
}
