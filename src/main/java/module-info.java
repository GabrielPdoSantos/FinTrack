module br.com.gabriel.fintrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens br.com.gabriel.fintrack.app to javafx.fxml;
    exports br.com.gabriel.fintrack.app;
}