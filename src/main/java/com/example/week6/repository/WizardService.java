    package com.example.week6.repository;

import com.example.week6.pojo.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public WizardService(WizardRepository repository) {
        this.repository = repository;
    }
    public List<Wizard> retrieveWizard(){
        return repository.findAll();
    }
    public Wizard retrieveWizardByPeople(String id){
        return repository.findByPeople(id);
    }
    public Wizard createWizard(Wizard wizard){
        return repository.save(wizard);
    }
    public boolean deleteWizardById(String id){
        try {
            repository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public int countWizard(){
        return (int)repository.count();
    }
    public Wizard updateWizard(Wizard wizard){
        return repository.save(wizard);
    }
}
