package entidades;

import java.util.ArrayList;

public class Cliente {
    private String cpf;
    private String nome;
    private int idade;
    private double dinheiro;
    private int codEvento;
    private ArrayList<String> telefones;

    public Cliente(String cpf, String nome, int idade, double dinheiro, int codEvento) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
        this.dinheiro = dinheiro;
        this.codEvento = codEvento;
        this.telefones = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void addTelefone(String telefone) {
        this.telefones.add(telefone);
    }

    public ArrayList<String> listarTelefones() {
        return this.telefones;
    }

    public void setTelefones(ArrayList<String> telefones) {
        this.telefones = telefones;
    }

    public void setCodEvento(int codEvento) {
        this.codEvento = codEvento;
    }

    public int getCodEvento() {
        return this.codEvento;
    }

}
