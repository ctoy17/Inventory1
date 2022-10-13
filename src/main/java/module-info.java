module sharrow.inventory {
    requires javafx.controls;
    requires javafx.fxml;


    opens sharrow.inventory to javafx.fxml;
    exports sharrow.inventory;
}