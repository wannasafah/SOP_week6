package com.example.week6.view;

import com.example.week6.pojo.Wizard;
import com.example.week6.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Route(value = "/mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField ioname;
    private NumberField currency;
    private Button previous,create,update,del,next;
    private RadioButtonGroup gender;
    private ComboBox position,school,house;
    private Wizard wizard;
    private int page = -1;
    public MainWizardView(){
        ioname = new TextField();
        ioname.setPlaceholder("Fullname");
        gender = new RadioButtonGroup();
        gender.setLabel("Gender :");
        gender.setItems("Male","Female");
        position = new ComboBox<>();
        position.setPlaceholder("Position");
        position.setItems("Student","Teacher");
        currency = new NumberField();
        currency.setLabel("Dollars");
        currency.setPrefixComponent(new Span("$"));
        school = new ComboBox<>();
        school.setPlaceholder("School");
        school.setItems("Hogwarts","Beauxbatons", "Durmstrang");
        house = new ComboBox<>();
        house.setItems("Gryffindor","Ravenclaw","Hufflepuff","Slyther");
        house.setPlaceholder("House");
        previous = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        del = new Button("Delete");
        next = new Button(">>");
        HorizontalLayout h1 = new HorizontalLayout();
        h1.add(previous,create,update,del,next);
        this.add(ioname,gender,position,currency,school,house,h1);
        create.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("name", ioname.getValue());
            if (gender.getValue().equals("Male")){
                formData.add("sex","m");
            }
            else{
                formData.add("sex","f");
            }
            formData.add("school", school.getValue()+"");
            formData.add("house", house.getValue()+"");
            formData.add("money", currency.getValue()+"");
            formData.add("position", position.getValue()+"");
            Wizards out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Wizards.class)
                    .block();
            Notification.show("Wizard has been created", 5000, Notification.Position.BOTTOM_START);
        });
        del.addClickListener(event ->{
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("id", wizard.get_id());
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(boolean.class)
                    .block();
            Wizards out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(Wizards.class)
                    .block();
            if (this.page > 0){
                this.page -= 1;
            }
            wizard = out.getModel().get(page);
            ioname.setValue(wizard.getName());
            if (wizard.getSex().equals("m")){
                gender.setValue("Male");
            }
            else if (wizard.getSex().equals("f")){
                gender.setValue("Female");
            }
            position.setValue(wizard.getPosition());
            currency.setValue(wizard.getMoney());
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());
            Notification.show("Wizard has been removed", 5000, Notification.Position.BOTTOM_START);
        });
        update.addClickListener(event ->{
            if (this.page >= 0) {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("old", wizard.get_id());
                formData.add("name", ioname.getValue());
                    if (gender.getValue().equals("Male")) {
                    formData.add("sex", "m");
                } else {
                    formData.add("sex", "f");
                }
                formData.add("school", school.getValue() + "");
                formData.add("house", house.getValue() + "");
                formData.add("money", currency.getValue()+"");
                formData.add("position", position.getValue() + "");
                String out = WebClient.create()
                        .post()
                        .uri("http://localhost:8080/updateWizard")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(BodyInserters.fromFormData(formData))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                System.out.println(out);
                Notification.show("Wizard has been updated", 5000, Notification.Position.BOTTOM_START);
            }
        });
        next.addClickListener(event ->{
                Wizards out = WebClient.create()
                        .get()
                        .uri("http://localhost:8080/wizards")
                        .retrieve()
                        .bodyToMono(Wizards.class)
                        .block();
                if (this.page < out.getModel().size()-1){
                    this.page += 1;
                }
                wizard = out.getModel().get(page);
                ioname.setValue(wizard.getName());
                if (wizard.getSex().equals("m")){
                    gender.setValue("Male");
                }
                else{
                    gender.setValue("Female");
                }
                position.setValue(wizard.getPosition());
                currency.setValue(wizard.getMoney());
                school.setValue(wizard. getSchool());
                house.setValue(wizard.getHouse());
        });
        previous.addClickListener(event ->{
            Wizards out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(Wizards.class)
                    .block();
            if (this.page > 0){
                this.page -= 1;
            }
            wizard = out.getModel().get(page);
            ioname.setValue(wizard.getName());
            if (wizard.getSex().equals("m")){
                gender.setValue("Male");
            }
            else{
                gender.setValue("Female");
            }
            position.setValue(wizard.getPosition());
            currency.setValue(wizard.getMoney());
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());
        });
    }
}