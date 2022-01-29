package br.com.projetonextfinal.controllers;

import br.com.projetonextfinal.Application;
import br.com.projetonextfinal.models.Conta;
import br.com.projetonextfinal.models.TipoConta;
import br.com.projetonextfinal.utils.Alerts;
import br.com.projetonextfinal.utils.BancoDeDados;
import br.com.projetonextfinal.utils.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField campoSenha;
    @FXML
    private TextField campoLogin;

    @FXML
    protected void onBtRegistrarAction(){
        limpaCampos();
        Application.changeScene("registrar");
    }

    @FXML
    protected void onBtLoginCorrenteAction(){
        Conta conta;
        conta = BancoDeDados.findContaByCPF(campoLogin.getText(), TipoConta.CORRENTE);
        if(conta == null){
            Alerts.showAlertError("CPF Inexistente", null, "CPF não cadastrado");
            return;
        }
        if(conta.getCliente().getCpf().equals(campoLogin.getText()) && conta.getSenha().equals(campoSenha.getText())){
            Application.setConta(conta);
            limpaCampos();
            Application.changeScene("transferencia");
        }else{
            recusado();
            return;
        }
        limpaCampos();
    }

    @FXML
    protected void onBtLoginPoupancaAction(){
        Conta conta;
        conta = BancoDeDados.findContaByCPF(campoLogin.getText(), TipoConta.CORRENTE);
        conta = BancoDeDados.findContaByCPF(campoLogin.getText(), TipoConta.POUPANCA);
        if(conta == null){
            Alerts.showAlertError("CPF Inexistente", null, "CPF não cadastrado");
            return;
        }
        if(conta.getCliente().getCpf().equals(campoLogin.getText()) && conta.getSenha().equals(campoSenha.getText())){
            Application.setConta(conta);
            limpaCampos();
            Application.changeScene("transferencia");
        }else{
            recusado();
            return;
        }
    }

    public void limpaCampos(){
        campoLogin.setText("");
        campoSenha.setText("");
    }

    @FXML
    protected void recusado(){
        Alerts.showAlertError("Dados Inválidos","CPF ou Senha Inválidos");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Constraints.setTextFieldInteger(campoLogin);
        Constraints.setTextFieldMaxLength(campoLogin, 11);
        Constraints.setTextFieldMaxLength(campoSenha, 16);
    }
}