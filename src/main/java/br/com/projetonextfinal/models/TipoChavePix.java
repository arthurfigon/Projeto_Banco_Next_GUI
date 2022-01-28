package br.com.projetonextfinal.models;

public enum TipoChavePix {
    CPF(0), EMAIL(1), TELEFONE(2), ALEATORIO(3);

    private final int id;

    TipoChavePix(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
