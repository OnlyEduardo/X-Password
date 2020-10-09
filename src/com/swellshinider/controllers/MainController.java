package com.swellshinider.controllers;

import com.swellshinider.main.Main;
import com.swellshinider.main.Cryptography;
import com.swellshinider.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public static MainController instance;

    private File selectedFile = null;
    private final List<Tab> tabsNames = new ArrayList<>();

    // Fxml
    public TabPane tabPane;
    public MenuItem textToShowVersion;
    public Text textSemRegister;
    public TextField searchTextInput;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        instance = this;
        textToShowVersion.setText(ProgramInfos.VERSION);

        File dirOfRegister = new File(PathsAndArchives.PATH_TO_REGISTERS);

        if(!dirOfRegister.exists()){
            if (!dirOfRegister.mkdirs())
                ErrorManager.showErrorManager("Erro ao tentar criar pasta raiz do X-Password",
                        "MainController",
                        "initialize",
                        ErrorManager.FALTAL_SEVERITY);
        }

        updateRegister();
    }

    public void updateRegister(){
        File dirOfRegister = new File(PathsAndArchives.PATH_TO_REGISTERS);

        File[] arqs = dirOfRegister.listFiles();

        if(arqs == null)
            return;

        tabPane.getTabs().clear();
        tabsNames.clear();

        for (File arq : arqs) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(arq));
                String tempRegisterTitle = arq.getName().replace("_", " ");
                tempRegisterTitle = tempRegisterTitle.replace(".txt", "");

                String tempUsername;
                String tempEmail;
                String tempPassword;
                String tempMaster;

                try {
                    String SEPARATOR = ProgramInfos.SEPARATOR;
                    String[] l = reader.readLine().split(SEPARATOR);
                    reader.close();

                    tempUsername = Cryptography.decrypt(l[0]);
                    tempEmail = Cryptography.decrypt(l[1]);
                    tempPassword = Cryptography.decrypt(l[2]);
                    tempMaster = Cryptography.decrypt(l[3]);

                    if(tempMaster == null)
                        continue;

                    if(!tempMaster.equals(Main.instance.usingMaster))
                        continue;

                } catch (Exception ignored){
                    continue;
                }

                Tab tabInQuestion = createTabsRegister(
                        tempRegisterTitle,
                        tempUsername,
                        tempEmail,
                        tempPassword,
                        arq);

                tabPane.getTabs().add(tabInQuestion);
                tabsNames.add(tabInQuestion);

            } catch (IOException e) {
                ErrorManager.showErrorManager("Erro fatal desconhecido",
                        "MainController",
                        "updateRegister",
                        e,
                        ErrorManager.FALTAL_SEVERITY);
            }
        }

        if(tabPane.getTabs().size() == 0){
            tabPane.setVisible(false);
            textSemRegister.setVisible(true);
        } else {
            tabPane.setVisible(true);
            textSemRegister.setVisible(false);
        }
    }

    private void search() {
        updateRegister();

        String input = searchTextInput.getText().replace(" ", "");

        if(input.equalsIgnoreCase(""))
            return;

        for (Tab tab : tabsNames) {
            String t = tab.getText().toLowerCase().replace(" ", "");

            if (!input.equals(t) && !t.endsWith(input) && !t.startsWith(input) && !t.contains(input)) {
                tabPane.getTabs().remove(tab);
            }
        }

        if(tabPane.getTabs().size() == 0)
            updateRegister();
    }

    public void deleteActualRegister(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        if(selectedFile == null)
            return;

        if(!selectedFile.delete()){
            ErrorManager.showErrorManager("Não foi possível deletar o registro",
                    "MainController",
                    "deleteActualRegister",
                    ErrorManager.GRAVE_SEVERITY);
            return;
        }
        updateRegister();
    }

    public void deleteAllRegisters(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        File dirOfRegister = new File(PathsAndArchives.PATH_TO_REGISTERS);
        File[] arqs = dirOfRegister.listFiles();

        if(arqs == null || arqs.length == 0){
            updateRegister();
            return;
        }

        for (File percorendoArqs: arqs){
            if(!percorendoArqs.delete()){
                if(percorendoArqs.exists())
                    if(!percorendoArqs.delete())
                        ErrorManager.showErrorManager("Não foi possível deletar o registro",
                                "MainController",
                                "deleteAllRegisters",
                                ErrorManager.GRAVE_SEVERITY);
            }
        }
        selectedFile = null;
        updateRegister();
    }

    private Tab createTabsRegister(String title, String username, String email, String password, File tabArq){
        Tab edditedTab = new Tab();
        edditedTab.setText(title);
        edditedTab.setOnSelectionChanged(event -> selectedFile = tabArq);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);
        anchorPane.setPrefSize(600, 240);

        // Apresentation Text
        Text nomeuserText = new Text();
        Text emailText = new Text();
        Text passwordText = new Text();

        nomeuserText.setText("Nome de Usuário:");
        nomeuserText.setLayoutX(28);
        nomeuserText.setLayoutY(72);
        nomeuserText.setFont(new Font("", 18));

        emailText.setText("Email:");
        emailText.setLayoutX(28);
        emailText.setLayoutY(110);
        emailText.setFont(new Font("", 18));

        passwordText.setText("Senha:");
        passwordText.setLayoutX(28);
        passwordText.setLayoutY(144);
        passwordText.setFont(new Font("", 18));

        // Separatos
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        Separator sep3 = new Separator();

        sep1.setPrefSize(543, 18);
        sep1.setLayoutX(28);
        sep1.setLayoutY(76);

        sep2.setPrefSize(543, 18);
        sep2.setLayoutX(28);
        sep2.setLayoutY(112);

        sep3.setPrefSize(543, 18);
        sep3.setLayoutX(28);
        sep3.setLayoutY(146);

        // Fields
        TextField userTextField = new TextField();
        TextField emailTextField = new TextField();
        PasswordField passwordTextField = new PasswordField();

        userTextField.setPrefSize(395,26);
        userTextField.setLayoutX(175);
        userTextField.setLayoutY(52);
        userTextField.setEditable(false);
        userTextField.setText(username);

        emailTextField.setPrefSize(485,26);
        emailTextField.setLayoutX(87);
        emailTextField.setLayoutY(90);
        emailTextField.setEditable(false);
        emailTextField.setText(email);

        passwordTextField.setPrefSize(425, 26);
        passwordTextField.setLayoutX(87);
        passwordTextField.setLayoutY(124);
        passwordTextField.setEditable(false);
        passwordTextField.setText(password);

        Button copyButton = new Button();
        copyButton.setText("Copiar");
        copyButton.setPrefSize(51.2f, 25.5f);
        copyButton.setLayoutX(519);
        copyButton.setLayoutY(125);
        copyButton.setOnAction(actionEvent -> Main.instance.copyToClipboard(password));

        // Adding contentsToAnchor
        anchorPane.getChildren().addAll(
                nomeuserText,
                emailText,
                passwordText,
                sep1,
                sep2,
                sep3,
                userTextField,
                emailTextField,
                passwordTextField,
                copyButton);

        // Main add
        edditedTab.setContent(anchorPane);
        return edditedTab;
    }

    public void createNewArchiveRegister(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        Main.instance.setPrimaryScene(SceneName.CREATE_REGISTER_SCENE);
    }

    public void openAboutMessageBox(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        PopUp.newPopUpToGo(ProgramInfos.ABOUT, SceneName.MAIN_SCENE);
    }

    public void redefineMasterPassword(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        Main.instance.setPrimaryScene(SceneName.CHANGE_PASS_SCENE);
    }

    public void exitApplication(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;
        System.exit(0);
    }

    public void searchTab(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        search();
    }

    public void attRegistersButton(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        updateRegister();
    }

    public void editRegister(ActionEvent actionEvent) {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        if(selectedFile == null)
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String tempRegisterTitle = selectedFile.getName().replace("_", " ");
            tempRegisterTitle = tempRegisterTitle.replace(".txt", "");

            String tempUsername;
            String tempEmail;
            String tempPassword;
            String SEPARATOR = ProgramInfos.SEPARATOR;

            String[] l = reader.readLine().split(SEPARATOR);
            reader.close();

            tempUsername = Cryptography.decrypt(l[0]);
            tempEmail = Cryptography.decrypt(l[1]);
            tempPassword = Cryptography.decrypt(l[2]);

            EditRegisterController.instance.setParameters(selectedFile, tempRegisterTitle, tempUsername,
                    tempEmail, tempPassword);
            Main.instance.setPrimaryScene(SceneName.EDIT_REGISTER_SCENE);
        } catch (IOException e) {
            ErrorManager.showErrorManager("Erro fatal desconhecido",
                    "MainController",
                    "updateRegister",
                    e,
                    ErrorManager.FALTAL_SEVERITY);
        }
    }

    public void openSite(ActionEvent actionEvent) throws URISyntaxException, IOException {
        if(!actionEvent.getEventType().equals(ActionEvent.ACTION))
            return;

        java.awt.Desktop.getDesktop().browse( new java.net.URI( "https://www.eduardo-ribeiro-leal.com"));
    }
}
