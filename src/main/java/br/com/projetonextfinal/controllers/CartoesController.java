package br.com.projetonextfinal.controllers;

import br.com.projetonextfinal.Application;
import br.com.projetonextfinal.models.CartaoCredito;
import br.com.projetonextfinal.models.CartaoDebito;
import br.com.projetonextfinal.models.Compra;
import br.com.projetonextfinal.utils.Alerts;
import br.com.projetonextfinal.utils.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CartoesController implements Initializable {


    public Label idNumeroCartao;
    public Label idNomeCartao;
    public Label idFatura;
    public Label idFaturaVencimento;

    public TextField campoSenhaCredito;
    public TextField campoBandeiraCredito;
    public TextField campoDiaCredito;
    public TextField campoSenhaDebito;
    public TextField campoBandeiraDebito;
    public TextField campoLimiteDebito;
    public TextField campoProdutoCredito;
    public TextField campoValorCredito;
    public TextField campoProdutoDebito;
    public TextField campoValorDebito;

    public AnchorPane idPaneOpcoesCredito;
    public AnchorPane idPaneOpcoesDebito;
    public AnchorPane idPaneCadastrarCredito;
    public AnchorPane idPaneCompraCredito;
    public AnchorPane idPaneCompraDebito;
    public AnchorPane idPaneCadastrarDebito;
    public AnchorPane idPaneFatura;


    public void atualizaDadosCredito(){
        if(Application.getConta().getCartaoCredito() != null){
            idNomeCartao.setText(Application.getConta().getCliente().getNome());
            idNumeroCartao.setText(Application.getConta().getCartaoCredito().getNumero());
            idPaneFatura.setVisible(true);
            idFatura.setText("R$ " + Application.getConta().getCartaoCredito().getValorFatura());
            idFaturaVencimento.setText("Vencimento: " + Application.getConta().getCartaoCredito().getDateString());
        }
    }

    public void atualizaDadosDebito(){
        if(Application.getConta().getCartaoDebito() != null){
            idNomeCartao.setText(Application.getConta().getCliente().getNome());
            idNumeroCartao.setText(Application.getConta().getCartaoDebito().getNumero());
        }
    }

    public void limpaTudo(){
        campoSenhaCredito.setText("");
        campoBandeiraCredito.setText("");
        campoDiaCredito.setText("");
        campoSenhaDebito.setText("");
        campoBandeiraDebito.setText("");
        campoLimiteDebito.setText("");
        campoProdutoCredito.setText("");
        campoValorCredito.setText("");
        campoProdutoDebito.setText("");
        campoValorDebito.setText("");
    }

    public void fechaTudo(){
        limpaTudo();
        idPaneOpcoesCredito.setVisible(false);
        idPaneOpcoesDebito.setVisible(false);
        idPaneCadastrarCredito.setVisible(false);
        idPaneCompraCredito.setVisible(false);
        idPaneCompraDebito.setVisible(false);
        idPaneCadastrarDebito.setVisible(false);
        idPaneFatura.setVisible(false);
    }

    public void onBtVoltarAction(){
        Application.changeScene("transferencia");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Constraints.setTextFieldInteger(campoSenhaCredito);
        Constraints.setTextFieldMaxLength(campoSenhaCredito, 6);
        Constraints.setTextFieldLetters(campoBandeiraCredito);
        Constraints.setTextFieldInteger(campoDiaCredito);
        Constraints.setTextFieldMaxLength(campoDiaCredito, 2);

        Constraints.setTextFieldDouble(campoValorCredito);

        Constraints.setTextFieldInteger(campoSenhaDebito);
        Constraints.setTextFieldMaxLength(campoSenhaDebito, 6);
        Constraints.setTextFieldLetters(campoBandeiraDebito);
        Constraints.setTextFieldDouble(campoLimiteDebito);

        Constraints.setTextFieldDouble(campoValorDebito);

    }

    // CRÉDITO METHODS

    @FXML
    public void onBtCreditoAction(){
        fechaTudo();
        idPaneOpcoesCredito.setVisible(true);
        atualizaDadosCredito();
    }

    @FXML
    public void onSelecionarCadastrarCredito(){
        fechaTudo();
        idPaneCadastrarCredito.setVisible(true);
    }

    @FXML
    public void onBtCadastrarCreditoAction(){
        String senha = campoSenhaCredito.getText();
        String bandeira = campoBandeiraCredito.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date data = null;
        try {
            data = sdf.parse(campoDiaCredito.getText().strip());
        } catch (ParseException e) {
            Alerts.showAlertError("Erro no Dia de Vencimento", "Data de Validação Inválida", "Por favor, insira uma Data Válida...");
            idPaneCadastrarCredito.setVisible(false);
            return;
        }

        CartaoCredito cartaoCredito = new CartaoCredito(bandeira, senha, data);
        Application.getConta().setCartaoCredito(cartaoCredito);
        idPaneCadastrarCredito.setVisible(false);
        atualizaDadosCredito();
    }

    public void onBtSegurosAction(){
        if(Application.getConta().getCartaoCredito() != null) {
            fechaTudo();
            Application.changeScene("apolices");
        }else{
            Alerts.showAlertError("Cartão Não Cadastrado", null, "Cartão de Crédito não cadastrado...");
        }
    }

    @FXML
    public void onBtComprarAction(){
        if(Application.getConta().getCartaoCredito() != null) {
            fechaTudo();
            idPaneCompraCredito.setVisible(!idPaneCompraCredito.isVisible());
        }else{
            Alerts.showAlertError("Cartão Não Cadastrado", null, "Cartão de Crédito não cadastrado...");
        }
    }

    public void onBtConfirmarCompraAction(){
        Compra compra = new Compra(campoProdutoCredito.getText(),Double.parseDouble(campoValorCredito.getText()));
        Application.getConta().getCartaoCredito().addCompra(compra);
        fechaTudo();
    }

    public void onBtPagarFaturaAction(){
        if(Application.getConta().getCartaoCredito() != null) {
            if(Application.getConta().getCartaoCredito().pagarFatura(Application.getConta())) {
                Alerts.showAlertConfirmation("Sucesso", null, "Fatura paga com sucesso!");
            }else{
                Alerts.showAlertError("Erro no Pagamento", null, "Saldo Insuficiente...");
            }
        }else{
            Alerts.showAlertError("Cartão Não Cadastrado", null, "Cartão de Crédito não cadastrado...");
        }
    }

    public void onBtBloquearCreditoAction(){
        Application.getConta().setCartaoCredito(null);
        fechaTudo();
    }


    // DEBIT METHODS

    @FXML
    public void onBtDebitoAction(){
        atualizaDadosDebito();
        fechaTudo();
        idPaneOpcoesDebito.setVisible(!idPaneOpcoesDebito.isVisible());
    }

    @FXML
    public void onSelecionarCadastrarDebito(){
        fechaTudo();
        idPaneCadastrarDebito.setVisible(true);
    }

    @FXML
    public void onBtCadastrarDebitoAction(){
        String senha = campoSenhaDebito.getText();
        String bandeira = campoBandeiraDebito.getText();
        double limite = Double.parseDouble(campoLimiteDebito.getText());

        CartaoDebito cartaoDebito = new CartaoDebito(bandeira, senha, limite);
        Application.getConta().setCartaoDebito(cartaoDebito);
        fechaTudo();
    }


    public void onBtComprarDebitoAction() {
        if(Application.getConta().getCartaoDebito() != null) {
            fechaTudo();
            idPaneCompraDebito.setVisible(true);
        }else{
            Alerts.showAlertError("Cartão Não Cadastrado", null, "Cartão de Débito não cadastrado...");
        }
    }

    public void onBtPagarCompraDebito(){
        if(Double.parseDouble(campoValorDebito.getText())
                > Application.getConta().getCartaoDebito().getLimitePorTransacao()){
            Alerts.showAlertError(
                    "Valor Inválido",
                    null,
                    "Seu limite é menor que o valor da compra...");
        }
        else if(Application.getConta().getSaldo() < Double.parseDouble(campoValorDebito.getText())){
            Alerts.showAlertError(
                    "Valor Indisponível",
                    null,
                    "Seu saldo não é suficiente para realizar a compra...");
        }else{
            Application.getConta().setSaldo(
                    Application.getConta().getSaldo()
                            - Double.parseDouble(campoValorDebito.getText()));
            Alerts.showAlertConfirmation(
                    "Sucesso",
                    null,
                    "Compra realizada com Sucesso!");
            fechaTudo();
        }
    }

    public void onBtBloquearDebitoAction(){
        Application.getConta().setCartaoDebito(null);
        fechaTudo();
    }
}
