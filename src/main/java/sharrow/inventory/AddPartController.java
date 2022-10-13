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

public class AddPartController implements Initializable {
    public TextField idField;
    public TextField nameField;
    public TextField priceField;
    public TextField stockField;
    public TextField minField;
    public TextField maxField;
    public TextField machineNameField;
    Stage stage;
    Parent scene;

    @FXML public ToggleGroup location;
    @FXML private RadioButton inHouseSelector;
    @FXML private RadioButton outsourcedSelector;
    @FXML private Label radioButtonLabel;

     ;


    private void throwAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Missing field");
                alert.setContentText("Part was NOT added. Please fill in all fields");
                alert.showAndWait();
            }
            case 2 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Inventory must be a number.");
                alert.setContentText("Please enter a valid integer between min and max values.");
                alert.showAndWait();
            }
            case 3 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Min Value Invalid");
                alert.setContentText("Min must be an integer greater than 0 and less than Max.");
                alert.showAndWait();
            }
            case 4 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Max Value Invalid");
                alert.setContentText("Max must be a valid integer.");
                alert.showAndWait();
            }
        }
    }
    public void onClickInHouse(ActionEvent actionEvent) {
        if (this.location.getSelectedToggle().equals(this.inHouseSelector))
            radioButtonLabel.setText("Machine ID");
    }

    public void onClickOutsourced(ActionEvent actionEvent) {
        if (this.location.getSelectedToggle().equals(this.outsourcedSelector))
            radioButtonLabel.setText("Company Name");
    }
    private boolean inStock(int min, int max, int stock){
        boolean validStockNum = true;
        if (stock < min || stock > max){
            validStockNum = false;
            throwAlert(2);
        }
        return validStockNum;
    }
    private  boolean minStock(int min, int max){
        boolean validMinNum = true;
        if (min <= 0 || min >= max){
            validMinNum = false;
            throwAlert(3);
        }
        return validMinNum;
    }
    private void homeScreen(ActionEvent actionEvent) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    public void onClickSave(ActionEvent actionEvent) throws IOException {
        try {
            String name = nameField.getText();
            Double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());
            int machineId;
            String companyName;
            boolean partAdded = false;
            int id = 0;

            if (name.isEmpty()) {
                throwAlert(1);
            } else {
                if (minStock(min, max) && inStock(min, max, stock)) {
                    if (inHouseSelector.isSelected()) {
                        try {
                            machineId = Integer.parseInt(machineNameField.getText());
                            InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                            newInHousePart.setId(Inventory.getNewId());
                            Inventory.addNewPart(newInHousePart);
                            partAdded = true;
                        } catch (Exception e) {
                            throwAlert(1);
                        }
                    }
                    if (outsourcedSelector.isSelected()) {
                        companyName = machineNameField.getText();
                        Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                        newOutsourcedPart.setId(Inventory.getNewId());
                        Inventory.addNewPart(newOutsourcedPart);
                        partAdded = true;
                    }
                    if (partAdded) {
                        homeScreen(actionEvent);
                    }
                }
            }
        } catch (Exception e) {
            throwAlert(1);
        }
    }
    public void onClickCancel(ActionEvent actionEvent) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Cancel and go back to Home Screen.");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.YES){
            homeScreen(actionEvent);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouseSelector.setSelected(true);
    }
}
