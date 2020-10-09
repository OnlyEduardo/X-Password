package com.swellshinider.util;

import javafx.scene.Scene;

public interface AllScenes {
    Scene createPassScene = new Scene(Parents.createPassParent);
    Scene loginScene = new Scene(Parents.loginParent);
    Scene mainScene = new Scene(Parents.mainParent);
    Scene changeMasterPassScene = new Scene(Parents.changeMasterPassParent);
    Scene createRegisterScene = new Scene(Parents.createRegisterParent);
    Scene beginScene = new Scene(Parents.beginParent);
    Scene popUpScene = new Scene(Parents.popUpParent);
    Scene editRegisterScene = new Scene(Parents.editRegisterParent);
}
