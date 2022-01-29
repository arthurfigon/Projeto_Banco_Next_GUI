package br.com.projetonextfinal.controllers;

import br.com.projetonextfinal.Application;
import br.com.projetonextfinal.models.Conta;
import br.com.projetonextfinal.models.ContaBO;
import br.com.projetonextfinal.utils.Alerts;
import br.com.projetonextfinal.utils.BancoDeDados;
import br.com.projetonextfinal.utils.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML
    public Label labelSaldo;
    public TextField txtValorDeposito;
    public TextField txtValorTransferencia;
    public Label idTipoCliente;
    public Label idTipoConta;
    public AnchorPane idPaneDeposito;
    public AnchorPane idPaneTransferencia;

    List<Conta> listConta;

    @FXML
    private ComboBox<Conta> contaComboBox;

    private ObservableList<Conta> observableList;

    private Conta contaSelecionada;

    public void limpaTodosCampos(){
        idTipoCliente.setText("");
        idTipoConta.setText("");
        labelSaldo.setText("");
        txtValorDeposito.setText("");
        txtValorTransferencia.setText("");
    }

    public void limpaCampos(){
        txtValorDeposito.setText("");
        txtValorTransferencia.setText("");
    }

    @FXML
    public void onContaComboBoxAction(){
        this.contaSelecionada = contaComboBox.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void onBtDepositoAction(){
        fecharPanes();
        idPaneDeposito.setVisible(true);
        atualizaDados();
    }

    @FXML
    public void onBtTransferenciaAction(){
        fecharPanes();
        idPaneTransferencia.setVisible(true);
        atualizaDados();
    }

    @FXML
    public void onBtDepositarAction(){
        ContaBO contaBO = new ContaBO(Application.getConta());
        contaBO.depositar(Double.valueOf(txtValorDeposito.getText()));
        atualizaDados();
        fecharPanes();
        limpaCampos();
    }

    @FXML
    public void onBtTransferirAction(){
        ContaBO contaBO = new ContaBO(Application.getConta());
        ContaBO contaBOExterna = new ContaBO(contaSelecionada);
        if(contaSelecionada == null){
            Alerts.showAlertError("Conta Não Selecionada",null, "Por favor, selecione uma conta...");
            return;
        }
        if(!contaBO.transferir(Double.valueOf(txtValorTransferencia.getText()), contaBOExterna)){
            Alerts.showAlertError("Saldo Indisponível...", null, "Você não tem saldo suficiente para realizar essa operação...");
        }
        atualizaDados();
        fecharPanes();
        limpaCampos();
    }

    public void atualizaDados(){
        String saldo = String.format("%.2f", Application.getConta().getSaldo());
        labelSaldo.setText("R$ "+saldo);
        idTipoCliente.setText(Application.getConta().getCliente().getTipo().name());
        idTipoConta.setText("Conta "+Application.getConta().getTipoConta().name());
    }

    public void fecharPanes(){
        aplicarTaxasConta();
        idPaneDeposito.setVisible(false);
        idPaneTransferencia.setVisible(false);
    }

    public void deslogar(){
        limpaTodosCampos();
        Application.changeScene("main");
    }

    public void aplicarTaxasContas() {
        ArrayList<Conta> contas = BancoDeDados.returnTodasContas();
        for (Conta conta : contas) {
            ContaBO continha= new ContaBO(conta);
            continha.aplicaTaxa();
        }
        Alerts.showAlertConfirmation("Taxas",null, "Taxas aplicadas a todas as contas...");
    }

    // Verificada ao usuário clicar em Depósito ou Transferência
    public void aplicarTaxasConta() {

        ContaBO conta = new ContaBO(Application.getConta());
        conta.aplicaTaxa();

    }

    @FXML
    public void onBtAtualizarAction(){
        listConta = BancoDeDados.returnTodasContas();
        observableList = FXCollections.observableList(listConta);
        contaComboBox.setItems(observableList);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listConta = BancoDeDados.returnTodasContas();

        Constraints.setTextFieldDouble(txtValorDeposito);
        Constraints.setTextFieldDouble(txtValorTransferencia);

        observableList = FXCollections.observableList(listConta);
        contaComboBox.setItems(observableList);
    }

    @FXML
    public void onBtAreaPixAction() {
        limpaCampos();
        Application.changeScene("pix");
    }

    @FXML
    public void onBtCartoesAction() {
        limpaCampos();
        Application.changeScene("cartoes");
    }
}
