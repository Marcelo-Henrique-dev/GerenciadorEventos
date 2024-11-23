package servicos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import entidades.Cliente;
import entidades.Evento;
import interfaces.IClientesServices;
import repositorios.RepositorioDeClientes;
import repositorios.RepositorioDeEventos;

public class ClientesServices implements IClientesServices {

    private Scanner scanner = new Scanner(System.in);

    private RepositorioDeEventos eventos = new RepositorioDeEventos();

    private RepositorioDeClientes clientes = new RepositorioDeClientes();

    @Override
    public void cadastrarCliente() throws SQLException {
        System.out.println("Você deseja cadastrar o cliente em qual evento? (pelo id)");
        int opc = scanner.nextInt();
        scanner.nextLine();
        Evento eventoSelecionado = eventos.listarEventos().get(opc);
        System.out.println("Nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.println("Idade do cliente: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Dinheiro do cliente: ");
        double dinheiro = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Cliente cliente = new Cliente(cpf, nome, idade, dinheiro, eventoSelecionado.getId());
        System.out.println("Quantos telefones ele tem?");
        int qntTelefones = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < qntTelefones; i++) {
            System.out.println("Qual o telefone do Cliente?");
            String telefone = scanner.nextLine();
            cliente.addTelefone(telefone);
        }
        clientes.cadastroParticipanteSql(eventoSelecionado, cliente);
    }

    @Override
    public void deletarCliente() throws SQLException {
        System.out.println("De qual evento você quer retirar um cliente? (pelo id)");
        int opcEvento = scanner.nextInt();
        scanner.nextLine();
        Evento eventoSelecionado = eventos.listarEventos().get(opcEvento);
        System.out.println("Qual cliente você deseja retirar do evento? (pelo cpf)");
        String cpfCliente = scanner.nextLine();
        clientes.deletarClienteSql(eventoSelecionado.getId(), cpfCliente);
    }

    @Override
    public void editarCliente() throws SQLException {
        System.out.println("Em qual evento está o participante? (Por id)");
        int opc = scanner.nextInt();
        scanner.nextLine();
        
        if (opc >= 0 && opc < eventos.listarEventos().size()) {
            Evento eventoSelecionado = eventos.listarEventos().get(opc);
            ArrayList<Cliente> clientesSql = clientes.listarParticipantesPorEventoSql(eventoSelecionado);
    
            System.out.println("Qual participante deseja editar? (pelo id)");
            int opcParticipante = scanner.nextInt();
            scanner.nextLine();
    
            if (opcParticipante >= 0 && opcParticipante < clientesSql.size()) {
                Cliente cliente = clientesSql.get(opcParticipante);
    
                System.out.println("Qual novo nome? ");
                String nome = scanner.nextLine();
                cliente.setNome(nome);
    
                System.out.println("Qual nova idade? ");
                int idade = scanner.nextInt();
                scanner.nextLine();
                cliente.setIdade(idade);
    
                System.out.println("Quanto ele tem agora? ");
                double dinheiro = scanner.nextDouble();
                scanner.nextLine();
                cliente.setDinheiro(dinheiro);
    
                ArrayList<String> novosTelefones = new ArrayList<>();
                for (String telefone : cliente.listarTelefones()) {
                    System.out.println("Qual o novo telefone dele?");
                    telefone = scanner.nextLine();
                    novosTelefones.add(telefone);
                }
                cliente.setTelefones(novosTelefones);
    
                clientes.editarParticipanteSql(cliente.getCpf(), cliente.getNome(), cliente.getIdade(), cliente.getDinheiro(), cliente.listarTelefones());
            } else {
                System.out.println("Participante não encontrado");
            }
        } else {
            System.out.println("Evento não encontrado");
        }
    }
    

    @Override
    public void listarClientesPorEvento() throws SQLException {
        System.out.println("Qual evento você quer ver os clientes? (pelo id)");
        int opcEvento = scanner.nextInt();
        scanner.nextLine();
        if (opcEvento >= 0 && opcEvento < eventos.listarEventos().size()) {
            Evento eventoSelecionado = eventos.listarEventos().get(opcEvento);
            ArrayList<Cliente> clientesSql = clientes.listarParticipantesPorEventoSql(eventoSelecionado);
            if (clientesSql.isEmpty()) {
                System.out.println("Nenhum cliente no evento");
            } else {
                for (int i = 0; i < clientesSql.size(); i++) {
                    Cliente cliente = clientesSql.get(i);
                    System.out.println("==============================");
                    System.out.println("ID: " + i);
                    System.out.println("CPF: " + cliente.getCpf());
                    System.out.println("Nome: " + cliente.getNome());
                    System.out.println("Idade: " + cliente.getIdade());
                    System.out.println("Dinheiro: " + cliente.getDinheiro());
                    System.out.println("Telefones: " + String.join(", ", cliente.listarTelefones()));
                    System.out.println("==============================");
                }
            }
        } else {
            System.out.println("Evento não encontrado");
        }
    }
    
    

}
