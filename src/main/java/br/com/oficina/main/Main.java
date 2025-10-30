package br.com.oficina.main;

import br.com.oficina.dao.ClienteDAO;
import br.com.oficina.dao.VeiculoDAO;
import br.com.oficina.model.Cliente;
import br.com.oficina.model.Veiculo;
import br.com.oficina.util.JPAUtil;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Seja bem-vindo ao OfiSystem ---");
            System.out.println("\n---        Menu Principal       ---");
            System.out.println("\n1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Veículos");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");
            String op = scanner.nextLine();
            switch (op) {
                case "1": menuClientes(); break;
                case "2": menuVeiculos(); break;
                case "3":
                    running = false;
                    break;
                default: System.out.println("Opção inválida.");
            }
        }
        System.out.println("Encerrando aplicação...");
        JPAUtil.closeFactory();
        scanner.close();
    }

    private static void menuClientes() {
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

    private static void inserirCliente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        Cliente c = new Cliente(nome, telefone);
        clienteDAO.salvar(c);
        System.out.println("Cliente salvo: " + c);
    }

    private static void listarClientes() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            clientes.forEach(System.out::println);
        }
    }

    private static void buscarClientePorId() {
        System.out.print("ID do cliente: ");
        Long id = lerLong();
        Cliente c = clienteDAO.buscarPorId(id);
        System.out.println(c != null ? c : "Cliente não encontrado.");
    }

    private static void atualizarCliente() {
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

    private static void excluirCliente() {
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

    private static void menuVeiculos() {
        boolean loop = true;
        while (loop) {
            System.out.println("\n--- Veículos ---");
            System.out.println("1. Inserir Veículo");
            System.out.println("2. Listar Veículos");
            System.out.println("3. Buscar Veículo por ID");
            System.out.println("4. Atualizar Veículo");
            System.out.println("5. Excluir Veículo");
            System.out.println("6. Listar Veículos por Cliente");
            System.out.println("7. Voltar");
            System.out.print("Escolha: ");
            String op = scanner.nextLine();
            switch (op) {
                case "1": inserirVeiculo(); break;
                case "2": listarVeiculos(); break;
                case "3": buscarVeiculoPorId(); break;
                case "4": atualizarVeiculo(); break;
                case "5": excluirVeiculo(); break;
                case "6": listarVeiculosPorCliente(); break;
                case "7": loop = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void inserirVeiculo() {
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("ID do cliente dono: ");
        Long cid = lerLong();
        Cliente c = clienteDAO.buscarPorId(cid);
        if (c == null) {
            System.out.println("Cliente não encontrado. Cadastre o cliente primeiro.");
            return;
        }
        Veiculo v = new Veiculo(placa, modelo, c);
        veiculoDAO.salvar(v);
        System.out.println("Veículo salvo: " + v);
    }

    private static void listarVeiculos() {
        List<Veiculo> vs = veiculoDAO.listarTodos();
        if (vs.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
        } else {
            vs.forEach(System.out::println);
        }
    }

    private static void buscarVeiculoPorId() {
        System.out.print("ID do veículo: ");
        Long id = lerLong();
        Veiculo v = veiculoDAO.buscarPorId(id);
        System.out.println(v != null ? v : "Veículo não encontrado.");
    }

    private static void atualizarVeiculo() {
        System.out.print("ID do veículo a atualizar: ");
        Long id = lerLong();
        Veiculo v = veiculoDAO.buscarPorId(id);
        if (v == null) {
            System.out.println("Veículo não encontrado.");
            return;
        }
        System.out.print("Nova placa (" + v.getPlaca() + "): ");
        String placa = scanner.nextLine();
        if (!placa.trim().isEmpty()) v.setPlaca(placa);
        System.out.print("Novo modelo (" + v.getModelo() + "): ");
        String modelo = scanner.nextLine();
        if (!modelo.trim().isEmpty()) v.setModelo(modelo);

        System.out.print("Novo ID do cliente dono (" + (v.getCliente() != null ? v.getCliente().getId() : "null") + "): ");
        String cidStr = scanner.nextLine();
        if (!cidStr.trim().isEmpty()) {
            try {
                Long cid = Long.parseLong(cidStr);
                Cliente c = clienteDAO.buscarPorId(cid);
                if (c == null) {
                    System.out.println("Cliente não encontrado. Mantendo cliente anterior.");
                } else {
                    v.setCliente(c);
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Mantendo cliente anterior.");
            }
        }
        veiculoDAO.atualizar(v);
        System.out.println("Veículo atualizado: " + v);
    }

    private static void excluirVeiculo() {
        System.out.print("ID do veículo a excluir: ");
        Long id = lerLong();
        veiculoDAO.excluir(id);
        System.out.println("Exclusão (se existia) realizada.");
    }

    private static void listarVeiculosPorCliente() {
        System.out.print("ID do cliente: ");
        Long cid = lerLong();
        List<Veiculo> vs = veiculoDAO.listarPorClienteId(cid);
        if (vs.isEmpty()) System.out.println("Nenhum veículo para esse cliente.");
        else vs.forEach(System.out::println);
    }

    private static Long lerLong() {
        try {
            String s = scanner.nextLine();
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return -1L;
        }
    }
}
