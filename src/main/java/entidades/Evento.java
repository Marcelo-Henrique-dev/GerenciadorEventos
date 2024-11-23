package entidades;

import entidades.enums.TipoEvento;
import java.util.ArrayList;

public abstract class Evento {
    private int id;
    private String nome;
    private String dataEvento;
    private TipoEvento tipoEvento;
    private double valorIngresso;
    private int quantidadeVagas;
    private ArrayList<Cliente> pessoas;

    public Evento(int id, String nome, String data, double valorIngresso, int quantidadeVagas, TipoEvento tipoEvento) {
        this.id = id;
        this.nome = nome;
        this.dataEvento = data;
        this.valorIngresso = valorIngresso;
        this.quantidadeVagas = quantidadeVagas;
        this.tipoEvento = tipoEvento;
        this.pessoas = new ArrayList<Cliente>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return dataEvento;
    }

    public void setData(String data) {
        this.dataEvento = data;
    }

    public double getValorIngresso() {
        return valorIngresso;
    }

    public void setValorIngresso(double valorIngresso) {
        this.valorIngresso = valorIngresso;
    }

    public int getQuantidadeVagas() {
        return quantidadeVagas;
    }

    public void setQuantidadeVagas(int quantidadeVagas) {
        this.quantidadeVagas = quantidadeVagas;
    }

    public TipoEvento getTipoEvento(){
        return this.tipoEvento;
    }

    public ArrayList<Cliente> listarPessoas(){
        return this.pessoas;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
   
}
