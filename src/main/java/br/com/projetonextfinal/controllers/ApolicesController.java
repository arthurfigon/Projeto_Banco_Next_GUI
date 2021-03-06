package br.com.projetonextfinal.controllers;

import br.com.projetonextfinal.Application;
import br.com.projetonextfinal.models.Apolice;
import br.com.projetonextfinal.models.Seguro;
import br.com.projetonextfinal.utils.Alerts;
import br.com.projetonextfinal.utils.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class ApolicesController implements Initializable {

    public ComboBox<Seguro> idCbSeguros;
    public TextField idTxfAnos;
    public AnchorPane idPane;
    public Label idLbSeguro;
    public Label idLbValor;
    private List<Seguro> listApolices = new ArrayList<>();
    private ObservableList<Seguro> observableList;
    private Seguro seguroSelecionado;

    public void limpaCampos(){
        idTxfAnos.setText("");
    }

    public void voltar(){
        Application.changeScene("cartoes");
    }

    public void selecionarNaLista(){
        this.seguroSelecionado = idCbSeguros.getSelectionModel().getSelectedItem();
        if(seguroSelecionado == null){
            return;
        }
        idLbSeguro.setText("Seguro: "+seguroSelecionado.getNome());
        idLbValor.setText("Valor por ano: "+String.valueOf(seguroSelecionado.getValorAno()));
        idPane.setVisible(true);
    }

    public void receberSeguro(){
        for(Apolice apolice: Application.getConta().getCartaoCredito().getApolices()){
            if(apolice.getSeguro().equals(seguroSelecionado)){
                Application.getConta().setSaldo(
                        Application.getConta().getSaldo()
                                + apolice.getValorApolice()*2);
                Application.getConta().getCartaoCredito().getApolices().remove(apolice);
                Alerts.showAlertConfirmation("Seguro resgatado", null, "Seu seguro foi resgatado com sucesso!");
                return;
            }
        }
        Alerts.showAlertError("Apolice n??o contratada", null, "Ap??lice n??o contratada...");
    }

    public void atualizarLista(){
        observableList = FXCollections.observableList(listApolices);
        idCbSeguros.setItems(observableList);
    }

    public void confirmarSeguro(){
        Calendar cal = Calendar.getInstance();
        int anosContratacao = Integer.valueOf(idTxfAnos.getText()); // dar get.text em textfield Anos a serem contratados
        cal.add(Calendar.DAY_OF_MONTH,15);

        Apolice apolice = new Apolice(
                String.valueOf(listApolices.size()),
                seguroSelecionado.getValorAno() * anosContratacao,
                seguroSelecionado.getRegras(),
                seguroSelecionado,
                Calendar.getInstance().getTime(),
                cal.getTime());

        if (Application.getConta().getSaldo() < seguroSelecionado.getValorAno() * anosContratacao){
            Alerts.showAlertError("Saldo na Contrata????o", "Saldo Insuficiente para Contrata????o...");
            return;
        }

        Application.getConta().getCartaoCredito().addApolice(apolice);
        Alerts.showAlertConfirmation("Seguro Contratado", "Seguro contratado com Sucesso!");
        idPane.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> regrasSeguroVida = new ArrayList<>();
        List<String> regrasSeguroInvalidez = new ArrayList<>();
        List<String> regrasSeguroDesemprego = new ArrayList<>();

        Constraints.setTextFieldInteger(idTxfAnos);

        regrasSeguroVida.add("Indeniza????o por despseas m??dico-hospitalares," +
                " ou por perda parcial ou total do funcionmanto ddos membros ou org??os.");
        regrasSeguroVida.add("Reembolso de custos em diagn??sticos de doen??as graves," +
                "como infarto, acidente vascular cerebral e c??ncer.");
        regrasSeguroVida.add("Assist??ncia funreal, para voc?? e a sua fam??lia.");


        regrasSeguroInvalidez.add("Invalidez Parcial: Ocorre quando h?? uma perda parcial das fun????es." +
                "Por exemplo, uma pessoa que sofre um acidente e perda a vis??o em um s?? dos olhos.");
        regrasSeguroInvalidez.add("Invalidez Total: Ocorre quando h?? uma perda total das fun????es." +
                "Intuitivamente, um bom exemplo seria o caso onde a pessoa sofre um acidente e perde" +
                " a vis??o em ambos os olhos.");

        regrasSeguroDesemprego.add("Necess??rio trabalhar com registro CLT, com tempo m??nimo" +
                " de estabilidade de 12 meses.");
        regrasSeguroDesemprego.add("V??lido apenas para desligamentos involut??rios e sem justa causa.");
        regrasSeguroDesemprego.add("N??o ?? valido em caso de demiss??o em massa (10% ou mais de demiss??es" +
                " simult??neas) ou fal??ncia/encerramento das atividades.");



        Seguro seguroVida = new Seguro(
                "01",
                "Seguro de Vida Next",
                regrasSeguroVida,
                36.00);

        listApolices.add(seguroVida);


        Seguro seguroInvalidez = new Seguro(
                "02",
                "Seguro de Invalidez Next",
                regrasSeguroInvalidez,
                26.00);

        listApolices.add(seguroInvalidez);

        Seguro seguroDesemprego = new Seguro(
                "03",
                "Seguro Desemprego Next",
                regrasSeguroDesemprego,
                16.00);

        listApolices.add(seguroDesemprego);


        observableList = FXCollections.observableList(listApolices);
        idCbSeguros.setItems(observableList);
        idPane.setVisible(false);
        limpaCampos();
    }
}
