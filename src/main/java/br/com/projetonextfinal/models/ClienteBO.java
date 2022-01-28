package br.com.projetonextfinal.models;

import java.util.Date;

public class ClienteBO {
    private Cliente cliente = new Cliente();

    public ClienteBO(String cpf, String nome, Date dataDeNascimento, Endereco endereco, String email, String telefone) {
        cadastrarDados(nome, cpf,dataDeNascimento, endereco, email, telefone);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    private void cadastrarDados(String nome, String cpf, Date dataDeNascimento, Endereco endereco, String email, String telefone){
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setNome(nome);
        cliente.setDataDeNascimento(dataDeNascimento);
        cliente.setEndereco(endereco);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);
    }

    public static boolean checkCPF(String cpf){
        if(cpf.length() == 11) {
            for (int i = 0; i < cpf.length(); i++) {
                if(!Character.isDigit(cpf.charAt(i))){
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }
}
