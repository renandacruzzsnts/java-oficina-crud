package br.com.oficina.view;

import br.com.oficina.dao.ClienteDAO;
import br.com.oficina.dao.VeiculoDAO;
import br.com.oficina.model.Cliente;
import br.com.oficina.model.Veiculo;

import java.util.List;
import java.util.Scanner;

public class ClienteView {

    private final Scanner scanner;
    private final ClienteDAO clienteDAO;
    private final VeiculoDAO veiculoDAO;

    public ClienteView(Scanner scanner, ClienteDAO clienteDAO, VeiculoDAO veiculoDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
        this.veiculoDAO = veiculoDAO;
    }

    public void exibirMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("\n--- Clientes ---");
            System.out.println("1. Inserir Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente por ID");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Excluir Cliente");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");
            String op = scanner.nextLine();
            switch (op) {
                case "1": inserirCliente(); break;
                case "2": listarClientes(); break;
                case "3": buscarClientePorId(); break;
                case "4": atualizarCliente(); break;
                case "5": excluirCliente(); break;
                case "6": loop = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

  
    private void inserirCliente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        Cliente c = new Cliente(nome, telefone);
        clienteDAO.salvar(c);
        System.out.println("Cliente salvo: " + c);
    }

    private void listarClientes() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            clientes.forEach(System.out::println);
        }
    }

    private void buscarClientePorId() {
        System.out.print("ID do cliente: ");
        Long id = lerLong();
        Cliente c = clienteDAO.buscarPorId(id);
        System.out.println(c != null ? c : "Cliente não encontrado.");
    }

    private void atualizarCliente() {
        System.out.print("ID do cliente a atualizar: ");
        Long id = lerLong();
        Cliente c = clienteDAO.buscarPorId(id);
        if (c == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        System.out.print("Novo nome (" + c.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.trim().isEmpty()) c.setNome(nome);
        System.out.print("Novo telefone (" + c.getTelefone() + "): ");
        String tel = scanner.nextLine();
        if (!tel.trim().isEmpty()) c.setTelefone(tel);
        clienteDAO.atualizar(c);
        System.out.println("Cliente atualizado: " + c);
    }

    private void excluirCliente() {
        System.out.print("ID do cliente a excluir: ");
        Long id = lerLong();
        List<Veiculo> veiculos = veiculoDAO.listarPorClienteId(id);
        if (!veiculos.isEmpty()) {
            System.out.println("Este cliente possui veículos cadastrados. Exclua os veículos primeiro.");
            veiculos.forEach(System.out::println);
            return;
        }
        clienteDAO.excluir(id);
        System.out.println("Exclusão (se existia) realizada.");
    }

    private Long lerLong() {
        try {
            String s = scanner.nextLine();
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return -1L;
        }
    }
}