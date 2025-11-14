package br.com.oficina.main;

import br.com.oficina.dao.ClienteDAO;
import br.com.oficina.dao.VeiculoDAO;
import br.com.oficina.util.JPAUtil;
import br.com.oficina.view.ClienteView;
import br.com.oficina.view.VeiculoView;

import java.util.Scanner;

public class Main {

    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private static final Scanner scanner = new Scanner(System.in);

    private static final ClienteView clienteView = new ClienteView(scanner, clienteDAO, veiculoDAO);
    private static final VeiculoView veiculoView = new VeiculoView(scanner, veiculoDAO, clienteDAO);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Seja bem-vindo ao OfiSystem ---");
            System.out.println("\n---       Menu Principal       ---");
            System.out.println("\n1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Veículos");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");
            String op = scanner.nextLine();
            switch (op) {

                case "1": clienteView.exibirMenu(); break;
                case "2": veiculoView.exibirMenu(); break;
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
}