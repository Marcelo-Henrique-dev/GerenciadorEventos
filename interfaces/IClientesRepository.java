package interfaces;

import java.util.ArrayList;

import entidades.Cliente;
import entidades.Evento;

public interface IClientesRepository {
    public void cadastroParticipanteSql(Evento evento, Cliente cliente);
    public void editarParticipanteSql(String cpfCliente, String nome, int idade, double dinheiro, ArrayList<String> telefones);
    public ArrayList<Cliente> listarParticipantesPorEventoSql(Evento eventoSelecionado);
    public void deletarClienteSql(int codEvento, String cpfCliente);
}
