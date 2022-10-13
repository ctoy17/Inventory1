package sharrow.inventory;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static int partID = 0;
    private static int productID = 0;
    private static ObservableList<Part> partsList = FXCollections.observableArrayList();
    private static ObservableList<Product> productList = FXCollections.observableArrayList();
    public static ObservableList<Part> getPartsList(){
        return partsList;
    }
    public static ObservableList<Product> getProductList(){
        return productList;
    }
    public static Part searchPart(int partID){
        Part partFound = null;
        for (Part part : partsList){
            if(part.getId() == partID){
                partFound = part;
            }
        } return partFound;
    }
    public static Product searchProd(int productID){
        Product prodFound = null;
        for(Product product : productList){
            if (product.getProductID() == productID){
                prodFound = product;
            }
        } return prodFound;
    }
    public static ObservableList<Part> searchPart(String partName){
        ObservableList<Part> partsReturned = FXCollections.observableArrayList();
        for(Part part : partsList){
            if(part.getName().equals(partName)){
                partsReturned.add(part);
            }
        }return partsReturned;
    }
    public static ObservableList<Product> searchProd(String prodName){
        ObservableList<Product> prodsReturned = FXCollections.observableArrayList();
        for(Product product : productList){
            if(product.getProdName().equals(prodName)){
                prodsReturned.add(product);
            }
        }return prodsReturned;
    }
    public static int getNewId() {
        return ++partID;
    }
    public static int getNewProductId(){
        return ++productID;
    }
    public static void addNewPart(Part newPart) {
        partsList.add(newPart);
    }
    public static void addNewProduct(Product newProduct){
        productList.add(newProduct);
    }
    public static void updatePart (int number, Part partSelected){
        partsList.set(number, partSelected);
    }
    public static void updateProd (int number, Product prodSelected){
        productList.set(number, prodSelected);
    }
    public static boolean deletePart(Part partSelected){
        for(Part part : partsList){
            if (part == partSelected){
                partsList.remove(partSelected);
                return true;
            }
        }return false;
    }
    public static boolean deleteProd(Product prodSelected){
        for (Product product : productList){
            if (product == prodSelected){
                productList.remove(prodSelected);
                return true;
            }
        }return false;
    }
}
