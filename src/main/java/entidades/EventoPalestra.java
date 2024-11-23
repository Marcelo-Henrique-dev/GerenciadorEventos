package entidades;
import entidades.enums.TipoEvento;

public class EventoPalestra extends Evento {
    private String palestrante;

    public EventoPalestra(int id, String nome, String data, double valorIngresso, int quantidadeVagas, String palestrante) {
        super(id, nome, data, valorIngresso, quantidadeVagas, TipoEvento.EVENTOPALESTRA);
        this.palestrante = palestrante;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }
    
}
