module com.project3.project3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project3 to javafx.fxml;
    exports com.project3;
}