package com.swellshinider.main;

import com.swellshinider.util.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    public static Main instance;
    public String usingMaster = "";

    @Override
    public void start(Stage _primaryStage) {
        instance = this;
        primaryStage = _primaryStage;
        ImageView iv = new ImageView(new Image(getClass().getResource("resources/icon.png").toExternalForm()));
        primaryStage.getIcons().add(iv.getImage());

        if(haveMasterPassword())
            setPrimaryScene(SceneName.LOGIN_SCENE);
        else
            setPrimaryScene(SceneName.BEGIN_SCENE);
    }

    private boolean haveMasterPassword() {
        File fileOfRootPath = new File(PathsAndArchives.ROOT_PATH);

        if(fileOfRootPath.exists()){
            File _configFilePath = new File(PathsAndArchives.ROOT_PATH + "/"+ PathsAndArchives.CONFIG_NAME +".txt");

            if(_configFilePath.exists()){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(_configFilePath));

                    return reader.readLine().length() == 64;

                } catch (IOException e) {
                    ErrorManager.showErrorManager("Falha ao ler o arquivo de configuração",
                            "Main",
                            "haveMasterPassword",
                            e,
                            ErrorManager.NORMAL_SEVERITY);
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setPrimaryScene(SceneName sceneName){

        if (sceneName == null)
            return;

        switch (sceneName) {
            case CREATE_PASS_SCENE:
                primaryStage.setScene(AllScenes.createPassScene);
                primaryStage.setTitle(TitleScenes.CREATE_PASS_SCENE);
                break;
            case LOGIN_SCENE:
                primaryStage.setScene(AllScenes.loginScene);
                primaryStage.setTitle(TitleScenes.LOGIN_SCENE);
                break;
            case MAIN_SCENE:
                primaryStage.setScene(AllScenes.mainScene);
                primaryStage.setTitle(TitleScenes.MAIN_SCENE);
                break;
            case CHANGE_PASS_SCENE:
                primaryStage.setScene(AllScenes.changeMasterPassScene);
                primaryStage.setTitle(TitleScenes.CHANGE_MASTER_PASS_SCENE);
                break;
            case CREATE_REGISTER_SCENE:
                primaryStage.setScene(AllScenes.createRegisterScene);
                primaryStage.setTitle(TitleScenes.CREATE_REGISTER_SCENE);
                break;
            case BEGIN_SCENE:
                primaryStage.setScene(AllScenes.beginScene);
                primaryStage.setTitle(TitleScenes.BEGIN_SCENE);
                break;
            case POP_UP_SCENE:
                primaryStage.setScene(AllScenes.popUpScene);
                primaryStage.setTitle(TitleScenes.POP_UP_SCENE);
                break;
            case EDIT_REGISTER_SCENE:
                primaryStage.setScene(AllScenes.editRegisterScene);
                primaryStage.setTitle(TitleScenes.EDIT_REGISTER_SCENE);
                break;
            case EXIT:
                System.exit(0);
                break;
        }

        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void copyToClipboard(String textToCopy){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(textToCopy);
        clipboard.setContents(selection, null);
    }
}
