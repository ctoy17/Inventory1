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


public class AddProductController implements Initializable {
    @FXML private TextField prodNameField;
    @FXML private TextField prodPriceField;
    @FXML private TextField prodStockField;
    @FXML private TextField prodMinField;
    @FXML private TextField prodMaxField;
    @FXML private TextField productIDField;
    @FXML private TextField partSearch;
    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Inventory> partStock;
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableView<Part> assocPartTable;
    @FXML private TableColumn<Part, Integer> assocPartId;
    @FXML private TableColumn<Part, String> assocPartName;
    @FXML private TableColumn<Part, Inventory> assocPartStock;
    @FXML private TableColumn<Part, Double> assocPartPrice;
    private ObservableList<Part> assocPart = FXCollections.observableArrayList();

    public AddProductController() {
    }


    private void throwAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

        switch (alertType) {
            case 1 -> {
                alert.setTitle("Error");
                alert.setHeaderText("Missing field");
                alert.setContentText("Product was NOT added. Please fill in all fields");
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
                alert.setHeaderText("No Part Selected. Please Select a Part");
                alert.showAndWait();
            }
            case 5 -> {
                alertInfo.setTitle("Info");
                alertInfo.setHeaderText("Part Not Found");
                alertInfo.showAndWait();
            }
        }
    }
    public void partSearchField(ActionEvent actionEvent){
        ObservableList<Part> partList = Inventory.getPartsList();
        ObservableList<Part> foundPart = FXCollections.observableArrayList();
        String searchResult = partSearch.getText();
        for(Part part : partList){
            if(String.valueOf(part.getId()).contains(searchResult) || part.getName().contains(searchResult)){
                foundPart.add(part);
            }
        }partTable.setItems(foundPart);
        if(foundPart.size()==0){
            throwAlert(5);
        }
    }
    public void onClickSave(ActionEvent actionEvent){
        try{
            int id =0;
            String prodName = prodNameField.getText();
            Double price = Double.parseDouble(prodPriceField.getText());
            int stock = Integer.parseInt(prodStockField.getText());
            int min = Integer.parseInt(prodMinField.getText());
            int max = Integer.parseInt(prodMaxField.getText());

            if (prodName.isEmpty()){
                throwAlert(1);
            }
            else{
                if(minValid(min, max) && inStock(min, max, stock)){
                    Product newProduct = new Product(id, prodName, price, stock, min, max);
                    for (Part part : assocPart){
                        newProduct.addAssociatedPart(part);
                    }
                    newProduct.setProductID(Inventory.getNewProductId());
                    Inventory.addNewProduct(newProduct);
                    homeScreen(actionEvent);
                }
            }
        }catch (Exception e){
            throwAlert(1);
        }
    }
    public void onClickAdd(ActionEvent actionEvent){
        Part partSelected = partTable.getSelectionModel().getSelectedItem();
        if(partSelected == null){
            throwAlert(4);
        }else{
            assocPart.add(partSelected);
            assocPartTable.setItems(assocPart);
        }
    }
    public void onClickRemoveAssocPart(ActionEvent actionEvent){
        Part partSelected = assocPartTable.getSelectionModel().getSelectedItem();
        if (partSelected == null){
            throwAlert(4);
        }else{
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Warning");
            confirm.setContentText("Are you sure you want to remove this part?");
            Optional<ButtonType> answer = confirm.showAndWait();
            if (answer.isPresent() && answer.get()== ButtonType.YES){
                assocPart.remove(partSelected);
                assocPartTable.setItems(assocPart);
            }
        }
    }
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);
        cancel.setTitle("Warning");
        cancel.setContentText("Do you want to cancel and return home?");
        Optional<ButtonType> answer = cancel.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.YES){
            homeScreen(actionEvent);
        }
    }
    private void homeScreen(ActionEvent actionEvent) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    private boolean inStock(int min, int max, int stock) {
        boolean invValid = true;
        if(stock < min || stock > max){
            invValid = false;
            throwAlert(2);
        }return invValid;
    }

    private boolean minValid(int min, int max) {
        boolean minInvValid = true;
        if (min<=0 || min >= max){
            minInvValid = false;
            throwAlert(3);
        }return minInvValid;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partName.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("PRICE"));
        assocPartId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        assocPartStock.setCellValueFactory(new PropertyValueFactory<>("STOCK"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("PRICE"));
        partTable.setItems(Inventory.getPartsList());
    }
}
