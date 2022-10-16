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

import static sharrow.inventory.HomeScreenController.getModifySelectedProd;

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
    private int modID = getModifySelectedProd().getProductID();
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
/**validates the minimum amount is lower than the max and stock**/
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
    /**daves the new modified product and removes the older version**/
    public void onClickSave(ActionEvent actionEvent) {
        try {
            int ID = modID;
            String modName = prodName.getText();
            double modPrice = Double.parseDouble(prodPrice.getText());
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
                    Product modProd = new Product(ID, modName, modPrice, modStock, modMin, modMax);
                    for (Part part : assocParts) {
                        modProd.addAssociatedPart(part);
                    }
                    Inventory.addNewProduct(modProd);
                    Inventory.deleteProd(productToMod);
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
    /**adds associated parts to the product**/
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
    /**removes associated parts from product**/
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
            if(answer.isPresent() && answer.get() == ButtonType.OK){
                assocParts.remove(partSelected);
                assocPartTable.setItems(assocParts);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productToMod = getModifySelectedProd();
        assocParts = productToMod.getAssociatedParts();
        assocPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartTable.setItems(assocParts);
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTable.setItems(Inventory.getPartsList());
        prodID.setText(String.valueOf(productToMod.getProductID()));
        prodName.setText(productToMod.getProdName());
        prodPrice.setText(Double.toString(productToMod.getPrice()));
        prodStock.setText(Integer.toString(productToMod.getStock()));
        prodMin.setText(Integer.toString(productToMod.getMin()));
        prodMax.setText(Integer.toString(productToMod.getMax()));

    }
}
