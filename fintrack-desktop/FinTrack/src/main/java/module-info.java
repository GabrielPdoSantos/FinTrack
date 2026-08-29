module br.com.gabriel.fintrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;


    opens br.com.gabriel.fintrack.controller to javafx.fxml;
    opens br.com.gabriel.fintrack.model to javafx.base;
    opens views to javafx.fxml;
    exports br.com.gabriel.fintrack.app;
}