package br.com.oficina.view;

import br.com.oficina.dao.ClienteDAO;
import br.com.oficina.dao.VeiculoDAO;
import br.com.oficina.model.Cliente;
import br.com.oficina.model.Veiculo;

import java.util.List;
import java.util.Scanner;

public class VeiculoView {

    private final Scanner scanner;
    private final VeiculoDAO veiculoDAO;
    private final ClienteDAO clienteDAO;

    public VeiculoView(Scanner scanner, VeiculoDAO veiculoDAO, ClienteDAO clienteDAO) {
        this.scanner = scanner;
        this.veiculoDAO = veiculoDAO;
        this.clienteDAO = clienteDAO;
    }

    public void exibirMenu() {
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

    private void inserirVeiculo() {
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

    private void listarVeiculos() {
        List<Veiculo> vs = veiculoDAO.listarTodos();
        if (vs.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
        } else {
            vs.forEach(System.out::println);
        }
    }

    private void buscarVeiculoPorId() {
        System.out.print("ID do veículo: ");
        Long id = lerLong();
        Veiculo v = veiculoDAO.buscarPorId(id);
        System.out.println(v != null ? v : "Veículo não encontrado.");
    }

    private void atualizarVeiculo() {
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

    private void excluirVeiculo() {
        System.out.print("ID do veículo a excluir: ");
        Long id = lerLong();
        veiculoDAO.excluir(id);
        System.out.println("Exclusão (se existia) realizada.");
    }

    private void listarVeiculosPorCliente() {
        System.out.print("ID do cliente: ");
        Long cid = lerLong();
        List<Veiculo> vs = veiculoDAO.listarPorClienteId(cid);
        if (vs.isEmpty()) System.out.println("Nenhum veículo para esse cliente.");
        else vs.forEach(System.out::println);
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