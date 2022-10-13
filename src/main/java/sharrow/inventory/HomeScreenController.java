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

public class HomeScreenController implements Initializable {
    private static Part modifySelectedPart;
    private static Product modifySelectedProd;
    @FXML private TableView<Part> partTable;
    @FXML private TableView<Product> prodTable;
    @FXML private TextField searchPart;
    @FXML private TextField searchProd;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableColumn<Product, Integer> prodID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Product, String> prodName;
    @FXML private TableColumn<Part, String> partStock;
    @FXML private TableColumn<Product, String> prodStock;
    @FXML private TableColumn<Part, String> partPrice;
    @FXML private TableColumn<Product, String> prodPrice;

    public static Part getModifySelectedPart(){
        return modifySelectedPart;
    }
    public void onClickModifyPart(ActionEvent actionEvent) throws IOException{
        modifySelectedPart = partTable.getSelectionModel().getSelectedItem();
        if (modifySelectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a part to modify");
            alert.showAndWait();
        } else{
            Parent parent = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    public void onClickAddPart(ActionEvent actionEvent) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void onClickDeletePart(ActionEvent actionEvent) {
        Part partSelected = partTable.getSelectionModel().getSelectedItem();
        if (partSelected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                Inventory.deletePart(partSelected);
            }
        }
    }
    public void partSearch(ActionEvent actionEvent){
        ObservableList<Part> parts = Inventory.getPartsList();
        ObservableList<Part> partResult = FXCollections.observableArrayList();
        String searchResults = searchPart.getText();
        for (Part part : parts){
            if(String.valueOf(part.getId()).contains(searchResults) || part.getName().contains(searchResults)){
                partResult.add(part);
            }
        }
        partTable.setItems(partResult);
        if (partResult.size()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Part not found. Please enter a new part name or ID");
            alert.showAndWait();
        }
    }
    public static Product getModifySelectedProd(){
        return modifySelectedProd;
    }
    public void onClickModifyProd(ActionEvent actionEvent) throws IOException{
        modifySelectedProd = prodTable.getSelectionModel().getSelectedItem();
        if (modifySelectedProd == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a product to modify");
            alert.showAndWait();
        } else{
            Parent parent = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    public void onClickAddProd(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void onClickDeleteProd(ActionEvent actionEvent){
        Product prodSelected = prodTable.getSelectionModel().getSelectedItem();
        if (prodSelected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a product to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                ObservableList<Part> assocPart = prodSelected.getAssociatedParts();
                if (assocPart.size() >= 1) {
                    Alert assocAlert = new Alert(Alert.AlertType.ERROR);
                    assocAlert.setTitle("ERROR");
                    assocAlert.setContentText("Please remove all associated parts before deleting product.");
                    assocAlert.showAndWait();
                } else {
                    Inventory.deleteProd(prodSelected);
                }
            }
        }
    }
    public void prodSearch(ActionEvent actionEvent){
        ObservableList<Product> products = Inventory.getProductList();
        ObservableList<Product> prodResult = FXCollections.observableArrayList();
        String searchResults = searchProd.getText();
        for (Product product : products){
            if(String.valueOf(product.getProductID()).contains(searchResults) || product.getName().contains(searchResults)){
                prodResult.add(product);
            }
        }
        prodTable.setItems(prodResult);
        if (prodResult.size()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Product not found. Please enter a new product name or ID");
            alert.showAndWait();
        }
    }
    public void onClickClose(ActionEvent actionEvent){
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTable.setItems(Inventory.getPartsList());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodTable.setItems(Inventory.getProductList());
        prodID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        prodName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        prodStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
