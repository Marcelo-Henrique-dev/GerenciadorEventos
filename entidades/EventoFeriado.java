package entidades;
import entidades.enums.TipoEvento;

public class EventoFeriado extends Evento {
    private String motivo;

    public EventoFeriado(int id, String nome, String data, double valorIngresso, int quantidadeVagas, String motivo) {
        super(id, nome, data, valorIngresso, quantidadeVagas, TipoEvento.EVENTOFERIADO);
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
