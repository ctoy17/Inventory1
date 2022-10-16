package sharrow.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**This class creates the inventory application
 * FUTURE ENHANCEMENT: Allow user to attach images of parts to easily identify the EXACT part that is being referenced
 * this way if there were two parts with similar names, the user would be able to know what one by verifying with an image.
 * RUNTIME ERROR: my modify part / product method was not operating correctly. My logic was correct, but did not realize I had a mismatch of naming conventions so when i tried
 * to update, the system was unsure of what variable i was referencing.
 *
 * I also had a problem where I was receiving an error due to storing the product name as an integer instead of a string.
 * I quickly saw that it said int, rather than string, so was able to rectify before too many issues arose from the wrong data type.
 * When i went to add a product, everything loaded except the name, so i knew that there was something wrong with how it was stored.**/
public class Home extends Application {
    /**This method is the home page GUI. It is the first screen the user sees**/
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Inventory.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Home Screen");
        stage.setScene(scene);
        stage.show();
    }

    /** This method generates test data and is the primary method of launching the application**/
    public static void main(String[] args) {
        int partID = Inventory.getNewId();
        InHouse notebook = new InHouse(partID, "Notebook", 7.99, 15, 1, 20, 4);
        partID = Inventory.getNewId();
        InHouse bluePen = new InHouse(partID, "Blue Pens", 3.99, 17, 1, 25, 4);
        partID = Inventory.getNewId();
        InHouse mechPencil = new InHouse(partID, "Mechanical Pencils", 3.49, 13, 1, 25, 4);
        partID = Inventory.getNewId();
        InHouse scissor = new InHouse(partID, "Scissors", 6.39, 10, 1, 15, 4);
        partID = Inventory.getNewId();
        Outsourced tablets = new Outsourced(partID, "Tablets", 249.00, 20, 1, 25, "Apple");
        partID = Inventory.getNewId();
        Outsourced mouse = new Outsourced(partID, "Mouse", 26.99, 20, 1, 25, "Apple");
        partID = Inventory.getNewId();
        Outsourced charger = new Outsourced(partID, "Charger", 19.99, 20, 1, 25, "Apple");
        partID = Inventory.getNewId();
        Outsourced dockStation = new Outsourced(partID, "Docking Station", 39.00, 20, 1, 25, "Amazon");
        Inventory.addNewPart(notebook);
        Inventory.addNewPart(bluePen);
        Inventory.addNewPart(mechPencil);
        Inventory.addNewPart(scissor);
        Inventory.addNewPart(tablets);
        Inventory.addNewPart(mouse);
        Inventory.addNewPart(charger);
        Inventory.addNewPart(dockStation);
        int prodID = Inventory.getNewProductId();
        Product classroom = new Product(prodID, "Classroom Supplies Bundle", 375.00, 24,1,45);
        classroom.addAssociatedPart(notebook);
        classroom.addAssociatedPart(bluePen);
        classroom.addAssociatedPart(mechPencil);
        classroom.addAssociatedPart(scissor);
        classroom.addAssociatedPart(tablets);
        classroom.addAssociatedPart(mouse);
        classroom.addAssociatedPart(charger);
        classroom.addAssociatedPart(dockStation);
        Inventory.addNewProduct(classroom);
        launch(args);
    }
}