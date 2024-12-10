package br.edu.ifsul.samuellpoo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Gerenciamento");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Título do sistema
        JLabel titleLabel = new JLabel("Sistema de Gerenciamento", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Botões de navegação
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnFornecedor = new JButton("Gerenciar Fornecedores");
        JButton btnProduto = new JButton("Gerenciar Produtos");
        JButton btnPedido = new JButton("Gerenciar Pedidos");

        buttonPanel.add(btnFornecedor);
        buttonPanel.add(btnProduto);
        buttonPanel.add(btnPedido);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Adicionando o painel principal ao frame
        add(mainPanel);

        // Ações dos botões
        btnFornecedor.addActionListener((ActionEvent e) -> {
            new FornecedorGUI().setVisible(true);
        });

        btnProduto.addActionListener((ActionEvent e) -> {
            new ProdutoGUI().setVisible(true);
        });

        btnPedido.addActionListener((ActionEvent e) -> {
            new PedidoGUI().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
