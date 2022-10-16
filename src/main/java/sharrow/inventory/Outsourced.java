package sharrow.inventory;
/**this class helps with the creation, update, and delete methods for outsourced parts**/
public class Outsourced extends Part {
    private String companyName;
    /**this is the constructor for outsourced parts**/
    public Outsourced(int id, String name, Double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**this is the getter and setter for the outsourced parts. it sets the company name that the user fills out after selecting the outsourced radio button**/
    public String getCompanyName(){
        return companyName;
    }
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
}
