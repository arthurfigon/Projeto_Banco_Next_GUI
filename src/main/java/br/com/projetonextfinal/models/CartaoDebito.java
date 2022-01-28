package br.com.projetonextfinal.models;

public class CartaoDebito extends Cartao{

    private double limitePorTransacao;

    public CartaoDebito(String bandeira, String senha, double limitePorTransacao) {
        super(bandeira, senha, true);
        this.limitePorTransacao = limitePorTransacao;
    }

    public double getLimitePorTransacao() {
        return limitePorTransacao;
    }

    public void setLimitePorTransacao(double limitePorTransacao) {
        this.limitePorTransacao = limitePorTransacao;
    }
}
