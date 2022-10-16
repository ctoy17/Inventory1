package sharrow.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID;
    private String prodName;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int productID, String prodName, double price, int stock, int min, int max){
        setProductID(productID);
        setProdName(prodName);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
    public void addAssociatedPart(Part partAdded){
        associatedParts.add(partAdded);
    }
    public boolean deleteAssociatedPart(Part partDeleted){
        if(associatedParts.contains(partDeleted)){
            associatedParts.remove(partDeleted);
            return true;
        }else
            return false;
    }
    /**gets all associated parts connected to a product**/
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
    /**gets and sets product name**/
    public String getProdName() {
        return this.prodName;
    }
    public void setProdName(String prodName){
        this.prodName = prodName;
    }
    /**gets and sets product ID**/

    public int getProductID() {
        return this.productID;
    }
    public void setProductID(int productID){
        this.productID = productID;
    }
    /**gets and sets product price**/

    public double getPrice(){
        return this.price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    /**gets and sets product stock**/

    public int getStock(){
        return this.stock;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    /**gets and sets product min**/

    public int getMin(){
        return this.min;
    }
    public void setMin(int min){
        this.min = min;
    }
    /**gets and sets product max**/

    public int getMax(){
        return this.max;
    }
    public void setMax(int max){
        this.max = max;
    }
    public ObservableList<Part> assocParts (){
        return associatedParts;
    }
}
