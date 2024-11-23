package entidades;

import entidades.enums.TipoEvento;

public class EventoReuniao extends Evento {
    private String assunto;

    public EventoReuniao(int id, String nome, String data, double valorIngresso, int quantidadeVagas, String assunto) {
        super(id, nome, data, valorIngresso, quantidadeVagas, TipoEvento.EVENTOREUNIAO);
        this.assunto = assunto;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    
}
