package br.com.projetonextfinal.models;

public enum TipoCliente {
    COMUM(0), SUPER(1), PREMIUM(2);

    private final int id;

    TipoCliente(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
