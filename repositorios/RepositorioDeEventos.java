package repositorios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import entidades.Evento;
import entidades.EventoFeriado;
import entidades.EventoFormatura;
import entidades.EventoIgreja;
import entidades.EventoPalestra;
import entidades.EventoReuniao;
import entidades.EventoShow;
import interfaces.IEventoRepository;
import util.ConnectionSingleton;

public class RepositorioDeEventos implements IEventoRepository {
    private Connection connection;

    public RepositorioDeEventos() {
        try {
            this.connection = ConnectionSingleton.getInstance().conexao;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void salvar(Evento evento) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM evento WHERE nome = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, evento.getNome());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Evento já cadastrado");
                return;
            }
        }
        String insertEventoSql = "INSERT INTO evento (nome, dataevento, tipoevento, valoringresso, vagas) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertEventoSql,
                java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, evento.getNome());
            stmt.setString(2, evento.getData());
            stmt.setString(3, evento.getTipoEvento().toString());
            stmt.setDouble(4, evento.getValorIngresso());
            stmt.setInt(5, evento.getQuantidadeVagas());
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idEvento = generatedKeys.getInt(1);
                evento.setId(idEvento);
            }
        } catch (Exception e) {
            e.getStackTrace();
            return;
        }
        String insertEventoTipado = "INSERT INTO " + evento.getTipoEvento().toString().toLowerCase() + " (idevento, "
                + constHeranca(evento.getTipoEvento().toString()) + ") VALUES (?, ?)";
        try (PreparedStatement stmtTipado = connection.prepareStatement(insertEventoTipado)) {
            stmtTipado.setInt(1, evento.getId());
            criarEventoTipado(evento, evento.getTipoEvento().toString(), stmtTipado);
            stmtTipado.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private String constHeranca(String tipoEvento) {
        switch (tipoEvento) {
            case "EVENTOFERIADO":
                return "motivo";
            case "EVENTOFORMATURA":
                return "curso";
            case "EVENTOIGREJA":
                return "padre";
            case "EVENTOPALESTRA":
                return "palestrante";
            case "EVENTOASSUNTO":
                return "assunto";
            case "EVENTOSHOW":
                return "artista";
            default:
                return null;
        }
    }

    private Evento criarEventoTipado(Evento evento, String tipoEvento, PreparedStatement stmt) throws SQLException {
        switch (tipoEvento) {
            case "EVENTOFERIADO":
                EventoFeriado eventoFeriado = (EventoFeriado) evento;
                stmt.setString(2, eventoFeriado.getMotivo());
                break;
            case "EVENTOFORMATURA":
                EventoFormatura eventoFormatura = (EventoFormatura) evento;
                stmt.setString(2, eventoFormatura.getCurso());
                break;
            case "EVENTOIGREJA":
                EventoIgreja eventoIgreja = (EventoIgreja) evento;
                stmt.setString(2, eventoIgreja.getDenominação());
                break;
            case "EVENTOPALESTRA":
                EventoPalestra eventoPalestra = (EventoPalestra) evento;
                stmt.setString(2, eventoPalestra.getPalestrante());
                break;
            case "EVENTOREUNIAO":
                EventoReuniao eventoReuniao = (EventoReuniao) evento;
                stmt.setString(2, eventoReuniao.getAssunto());
                break;
            case "EVENTOSHOW":
                EventoShow eventoShow = (EventoShow) evento;
                stmt.setString(2, eventoShow.getArtista());
                break;
            default:
                System.out.println("Tipo inválido");
                break;
        }
        return null;
    }

    @Override
    public void alterar(Evento evento, String tipoEvento) throws SQLException {
        String updateEventoSql = "UPDATE evento SET nome = ?, dataevento = ?, tipoevento = ?, valoringresso = ?, vagas = ? WHERE idevento = ?";
        try(PreparedStatement stmtUpdateEvento = connection.prepareStatement(updateEventoSql)){
            stmtUpdateEvento.setString(1, evento.getNome());
            stmtUpdateEvento.setString(2, evento.getData());
            stmtUpdateEvento.setString(3, evento.getTipoEvento().toString());
            stmtUpdateEvento.setDouble(4, evento.getValorIngresso());
            stmtUpdateEvento.setInt(5, evento.getQuantidadeVagas());
            stmtUpdateEvento.setInt(6, evento.getId());
            stmtUpdateEvento.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            throw new SQLException("Erro ao atualizar evento"+e);
        }
        String updateEventoTipadoSql = null;
        switch (tipoEvento) {
            case "EVENTOFERIADO": // Evento Feriado
                updateEventoTipadoSql = "UPDATE eventoferiado SET motivo = ? WHERE idEvento = ?";
                try (PreparedStatement stmtTipado = connection.prepareStatement(updateEventoTipadoSql)) {
                    stmtTipado.setString(1, ((EventoFeriado) evento).getMotivo());
                    stmtTipado.setInt(2, evento.getId());
                    stmtTipado.executeUpdate();
                }
                break;
            case "EVENTOFORMATURA": // Evento Formatura
                updateEventoTipadoSql = "UPDATE eventoformatura SET curso = ? WHERE idEvento = ?";
                try (PreparedStatement stmtTipado = connection.prepareStatement(updateEventoTipadoSql)) {
                    stmtTipado.setString(1, ((EventoFormatura) evento).getCurso());
                    stmtTipado.setInt(2, evento.getId());
                    stmtTipado.executeUpdate();
                }
                break;
            case "EVENTOIGREJA": // Evento Igreja
                updateEventoTipadoSql = "UPDATE eventoigreja SET padre = ? WHERE idEvento = ?";
                try (PreparedStatement stmtTipado = connection.prepareStatement(updateEventoTipadoSql)) {
                    stmtTipado.setString(1, ((EventoIgreja) evento).getDenominação());
                    stmtTipado.setInt(2, evento.getId());
                    stmtTipado.executeUpdate();
                }
                break;
            case "EVENTOPALESTRA": // Evento Palestra
                updateEventoTipadoSql = "UPDATE eventopalestra SET palestrante = ? WHERE idEvento = ?";
                try (PreparedStatement stmtTipado = connection.prepareStatement(updateEventoTipadoSql)) {
                    stmtTipado.setString(1, ((EventoPalestra) evento).getPalestrante());
                    stmtTipado.setInt(2, evento.getId());
                    stmtTipado.executeUpdate();
                }
                break;
            case "EVENTOREUNIAO": // Evento Reunião
                updateEventoTipadoSql = "UPDATE eventoreuniao SET assunto = ? WHERE idEvento = ?";
                try (PreparedStatement stmtTipado = connection.prepareStatement(updateEventoTipadoSql)) {
                    stmtTipado.setString(1, ((EventoReuniao) evento).getAssunto());
                    stmtTipado.setInt(2, evento.getId());
                    stmtTipado.executeUpdate();
                }
                break;
            case "EVENTOSHOW": // Evento Show
                updateEventoTipadoSql = "UPDATE eventoshow SET artista = ? WHERE idEvento = ?";
                try (PreparedStatement stmtTipado = connection.prepareStatement(updateEventoTipadoSql)) {
                    stmtTipado.setString(1, ((EventoShow) evento).getArtista());
                    stmtTipado.setInt(2, evento.getId());
                    stmtTipado.executeUpdate();
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de evento desconhecido: " + tipoEvento.toString());
        }
        System.out.println("Evento atualizado com sucesso:"+ evento.getNome());
    }

    @Override
    public ArrayList<Evento> listarEventos() throws SQLException {
        ArrayList<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idEvento = rs.getInt("idEvento");
                String nome = rs.getString("nome");
                String dataEvento = rs.getString("dataEvento");
                String tipoEvento = rs.getString("tipoEvento");
                double valorIngresso = rs.getDouble("valorIngresso");
                int vagas = rs.getInt("vagas");

                Evento evento = criarEvento(rs, idEvento, nome, dataEvento, tipoEvento, valorIngresso, vagas);
                if (evento != null) {
                    eventos.add(evento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }

    private Evento criarEvento(ResultSet rs, int idEvento, String nome, String dataEvento, String tipoEvento, double valorIngresso, int vagas) throws SQLException {
        Evento evento;
        String sqlEspecifico = "SELECT * FROM " + tipoEvento.toLowerCase() + " WHERE idEvento = ?";
        try (PreparedStatement stmtEspecifico = connection.prepareStatement(sqlEspecifico)) {
            stmtEspecifico.setInt(1, idEvento);
            ResultSet rsEspecifico = stmtEspecifico.executeQuery();
            if (rsEspecifico.next()) {
                switch (tipoEvento) {
                    case "EVENTOFERIADO":
                        String motivo = rsEspecifico.getString("motivo");
                        evento = new EventoFeriado(idEvento, nome, dataEvento, valorIngresso, vagas, motivo);
                        break;
                    case "EVENTOFORMATURA":
                        String curso = rsEspecifico.getString("curso");
                        evento = new EventoFormatura(idEvento, nome, dataEvento, valorIngresso, vagas, curso);
                        break;
                    case "EVENTOIGREJA":
                        String padre = rsEspecifico.getString("padre");
                        evento = new EventoIgreja(idEvento, nome, dataEvento, valorIngresso, vagas, padre);
                        break;
                    case "EVENTOPALESTRA":
                        String palestrante = rsEspecifico.getString("palestrante");
                        evento = new EventoPalestra(idEvento, nome, dataEvento, valorIngresso, vagas, palestrante);
                        break;
                    case "EVENTOREUNIAO":
                        String assunto = rsEspecifico.getString("assunto");
                        evento = new EventoReuniao(idEvento, nome, dataEvento, valorIngresso, vagas, assunto);
                        break;
                    case "EVENTOSHOW":
                        String artista = rsEspecifico.getString("artista");
                        evento = new EventoShow(idEvento, nome, dataEvento, valorIngresso, vagas, artista);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de evento desconhecido: " + tipoEvento);
                }
            } else {
                evento = null;
            }
        }
        return evento;
    }

    @Override
    public void excluir(Evento eventoSelecionado) throws SQLException {
        // Deletar dados específicos do evento
        String deleteSpecificSql = "DELETE FROM " + eventoSelecionado.getTipoEvento().toString().toLowerCase() + " WHERE idevento = ?";
        try (PreparedStatement stmtSpecific = connection.prepareStatement(deleteSpecificSql)) {
            stmtSpecific.setInt(1, eventoSelecionado.getId());
            stmtSpecific.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erro ao apagar dados específicos: " + e);
        }
    
        // Deletar associações na tabela cliente_evento
        String selectClientesSql = "SELECT codCliente FROM cliente_evento WHERE codEvento = ?";
        ArrayList<String> clientesParaDeletar = new ArrayList<>();
        try (PreparedStatement stmtSelectClientes = connection.prepareStatement(selectClientesSql)) {
            stmtSelectClientes.setInt(1, eventoSelecionado.getId());
            ResultSet rs = stmtSelectClientes.executeQuery();
            while (rs.next()) {
                clientesParaDeletar.add(rs.getString("codCliente"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erro ao selecionar clientes do evento: " + e);
        }
    
        // Deletar as associações de cliente_evento antes de deletar os clientes
        String deleteClientesEventoSql = "DELETE FROM cliente_evento WHERE codEvento = ?";
        try (PreparedStatement stmtClientesEvento = connection.prepareStatement(deleteClientesEventoSql)) {
            stmtClientesEvento.setInt(1, eventoSelecionado.getId());
            stmtClientesEvento.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erro ao deletar clientes do evento: " + e);
        }
    
        // Deletar telefones dos clientes
        String deleteTelefonesSql = "DELETE FROM telefone_cliente WHERE cpf_cliente = ?";
        try (PreparedStatement stmtDeleteTelefones = connection.prepareStatement(deleteTelefonesSql)) {
            for (String cpfCliente : clientesParaDeletar) {
                stmtDeleteTelefones.setString(1, cpfCliente);
                stmtDeleteTelefones.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erro ao deletar telefones dos clientes: " + e);
        }
    
        // Deletar clientes da tabela cliente
        String deleteClientesSql = "DELETE FROM cliente WHERE cpf = ?";
        try (PreparedStatement stmtDeleteClientes = connection.prepareStatement(deleteClientesSql)) {
            for (String cpfCliente : clientesParaDeletar) {
                stmtDeleteClientes.setString(1, cpfCliente);
                stmtDeleteClientes.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erro ao deletar clientes: " + e);
        }
    
        // Deletar o evento da tabela evento
        String deleteEventoSql = "DELETE FROM evento WHERE idevento = ?";
        try (PreparedStatement stmtEvento = connection.prepareStatement(deleteEventoSql)) {
            stmtEvento.setInt(1, eventoSelecionado.getId());
            stmtEvento.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erro ao deletar evento: " + e);
        }
    
        System.out.println("Evento deletado com sucesso!");
    }
    
    
}
