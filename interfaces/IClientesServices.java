package interfaces;

import java.sql.SQLException;

public interface IClientesServices {
    public void cadastrarCliente() throws SQLException;
    public void editarCliente() throws SQLException;
    public void deletarCliente() throws SQLException;
    public void listarClientesPorEvento() throws SQLException;
}
