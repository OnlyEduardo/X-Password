package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.util.ProgramInfos;
import com.swellshinider.util.SceneName;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class BeginController implements Initializable {

    public Text aboutText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aboutText.setText(ProgramInfos.ABOUT);
    }

    public void goToCreatePass(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        Main.instance.setPrimaryScene(SceneName.CREATE_PASS_SCENE);
    }
}
