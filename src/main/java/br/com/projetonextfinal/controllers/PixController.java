package br.com.projetonextfinal.controllers;

import br.com.projetonextfinal.Application;
import br.com.projetonextfinal.models.Conta;
import br.com.projetonextfinal.models.ContaBO;
import br.com.projetonextfinal.models.PixBO;
import br.com.projetonextfinal.models.TipoChavePix;
import br.com.projetonextfinal.utils.Alerts;
import br.com.projetonextfinal.utils.BancoDeDados;
import br.com.projetonextfinal.utils.Constraints;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class PixController implements Initializable {


    public TextField txtConteudoCadastroChave;
    public TextField txtConteudoConsultaChave;
    public TextField txtValorChave;
    public TextField txtCampoPagamentoPix;
    public AnchorPane idPaneCadastro;
    public AnchorPane idPaneConsulta;
    public Label idLabelCPF;
    public Label idLabelNome;
    public Label idLabelValor;
    public AnchorPane idPanePagamento;
    public AnchorPane idPaneConta;

    public void limpaCampos(){
        txtConteudoCadastroChave.setText("");
        txtConteudoConsultaChave.setText("");
        txtValorChave.setText("");
        txtCampoPagamentoPix.setText("");
    }

    public void onBtCadastrarChaveAction(){
        fechaPanes();
        idPaneCadastro.setVisible(true);
    }
    
    public void fechaPanes(){
        limpaCampos();
        idPaneCadastro.setVisible(false);
        idPanePagamento.setVisible(false);
        idPaneConsulta.setVisible(false);
        idPaneConta.setVisible(false);
    }

    public void onBtConsultarChaveAction(){
        fechaPanes();
        idPaneConsulta.setVisible(true);
    }

    public void onBtShowChaveDados(){
        Conta contaExterna = BancoDeDados.findContabyChavePix(txtCampoPagamentoPix.getText());
        if (contaExterna == null){
            Alerts.showAlertError("Conta não encontrada", "Certifique-se de inserir uma chave válida...");
            return;
        }
        idLabelNome.setText("Nome: " + contaExterna.getCliente().getNome());
        idLabelCPF.setText("CPF: " + contaExterna.getCliente().getCpf());
        double valorAPagar = descobreValor(txtCampoPagamentoPix.getText(), contaExterna);
        String pagamento = String.format("%.2f", valorAPagar);
        idLabelValor.setText("Valor: " + pagamento);
        idPaneConta.setVisible(true);
    }

    public void consultaChaveAction(){
        fechaPanes();
        if (verificarTipoChavePix(txtConteudoConsultaChave.getText()) == null){
            fechaPanes();
            return;
        }
        if(verificarTipoChavePix(txtConteudoConsultaChave.getText()).equals(TipoChavePix.CPF)){
            if(Application.getConta().getChavesPix()[0] != null) {
                Alerts.showAlertConfirmation(
                        "Chave Cadastrada",
                        "Chave: " + Application.getConta().getChavesPix()[0].getPix().getConteudoChave(),
                        "Valor: " + Application.getConta().getChavesPix()[0].getPix().getValor());
            }else{
                Alerts.showAlertError("Chave não cadastrada", null, "Chave não cadastrada");
                fechaPanes();
                return;
            }
        }else if(verificarTipoChavePix(txtConteudoConsultaChave.getText()).equals(TipoChavePix.EMAIL)){
            if(Application.getConta().getChavesPix()[1] != null) {
                Alerts.showAlertConfirmation(
                    "Chave Cadastrada",
                    "Chave: " + Application.getConta().getChavesPix()[1].getPix().getConteudoChave(),
                    "Valor: " + Application.getConta().getChavesPix()[1].getPix().getValor());
            }else{
                Alerts.showAlertError("Chave não cadastrada", null, "Chave não cadastrada");
                fechaPanes();
                return;
            }
        }else if(verificarTipoChavePix(txtConteudoConsultaChave.getText()).equals(TipoChavePix.TELEFONE)){
            if(Application.getConta().getChavesPix()[2] != null) {
                Alerts.showAlertConfirmation(
                        "Chave Cadastrada",
                        "Chave: " + Application.getConta().getChavesPix()[2].getPix().getConteudoChave(),
                        "Valor: " + Application.getConta().getChavesPix()[2].getPix().getValor());
            }else{
                Alerts.showAlertError("Chave não cadastrada", null, "Chave não cadastrada");
                fechaPanes();
                return;
            }
        }else if(verificarTipoChavePix(txtConteudoConsultaChave.getText()).equals(TipoChavePix.ALEATORIO)){
            if(Application.getConta().getChavesPix()[3] != null) {
                Alerts.showAlertConfirmation(
                        "Chave Cadastrada",
                        "Chave: " + Application.getConta().getChavesPix()[3].getPix().getConteudoChave(),
                        "Valor: " + Application.getConta().getChavesPix()[3].getPix().getValor());
            }else {
                Alerts.showAlertError("Chave não cadastrada", "Chave não cadastrada");
                fechaPanes();
                return;
            }
        }
    }

    public void cadastraChaveAction(){
        TipoChavePix tipoChavePix = verificarTipoChavePix(txtConteudoCadastroChave.getText());
        if (tipoChavePix == null) {
            fechaPanes();
            return;
        }
        double valor;
        try {
            valor = Double.parseDouble(txtValorChave.getText());
        } catch (Exception e) {
            Alerts.showAlertError( null, "Numero Invalido...");
            return;
        }
        String chave;
        PixBO pixBO = new PixBO();
        if (TipoChavePix.CPF.equals(tipoChavePix)) {
            chave = Application.getConta().getCliente().getCpf();
        } else if (TipoChavePix.EMAIL.equals(tipoChavePix)) {
            chave = "emailpadrão@gmail.com";
        } else if (TipoChavePix.TELEFONE.equals(tipoChavePix)) {
            chave = "21 98765-4321";
        } else {
            Random random = new Random();
            chave = String.valueOf(random.nextLong(99999999));
        }
        if (!pixBO.ativarChave(tipoChavePix, valor, chave, Application.getConta())) {
            Alerts.showAlertError("Erro ao cadastrar chave", null, "Conta associada não existe");
            fechaPanes();
            return;
        }
        switch (tipoChavePix) {
            case CPF -> Application.getConta().getChavesPix()[0] = pixBO;
            case EMAIL -> Application.getConta().getChavesPix()[1] = pixBO;
            case TELEFONE -> Application.getConta().getChavesPix()[2] = pixBO;
            case ALEATORIO -> Application.getConta().getChavesPix()[3] = pixBO;
        }
        Alerts.showAlertConfirmation("Chave Criada", null, "Chave Criada com Sucesso!");
        fechaPanes();
    }

    public TipoChavePix verificarTipoChavePix(String chave) {
        String opcao = chave.toLowerCase(Locale.ROOT);
        switch (opcao) {
            case "cpf":
                return TipoChavePix.CPF;
            case "email":
                return TipoChavePix.EMAIL;
            case "telefone":
                return TipoChavePix.TELEFONE;
            case "aleatorio":
                return TipoChavePix.ALEATORIO;
            default:
                Alerts.showAlertError("Opção Inválida", null, "Por favor insira uma chave válida...");
                return null;
        }
    }

    public void onBtProcurarAction(){
        fechaPanes();
        idPanePagamento.setVisible(true);
    }

    public void transfereDinheiro(){
        Conta contaExterna = BancoDeDados.findContabyChavePix(txtCampoPagamentoPix.getText());
        double valorAPagar = descobreValor(txtCampoPagamentoPix.getText(), contaExterna);
        if(contaExterna == null || valorAPagar > Application.getConta().getSaldo()){
            Alerts.showAlertError("Transferência Inválida", null, "Saldo Insuficiente para Realizar Transção");
            idPanePagamento.setVisible(false);
        }else{
            ContaBO contaBO = new ContaBO(Application.getConta());
            contaBO.sacar(valorAPagar);
            contaBO.setConta(contaExterna);
            contaBO.depositar(valorAPagar);
            Alerts.showAlertConfirmation("Pagamento feito com Sucesso",
                    contaExterna.getCliente().getNome()+" recebeu seu pagamento!",
                    "Pagamento no valor de: R$"+valorAPagar);
            fechaPanes();
        }
    }

    public double descobreValor(String chave, Conta conta){
        if(conta.getChavesPix()[0] != null &&
                conta.getChavesPix()[0].getPix().getConteudoChave().equals(chave)) {
            return conta.getChavesPix()[0].getPix().getValor();
        }else if(conta.getChavesPix()[1] != null &&
                conta.getChavesPix()[1].getPix().getConteudoChave().equals(chave)) {
            return conta.getChavesPix()[1].getPix().getValor();
        }else if(conta.getChavesPix()[2] != null &&
                conta.getChavesPix()[2].getPix().getConteudoChave().equals(chave)) {
            return conta.getChavesPix()[2].getPix().getValor();
        }else if(conta.getChavesPix()[3] != null &&
                conta.getChavesPix()[3].getPix().getConteudoChave().equals(chave)) {
            return conta.getChavesPix()[3].getPix().getValor();
        }else{
            return -1;
        }

    }

    public void onBtVoltarAction(){
        limpaCampos();
        Application.changeScene("transferencia");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Constraints.setTextFieldLetters(txtConteudoCadastroChave);
        Constraints.setTextFieldLetters(txtConteudoConsultaChave);
        Constraints.setTextFieldDouble(txtValorChave);
    }
}
