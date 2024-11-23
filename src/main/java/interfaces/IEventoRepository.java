package interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import entidades.Evento;

public interface IEventoRepository {
    void salvar(Evento evento) throws SQLException;
    void alterar(Evento evento, String tipoEvento) throws SQLException;
    ArrayList<Evento> listarEventos () throws SQLException;
    void excluir(Evento eventoSelecionado) throws SQLException;
}
