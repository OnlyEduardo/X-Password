package com.swellshinider.util;

import com.swellshinider.controllers.PopUpController;
import com.swellshinider.main.Main;

public class PopUp {

    public static void newPopUpToGo(String message, SceneName sceneToGo){
        PopUpController.instance.setMessageAndGo(message, sceneToGo);
        Main.instance.setPrimaryScene(SceneName.POP_UP_SCENE);
    }
}
