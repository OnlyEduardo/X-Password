package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.main.Cryptography;
import com.swellshinider.util.ErrorManager;
import com.swellshinider.util.ProgramInfos;
import com.swellshinider.util.SceneName;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditRegisterController implements Initializable {

    public Text editingRegisterTal;
    public TextField actualUser;
    public TextField actualEmail;
    public PasswordField actualPass;
    public TextField newUser;
    public TextField newEmail;
    public TextField newPass;
    public Button copyButton;

    private File editingFile;

    public static EditRegisterController instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { instance = this; }

    public void setParameters(File archive, String registerName,String user, String email, String pass){
        editingFile = archive;
        editingRegisterTal.setText("Editando: "+registerName);

        actualUser.setText(user);
        actualEmail.setText(email);
        actualPass.setText(pass);
        copyButton.setOnAction(actionEvent -> Main.instance.copyToClipboard(pass));
    }

    public void editAndSave(ActionEvent actionEvent) throws IOException {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        File archive = editingFile;

        if(archive.delete()){

            String user = newUser.getText().equals("") || newUser.getText() == null
                    ? actualUser.getText() : newUser.getText();

            String email = newEmail.getText().equals("") || newEmail.getText() == null
                    ? actualEmail.getText() : newEmail.getText();

            String pass = newPass.getText().equals("") || newPass.getText() == null
                    ? actualPass.getText() : newPass.getText();

            if(!archive.createNewFile()){
                ErrorManager.showErrorManager("Erro ao criar novo registro",
                        "EditRegisterController",
                        "editAndSave",
                        ErrorManager.GRAVE_SEVERITY);
                return;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(archive));
            String SEPARATOR = ProgramInfos.SEPARATOR;
            writer.write(
                    Cryptography.encrypt(user) + SEPARATOR +
                            Cryptography.encrypt(email) + SEPARATOR +
                            Cryptography.encrypt(pass) + SEPARATOR +
                            Cryptography.encrypt(Main.instance.usingMaster));
            writer.close();

            resetParameters();

            MainController.instance.updateRegister();
            Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        resetParameters();
        Main.instance.setPrimaryScene(SceneName.MAIN_SCENE);
    }

    private void resetParameters(){
        setParameters(null, "", "", "", "");
        newUser.setText("");
        newEmail.setText("");
        newPass.setText("");
    }
}
