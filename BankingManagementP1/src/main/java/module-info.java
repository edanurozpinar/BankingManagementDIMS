module com.sau.bankingmanagementp1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires static lombok;
    requires java.sql;

    opens com.sau.bankingmanagementp1 to javafx.fxml;
    exports com.sau.bankingmanagementp1;
    exports com.sau.bankingmanagementp1.controller;
    opens com.sau.bankingmanagementp1.controller to javafx.fxml;
}