package com.swellshinider.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Parents {

    public static Parent loginParent;
    public static Parent createPassParent;
    public static Parent mainParent;
    public static Parent changeMasterPassParent;
    public static Parent createRegisterParent;
    public static Parent beginParent;
    public static Parent popUpParent;
    public static Parent editRegisterParent;

    static {
        try {
            loginParent = FXMLLoader.load(Parents.class.getResource("fxml/LoginScene.fxml"));
            createPassParent = FXMLLoader.load(Parents.class.getResource("fxml/CreatePassScene.fxml"));
            mainParent = FXMLLoader.load(Parents.class.getResource("fxml/MainScene.fxml"));
            changeMasterPassParent = FXMLLoader.load(Parents.class.getResource("fxml/ChangeMasterPassScene.fxml"));
            createRegisterParent = FXMLLoader.load(Parents.class.getResource("fxml/CreateRegisterScene.fxml"));
            beginParent = FXMLLoader.load(Parents.class.getResource("fxml/BeginScene.fxml"));
            popUpParent = FXMLLoader.load(Parents.class.getResource("fxml/PopUpScene.fxml"));
            editRegisterParent = FXMLLoader.load(Parents.class.getResource("fxml/editRegisterScene.fxml"));

        } catch (IOException e) {
            ErrorManager.showErrorManager(
                    "Não foi possível carregar tela",
                    "Parents",
                    "Parents",
                    e,
                    ErrorManager.FALTAL_SEVERITY);
        }
    }
}
