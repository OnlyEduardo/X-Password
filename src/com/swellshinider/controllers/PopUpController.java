package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.util.SceneName;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {

    public static PopUpController instance;
    public Text messageText;

    private SceneName sceneToGo;
    
    public void setMessageAndGo(String message, SceneName goTo) {
        messageText.setText(message);
        sceneToGo = goTo;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
    }

    public void goBack(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        Main.instance.setPrimaryScene(sceneToGo);
    }


}
