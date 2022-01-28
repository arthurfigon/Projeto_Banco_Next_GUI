package br.com.projetonextfinal;

import br.com.projetonextfinal.models.Conta;
import br.com.projetonextfinal.utils.BancoDeDados;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private static Stage stage;

    private static Scene sceneInicial;
    private static Scene sceneTransferencia;
    private static Scene sceneRegistrar;
    private static Scene scenePix;
    private static Scene sceneCartoes;
    private static Scene sceneApolices;
    private static Conta conta;

    public static Conta getConta() {
        return conta;
    }

    public static void setConta(Conta conta) {
        Application.conta = conta;
    }

    @Override
    public void start(Stage stage) throws IOException {

        Application.stage = stage;
        Application.stage.setTitle("Next");

        FXMLLoader fxmlLoaderInicial = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        sceneInicial = new Scene(fxmlLoaderInicial.load());

        FXMLLoader fxmlLoaderTransferencia = new FXMLLoader(Application.class.getResource("principal-view.fxml"));
        sceneTransferencia = new Scene(fxmlLoaderTransferencia.load());

        FXMLLoader fxmlLoaderRegistrar = new FXMLLoader(Application.class.getResource("registrar-view.fxml"));
        sceneRegistrar = new Scene(fxmlLoaderRegistrar.load());

        FXMLLoader fxmlLoaderPix = new FXMLLoader(Application.class.getResource("pix-view.fxml"));
        scenePix = new Scene(fxmlLoaderPix.load());

        FXMLLoader fxmlLoaderCartoes = new FXMLLoader(Application.class.getResource("cartoes-view.fxml"));
        sceneCartoes = new Scene(fxmlLoaderCartoes.load());

        FXMLLoader fxmlLoaderApolices = new FXMLLoader(Application.class.getResource("apolices-view.fxml"));
        sceneApolices = new Scene(fxmlLoaderApolices.load());

        stage.setTitle("Web Banking Next");
        stage.setScene(sceneInicial);
        stage.show();
    }

    public static void changeScene(String scene){
        switch (scene){
            case "main" -> stage.setScene(sceneInicial);
            case "registrar" -> stage.setScene(sceneRegistrar);
            case "transferencia" -> stage.setScene(sceneTransferencia);
            case "pix" -> stage.setScene(scenePix);
            case "cartoes" -> stage.setScene(sceneCartoes);
            case "apolices" -> stage.setScene(sceneApolices);
            default -> {
            }
        }
    }

    public static void main(String[] args) {
        BancoDeDados.insereContasPadrao();
        launch();
    }
}