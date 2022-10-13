package sharrow.inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {
    @FXML private TextField minStockField;
    @FXML private TextField maxStockField;
    @FXML private TextField partPriceField;
    @FXML private TextField partStockField;
    @FXML private TextField idOrCompName;
    @FXML private TextField partName;
    @FXML private TextField partID;
    @FXML private RadioButton inHousePart;
    @FXML private RadioButton outsourcedPart;
    @FXML private ToggleGroup location;
    @FXML private Label idOrNameLabel;
    private Part partToModify;

    public void onSelectInHouse(ActionEvent actionEvent){
        idOrNameLabel.setText("Machine ID");
    }
    public void onSelectOutsourced(ActionEvent actionEvent){
        idOrNameLabel.setText("Company Name");
    }
    public void onClickSave(ActionEvent actionEvent){
        try{
            int modID = partToModify.getId();
            String modName = partName.getText();
            Double modPrice = Double.parseDouble(partPriceField.getText());
            int modStock = Integer.parseInt(partStockField.getText());
            int modMin = Integer.parseInt(minStockField.getText());
            int modMax = Integer.parseInt(maxStockField.getText());
            int modMachineID;
            String modCompName;
            boolean modPartAdded = false;
            if(minAmount(modMin,modMax) && inStock(modMin, modMax, modStock)){
                if (inHousePart.isSelected()){
                    try {
                        modMachineID = Integer.parseInt(idOrCompName.getText());
                        InHouse newPartMod = new InHouse(modID, modName, modPrice, modStock, modMin, modMax, modMachineID);
                        Inventory.addNewPart(newPartMod);
                        modPartAdded = true;
                    }catch (Exception e){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Machine ID is not valid.");
                        alert.setContentText("Machine ID is not valid. Please enter a valid number.");
                        alert.showAndWait();
                    }
                }if(outsourcedPart.isSelected()){
                        modCompName = idOrCompName.getText();
                        Outsourced newOPartMod = new Outsourced(modID, modName, modPrice, modStock, modMin, modMax, modCompName);
                        Inventory.addNewPart(newOPartMod);
                        modPartAdded = true;
                } if(modPartAdded){
                        Inventory.deletePart(partToModify);
                        homeScreen(actionEvent);
                    }
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Part NOT modified.");
            alert.setContentText("Part could NOT be modified. Fill out all fields.");
            alert.showAndWait();
        }
    }
    private boolean inStock(int modMin, int modMax, int modStock) {
        boolean stockValid = true;
        if (modStock< modMin || modStock > modMax){
            stockValid = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Stock invalid.");
            alert.setContentText("Stock value is not valid. Please enter a valid number.");
            alert.showAndWait();
        }return stockValid;
    }

    private boolean minAmount(int modMin, int modMax) {
        boolean minValid = true;
        if (modMin <= 0 || modMin >= modMax){
            minValid = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not a valid number.");
            alert.setContentText("Minimum value is not valid. Please enter a number between 0 and max.");
            alert.showAndWait();
        }return minValid;
    }

    private void homeScreen(ActionEvent actionEvent) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onClickCancel(ActionEvent actionEvent) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ALERT");
        alert.setHeaderText("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.isPresent() && answer.get()==ButtonType.YES){
            homeScreen(actionEvent);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partToModify = HomeScreenController.getModifySelectedPart();
        if(partToModify instanceof InHouse){
            inHousePart.setSelected(true);
            idOrNameLabel.setText("Machine ID");
            idOrCompName.setText(String.valueOf(((InHouse) partToModify).getMachineId()));
        }
        if(partToModify instanceof Outsourced){
            outsourcedPart.setSelected(true);
            idOrNameLabel.setText("Company Name");
            idOrCompName.setText(((Outsourced) partToModify).getCompanyName());
        }
        partID.setText(String.valueOf(partToModify.getId()));
        partName.setText(String.valueOf(partToModify.getName()));
        partPriceField.setText(String.valueOf(partToModify.getPrice()));
        partStockField.setText(String.valueOf(partToModify.getStock()));
        minStockField.setText(String.valueOf(partToModify.getMin()));
        maxStockField.setText(String.valueOf(partToModify.getMax()));
    }
}
