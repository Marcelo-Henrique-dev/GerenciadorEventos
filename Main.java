import java.sql.SQLException;
import java.util.Scanner;

import exceptions.InvalidEventoTypeException;
import servicos.ClientesServices;
import servicos.EventosServices;
public class Main{
    
    private static Scanner scanner = new Scanner(System.in);
    private static EventosServices eventosServices = new EventosServices();
    private static ClientesServices clientesServices = new ClientesServices();
    public static void main(String[] args) throws InvalidEventoTypeException, SQLException {
        int opc;
        do {
            System.out.println("=================================");
            System.out.println("| Projeto feito por: |");
            System.out.println("| Marcelo Henrique | Aquiles Arruda | José Renato | Adriano Ramos |");
            System.out.println("=================================");
            System.out.println("|======- Menu -======|");
            System.out.println("| 1 - Cadastra evento | 2 - Visualizar eventos | 3 - Editar Evento | 4 - Cancelar Evento |");
            System.out.println("| 5 - Cadastrar Cliente | 6 - Listar Clientes | 7 - Editar Cliente | 8 - Apagar Cliente |");
            System.out.println("| 0 - Sair |");
            System.out.println("|====================");
            opc = scanner.nextInt();
            scanner.nextLine();
            switch (opc) {
                case 0:
                    System.out.println("Saindo...");
                    break;
                case 1:
                    eventosServices.cadastrarEventoNoRepositorio();
                    break;
                case 2:
                    eventosServices.listarEventos();
                    break;
                case 3:
                    eventosServices.editarEventos();
                    break;
                case 4:
                    eventosServices.apagarEvento();
                    break;
                case 5:
                    clientesServices.cadastrarCliente();
                    break;
                case 6:
                    clientesServices.listarClientesPorEvento();
                    break;
                case 7:
                    clientesServices.editarCliente();
                    break;
                case 8:
                    clientesServices.deletarCliente();
                    break;
                default:
                    System.out.println("Opção Inválida");
                    break;
            }
        } while (opc != 0);
    }

}