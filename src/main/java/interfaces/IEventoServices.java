package interfaces;

import java.sql.SQLException;

import exceptions.InvalidEventoTypeException;

public interface IEventoServices {
    public void cadastrarEventoNoRepositorio() throws InvalidEventoTypeException, SQLException;
    public void listarEventos() throws SQLException;
    public void editarEventos() throws SQLException;
    public void apagarEvento() throws SQLException;
}
