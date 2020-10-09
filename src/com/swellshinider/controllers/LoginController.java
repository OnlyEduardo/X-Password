package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.util.PathsAndArchives;
import com.swellshinider.util.ProgramInfos;
import com.swellshinider.util.SceneName;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public PasswordField inputMasterPassField;
    public Text textToShowVersion;
    public Text textWarningPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        textToShowVersion.setText(ProgramInfos.VERSION);
    }

    public void enterLoginAction() throws IOException, NoSuchAlgorithmException {

        File _configFilePath = new File(PathsAndArchives.ROOT_PATH + "/"+ PathsAndArchives.CONFIG_NAME +".txt");

        BufferedReader reader = new BufferedReader(new FileReader(_configFilePath));

        if(stringToSha256(inputMasterPassField.getText()).equals(reader.readLine())){
            textWarningPassword.setText("");
            Main.instance.usingMaster = stringToSha256(inputMasterPassField.getText());
            MainController.instance.updateRegister();
            Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
        }

        textWarningPassword.setText("Senha mestra inválida");
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
