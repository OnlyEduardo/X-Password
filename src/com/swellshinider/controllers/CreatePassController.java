package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreatePassController implements Initializable {

    public Text textWarningPassword;
    public Text textToShowVersion;
    public PasswordField inputPass;
    public PasswordField inputPassTest;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        textToShowVersion.setText(ProgramInfos.VERSION);
    }

    public void createMasterPassword(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        if(!inputPass.getText().equals(inputPassTest.getText())){
            textWarningPassword.setText("Senhas não coincidem.");
            return;
        }

        if(inputPass.getText().length() < 8){
            textWarningPassword.setText("Senha mestra muito fraca.");
            return;
        }

        textWarningPassword.setText("");

        // Senhas são boas e coincidem

        File fileOfRootPath = new File(PathsAndArchives.ROOT_PATH);

        if(!fileOfRootPath.exists()){
            if(!fileOfRootPath.mkdirs()){
                ErrorManager.showErrorManager("Falha ao criar - > " + fileOfRootPath.getAbsolutePath(),
                        "CreatePassController",
                        "createMasterPassword",
                        ErrorManager.GRAVE_SEVERITY);
            }
        }

        File _configFilePath = new File(PathsAndArchives.ROOT_PATH + "/"+ PathsAndArchives.CONFIG_NAME +".txt");

        if(!_configFilePath.exists()){
            if(_configFilePath.createNewFile()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(_configFilePath));
                writer.write(stringToSha256(inputPass.getText()));
                writer.close();

                verifyIfExistRegister();

                Main.instance.usingMaster = stringToSha256(inputPass.getText());
                MainController.instance.updateRegister();
                PopUp.newPopUpToGo("Senha mestra criada com sucesso!", SceneName.MAIN_SCENE);
            } else {
                ErrorManager.showErrorManager("Falha ao criar - > " + fileOfRootPath.getAbsolutePath(),
                        "CreatePassController",
                        "createMasterPassword",
                        ErrorManager.GRAVE_SEVERITY);
            }
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(_configFilePath));

            if(reader.readLine() == null){
                BufferedWriter writer = new BufferedWriter(new FileWriter(_configFilePath));
                writer.write(stringToSha256(inputPass.getText()));
                writer.close();
            }

            reader.close();
        }
    }

    private void verifyIfExistRegister() {
        File pathToRegister = new File(PathsAndArchives.PATH_TO_REGISTERS);

        if(pathToRegister.exists()){

            if(pathToRegister.listFiles() == null)
                return;

            if(Objects.requireNonNull(pathToRegister.listFiles()).length <= 0)
                return;

            for (File filesInReg: Objects.requireNonNull(pathToRegister.listFiles())){
                if(!filesInReg.delete()){
                    ErrorManager.showErrorManager(
                            "Erro mortal, reinicie o programa para tentar solucionar o problema",
                            "CreatePassController",
                            "verifyIfExistRegister",
                            ErrorManager.FALTAL_SEVERITY
                    );
                    System.exit(0);
                }
            }
            MainController.instance.updateRegister();
        }
    }

    private String stringToSha256(String text) throws NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                result.append('0');
            result.append(hex);
        }

        return result.toString();
    }
}
