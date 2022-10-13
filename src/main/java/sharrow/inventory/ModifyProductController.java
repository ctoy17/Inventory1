package sharrow.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {
    @FXML private TableView<Part> assocPartTable;
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableColumn<Part, Integer> assocPartID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, String> assocPartName;
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Part, Double> assocPartPrice;
    @FXML private TableColumn<Part, Integer> partStock;
    @FXML private TableColumn<Part, Integer> assocPartStock;
    @FXML private TextField partSearch;
    @FXML private TextField prodID;
    @FXML private TextField prodName;
    @FXML private TextField prodPrice;
    @FXML private TextField prodStock;
    @FXML private TextField prodMin;
    @FXML private TextField prodMax;
    Product productToMod;
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();
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
    private void homeScreen(ActionEvent actionEvent) throws IOException {
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
        if (answer.isPresent() && answer.get()==ButtonType.OK){
            homeScreen(actionEvent);
        }
    }
    public void onClickSave(ActionEvent actionEvent) {
        try {
            int modID = productToMod.getProductID();
            String modName = prodName.getText();
            Double modPrice = Double.parseDouble(prodPrice.getText());
            int modStock = Integer.parseInt(prodStock.getText());
            int modMin = Integer.parseInt(prodMin.getText());
            int modMax = Integer.parseInt(prodMax.getText());
            if (modName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Product NOT modified.");
                alert.setContentText("Product could NOT be modified. Fill out all fields.");
                alert.showAndWait();
            } else {
                if (minAmount(modMin, modMax) && inStock(modMin, modMax, modStock)) {
                    Product modProd = new Product(modID, modName, modPrice, modStock, modMin, modMax);
                    for (Part part : assocParts) {
                        modProd.addAssociatedPart(part);
                    }
                    Inventory.addNewProduct(modProd);
                    homeScreen(actionEvent);
                }
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product NOT modified.");
            alert.setContentText("Product could NOT be modified. Fill out all fields.");
            alert.showAndWait();
            }
        }
    public void searchPart(ActionEvent actionEvent){
        ObservableList<Part> parts = Inventory.getPartsList();
        ObservableList<Part> results = FXCollections.observableArrayList();
        String searchResults = partSearch.getText();
        for (Part part : parts){
            if(String.valueOf(part.getId()).contains(searchResults) || part.getName().contains(searchResults)){
                results.add(part);
            }
        }partTable.setItems(results);
        if(results.size()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not Found.");
            alert.setContentText("Part not found. Please update search field.");
            alert.showAndWait();
        }
    }
    public void onClickAdd(ActionEvent actionEvent){
        Part partSelected = partTable.getSelectionModel().getSelectedItem();
        if(partSelected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not Chosen.");
            alert.setContentText("Please select a part to continue.");
            alert.showAndWait();
        }else{
            assocParts.add(partSelected);
            assocPartTable.setItems(assocParts);
        }
    }
    public void onClickRemove(ActionEvent actionEvent){
        Part partSelected = assocPartTable.getSelectionModel().getSelectedItem();
        if(partSelected == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not Chosen.");
            alert.setContentText("Please select a part to continue.");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Are you sure you want to remove the selected part?");
            Optional<ButtonType> answer = alert.showAndWait();
            if(answer.isPresent() && answer.get() == ButtonType.YES){
                assocParts.remove(partSelected);
                assocPartTable.setItems(assocParts);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productToMod = HomeScreenController.getModifySelectedProd();
        assocParts = productToMod.getAssociatedParts();
        assocPartID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("PRICE"));
        assocPartStock.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
        assocPartTable.setItems(assocParts);
        partID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partName.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("PRICE"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
        partTable.setItems(Inventory.getPartsList());
        prodID.setText(String.valueOf(productToMod.getProductID()));
        prodName.setText(productToMod.getName());
        prodPrice.setText(String.valueOf(productToMod.getPrice()));
        prodStock.setText(String.valueOf(productToMod.getStock()));
        prodMin.setText(String.valueOf(productToMod.getMin()));
        prodMax.setText(String.valueOf(productToMod.getMax()));

    }
}
