package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.main.Cryptography;
import com.swellshinider.util.ErrorManager;
import com.swellshinider.util.PathsAndArchives;
import com.swellshinider.util.ProgramInfos;
import com.swellshinider.util.SceneName;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangeMasterPassController {
    public TextField actualPass;
    public PasswordField newPass;
    public PasswordField confirmNewPass;
    public Text warningMessage1;
    public Text warningMessage2;

    public void changeMasterPassword(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        if(actionEvent.getEventType() != ActionEvent.ACTION)
            return;

        File _configFilePath = new File(PathsAndArchives.ROOT_PATH + "/"+ PathsAndArchives.CONFIG_NAME +".txt");

        BufferedReader reader = new BufferedReader(new FileReader(_configFilePath));
        String savedPass = reader.readLine();
        reader.close();

        if(!stringToSha256(actualPass.getText()).equals(savedPass)){
            warningMessage1.setText("Senha mestra inválida");
            return;
        }
        warningMessage1.setText("");

        if(!newPass.getText().equals(confirmNewPass.getText())){
            warningMessage2.setText("Senhas mestras não coincidem");
            return;
        }

        if(newPass.getText().length() < 8){
            warningMessage2.setText("Nova senha muito fraca");
            return;
        }

        String oldPass = stringToSha256(actualPass.getText());
        String SEPARATOR = ProgramInfos.SEPARATOR;
        String newMasterPassword = newPass.getText();

        BufferedWriter writer = new BufferedWriter(new FileWriter(_configFilePath));
        writer.write(stringToSha256(newMasterPassword));
        Main.instance.usingMaster = stringToSha256(newMasterPassword);
        writer.close();

        // Update masterpassword in registers
        File dirOfRegister = new File(PathsAndArchives.PATH_TO_REGISTERS);

        File[] arqs = dirOfRegister.listFiles();

        if(arqs == null)
            return;

        for (File arq : arqs) {
            try {
                BufferedReader breader = new BufferedReader(new FileReader(arq));

                String tempUsername;
                String tempEmail;
                String tempPassword;
                String writeInMasterPass;

                try {

                    String[] l = breader.readLine().split(SEPARATOR);
                    breader.close();

                    tempUsername = Cryptography.decrypt(l[0]);
                    tempEmail = Cryptography.decrypt(l[1]);
                    tempPassword = Cryptography.decrypt(l[2]);
                    writeInMasterPass = Cryptography.decrypt(l[3]);

                    assert writeInMasterPass != null;
                    if(!writeInMasterPass.equals(oldPass))
                        continue;

                } catch (Exception ignored){
                    continue;
                }

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(arq));
                bufferedWriter.write(
                        Cryptography.encrypt(tempUsername) + SEPARATOR +
                                Cryptography.encrypt(tempEmail) + SEPARATOR +
                                Cryptography.encrypt(tempPassword) + SEPARATOR +
                                Cryptography.encrypt(Main.instance.usingMaster));
                bufferedWriter.close();

            } catch (IOException e) {
                ErrorManager.showErrorManager("Erro fatal desconhecido",
                        "ChangeMasterPassController",
                        "changeMasterPassword",
                        e,
                        ErrorManager.FALTAL_SEVERITY);
            }
        }

        actualPass.setText("");
        newPass.setText("");
        confirmNewPass.setText("");
        warningMessage1.setText(" ");
        warningMessage2.setText(" ");
        MainController.instance.updateRegister();
        Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
    }

    public void goBackToMainScene(ActionEvent actionEvent) {
        if(actionEvent.getEventType() != ActionEvent.ACTION)
            return;

        Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
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
