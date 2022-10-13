package sharrow.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Home extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Inventory.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Home Screen");
        stage.setScene(scene);
        stage.show();
    }

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