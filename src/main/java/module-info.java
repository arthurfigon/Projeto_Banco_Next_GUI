module com.example.nextgroup {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.com.projetonextfinal to javafx.fxml;
    exports br.com.projetonextfinal;
    exports br.com.projetonextfinal.controllers;
    opens br.com.projetonextfinal.controllers to javafx.fxml;
}