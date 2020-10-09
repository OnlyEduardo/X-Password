package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.main.Cryptography;
import com.swellshinider.util.*;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateRegisterController {

    public Text warningText;
    public TextField userText;
    public TextField emailText;
    public TextField passwordText;
    public TextField registerText;

    public void goBackToMainScene(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        registerText.setText("");
        userText.setText("");
        emailText.setText("");
        passwordText.setText("");
        Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
    }

    public void createNewRegister(ActionEvent actionEvent) throws IOException {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        String registerName;

        registerName = registerText.getText().replace(" ", "_");

        if(registerName.equals(""))
            return;

        File newReg = new File(PathsAndArchives.PATH_TO_REGISTERS + "/"
                + registerName + ".txt");

        if(newReg.exists()){
            PopUp.newPopUpToGo("Esse Registro já existe, crie um com outro nome", SceneName.CREATE_REGISTER_SCENE);
            return;
        }

        if(userText.getText().equals("")){
            warningText.setText("Usuário não pode ser vazio");
            return;
        }

        if(emailText.getText().equals("")){
            warningText.setText("Email não pode ser vazio");
            return;
        }

        if(passwordText.getText().equals("")){
            warningText.setText("Senha não pode ser vazia");
            return;
        }

        if(!newReg.createNewFile()){
            ErrorManager.showErrorManager("Erro ao criar novo registro",
                    "MainController",
                    "createNewRegister",
                    ErrorManager.GRAVE_SEVERITY);
            return;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(newReg));
        String SEPARATOR = ProgramInfos.SEPARATOR;
        writer.write(
                Cryptography.encrypt(userText.getText()) + SEPARATOR +
                        Cryptography.encrypt(emailText.getText()) + SEPARATOR +
                        Cryptography.encrypt(passwordText.getText()) + SEPARATOR +
                        Cryptography.encrypt(Main.instance.usingMaster));
        writer.close();

        MainController.instance.updateRegister();

        warningText.setText("");
        registerText.setText("");
        userText.setText("");
        emailText.setText("");
        passwordText.setText("");

        Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
    }
}
