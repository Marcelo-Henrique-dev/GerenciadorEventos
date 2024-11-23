package repositorios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entidades.Cliente;
import entidades.Evento;
import interfaces.IClientesRepository;
import util.ConnectionSingleton;

public class RepositorioDeClientes implements IClientesRepository {

    private Connection connection;

    public RepositorioDeClientes() {
        try {
            this.connection = ConnectionSingleton.getInstance().conexao;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void cadastroParticipanteSql(Evento evento, Cliente cliente) {
        String checkSql = "SELECT COUNT(*) FROM cliente_evento WHERE codCliente = ? AND codEvento = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, cliente.getCpf());
            checkStmt.setInt(2, cliente.getCodEvento());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Cliente já cadastrado neste evento!");
                return;
            }
        } catch (Exception e) {
            System.out.println("Erro checando clientes: " + e);
        }

        String insertCliente = "INSERT INTO cliente(cpf, nome, idade, dinheiro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertCliente)) {
            insertStmt.setString(1, cliente.getCpf());
            insertStmt.setString(2, cliente.getNome());
            insertStmt.setInt(3, cliente.getIdade());
            insertStmt.setDouble(4, cliente.getDinheiro());
            insertStmt.executeUpdate();
            System.out.println("Cliente " + cliente.getNome() + " cadastrado em " + evento.getNome() + "!");
        } catch (Exception e) {
            System.out.println("Erro Cliente: " + e);
        }

        String insertClienteEvento = "INSERT INTO cliente_evento(codCliente, codEvento) VALUES (?, ?)";
        try (PreparedStatement stmtInsertClienteEvento = connection.prepareStatement(insertClienteEvento)) {
            stmtInsertClienteEvento.setString(1, cliente.getCpf());
            stmtInsertClienteEvento.setInt(2, evento.getId());
            stmtInsertClienteEvento.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro Cliente_Evento: " + e);
        }

        String insertTelefoneCliente = "INSERT INTO telefone_cliente(cpf_cliente, telefone) VALUES (?, ?)";
        for (String telefone : cliente.listarTelefones()) {
            try (PreparedStatement stmtTelefoneCliente = connection.prepareStatement(insertTelefoneCliente)) {
                stmtTelefoneCliente.setString(1, cliente.getCpf());
                stmtTelefoneCliente.setString(2, telefone);
                stmtTelefoneCliente.executeUpdate();
            } catch (Exception e) {
                System.out.println("Erro Telefone: " + e);
            }
        }

    }

    @Override
    public void deletarClienteSql(int codEvento, String cpfCliente) {
        // Deletar associações na tabela cliente_evento
        String deleteClienteEventoSql = "DELETE FROM cliente_evento WHERE codCliente = ? AND codEvento = ?";
        try (PreparedStatement stmtDeleteClienteEvento = connection.prepareStatement(deleteClienteEventoSql)) {
            stmtDeleteClienteEvento.setString(1, cpfCliente);
            stmtDeleteClienteEvento.setInt(2, codEvento);
            stmtDeleteClienteEvento.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro ao deletar cliente do evento: " + e);
            return;
        }
    
        // Deletar telefones dos clientes
        String deleteTelefoneClienteSql = "DELETE FROM telefone_cliente WHERE cpf_cliente = ?";
        try (PreparedStatement stmtDeleteTelefone = connection.prepareStatement(deleteTelefoneClienteSql)) {
            stmtDeleteTelefone.setString(1, cpfCliente);
            stmtDeleteTelefone.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro ao deletar telefones do cliente: " + e);
            return;
        }
    
        // Deletar cliente da tabela cliente
        String deleteClienteSql = "DELETE FROM cliente WHERE cpf = ?";
        try (PreparedStatement stmtDeleteCliente = connection.prepareStatement(deleteClienteSql)) {
            stmtDeleteCliente.setString(1, cpfCliente);
            stmtDeleteCliente.executeUpdate();
            System.out.println("Cliente deletado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao deletar cliente: " + e);
        }
    }
    

    @Override
    public void editarParticipanteSql(String cpfCliente, String nome, int idade, double dinheiro, ArrayList<String> telefones) {
        // Atualizar dados do cliente
        String sqlUpdateCliente = "UPDATE cliente SET nome = ?, idade = ?, dinheiro = ? WHERE cpf = ?";
        try (PreparedStatement stmtUpdateCliente = connection.prepareStatement(sqlUpdateCliente)) {
            stmtUpdateCliente.setString(1, nome);
            stmtUpdateCliente.setInt(2, idade);
            stmtUpdateCliente.setDouble(3, dinheiro);
            stmtUpdateCliente.setString(4, cpfCliente);
            stmtUpdateCliente.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        String sqlDeleteTelefones = "DELETE FROM telefone_cliente WHERE cpf_cliente = ?";
        try (PreparedStatement stmtDeleteTelefones = connection.prepareStatement(sqlDeleteTelefones)) {
            stmtDeleteTelefones.setString(1, cpfCliente);
            stmtDeleteTelefones.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        String sqlInsertTelefone = "INSERT INTO telefone_cliente (cpf_cliente, telefone) VALUES (?, ?)";
        for (String telefone : telefones) {
            try (PreparedStatement stmtInsertTelefone = connection.prepareStatement(sqlInsertTelefone)) {
                stmtInsertTelefone.setString(1, cpfCliente);
                stmtInsertTelefone.setString(2, telefone);
                stmtInsertTelefone.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public ArrayList<Cliente> listarParticipantesPorEventoSql(Evento eventoSelecionado) {
        String sqlSelecionarClientes = "SELECT c.cpf, c.nome, c.idade, c.dinheiro, tc.telefone " +
                                       "FROM cliente_evento ce " +
                                       "JOIN cliente c ON ce.codCliente = c.cpf " +
                                       "LEFT JOIN telefone_cliente tc ON c.cpf = tc.cpf_cliente " +
                                       "WHERE ce.codEvento = ?";
        ArrayList<Cliente> clientesEvento = new ArrayList<>();
        try (PreparedStatement stmtSelecionarClientes = connection.prepareStatement(sqlSelecionarClientes)) {
            stmtSelecionarClientes.setInt(1, eventoSelecionado.getId());
            ResultSet rs = stmtSelecionarClientes.executeQuery();
            while (rs.next()) {
                String cpf = rs.getString("cpf");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                double dinheiro = rs.getDouble("dinheiro");
                String telefone = rs.getString("telefone");

                Cliente clienteEvento = encontrarClientePorCpf(clientesEvento, cpf);
                if (clienteEvento == null) {
                    clienteEvento = new Cliente(cpf, nome, idade, dinheiro, eventoSelecionado.getId());
                    clientesEvento.add(clienteEvento);
                }
                if (telefone != null) {
                    clienteEvento.addTelefone(telefone);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return clientesEvento;
    }

    private Cliente encontrarClientePorCpf(ArrayList<Cliente> clientes, String cpf) {
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

}
