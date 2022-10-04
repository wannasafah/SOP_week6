package com.example.week6.pojo;

import java.util.*;

public class Wizards {
    private List<Wizard> model = new ArrayList<>();
    public Wizards(){
    }

    public Wizards(List<Wizard> model) {
        this.model = model;
    }

    public List<Wizard> getModel() {
        return model;
    }

    public void setModel(List<Wizard> model) {
        this.model = model;
    }
}
