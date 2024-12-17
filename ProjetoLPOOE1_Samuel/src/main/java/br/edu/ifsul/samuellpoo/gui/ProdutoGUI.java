package br.edu.ifsul.samuellpoo.gui;

import br.edu.ifsul.samuellpoo.Fornecedor;
import br.edu.ifsul.samuellpoo.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoGUI extends JFrame {
    private EntityManager em;
    private JTable produtoTable;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, searchButton;

    public ProdutoGUI() {
        setTitle("Gerenciar Produtos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");
        em = emf.createEntityManager();

        initComponents();
        atualizarLista();
        setVisible(true);
    }

    private void initComponents() {
        produtoTable = new JTable();
        searchField = new JTextField(15);
        searchButton = new JButton("Buscar");
        addButton = new JButton("Adicionar");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Excluir");

        // Painel de pesquisa
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pesquisar:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(editButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(deleteButton, gbc);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(produtoTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        searchButton.addActionListener(e -> filtrarProdutos());
        addButton.addActionListener(e -> adicionarProduto());
        editButton.addActionListener(e -> editarProduto());
        deleteButton.addActionListener(e -> excluirProduto());
    }

    private void atualizarLista() {
        List<Produto> produtos = em.createQuery("FROM Produto", Produto.class).getResultList();
        atualizarTabela(produtos);
    }

    private void atualizarTabela(List<Produto> produtos) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Fornecedor"}, 0);
        for (Produto produto : produtos) {
            model.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getFornecedor().getNome()
            });
        }
        produtoTable.setModel(model);
    }

    private void filtrarProdutos() {
        String filtro = searchField.getText().trim();
        List<Produto> produtos;

        if (filtro.isEmpty()) {
            produtos = em.createQuery("FROM Produto", Produto.class).getResultList();
        } else {
            produtos = em.createQuery("FROM Produto p WHERE p.nome LIKE :nome", Produto.class)
                    .setParameter("nome", "%" + filtro + "%").getResultList();
        }
        atualizarTabela(produtos);
    }

    private void adicionarProduto() {
        JTextField nomeField = new JTextField(20);
        JTextField precoField = new JTextField(20);
        JComboBox<Fornecedor> fornecedorComboBox = new JComboBox<>();
        carregarFornecedores(fornecedorComboBox);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Preço:"));
        panel.add(precoField);
        panel.add(new JLabel("Fornecedor:"));
        panel.add(fornecedorComboBox);

        int option = JOptionPane.showConfirmDialog(this, panel, "Adicionar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String precoStr = precoField.getText().trim();
            Fornecedor fornecedorSelecionado = (Fornecedor) fornecedorComboBox.getSelectedItem();

            if (nome.isEmpty() || precoStr.isEmpty() || fornecedorSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double preco = Double.parseDouble(precoStr);

                em.getTransaction().begin();
                Produto produto = new Produto();
                produto.setNome(nome);
                produto.setPreco(preco);
                produto.setFornecedor(fornecedorSelecionado);
                em.persist(produto);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
                atualizarLista();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido. Insira um valor numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                em.getTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProduto() {
        int selectedRow = produtoTable.getSelectedRow();
        if (selectedRow != -1) {
            Long produtoId = (Long) produtoTable.getValueAt(selectedRow, 0);
            Produto produto = em.find(Produto.class, produtoId);

            JTextField nomeField = new JTextField(produto.getNome(), 20);
            JTextField precoField = new JTextField(produto.getPreco().toString(), 20);
            JComboBox<Fornecedor> fornecedorComboBox = new JComboBox<>();
            carregarFornecedores(fornecedorComboBox);
            fornecedorComboBox.setSelectedItem(produto.getFornecedor());

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("Preço:"));
            panel.add(precoField);
            panel.add(new JLabel("Fornecedor:"));
            panel.add(fornecedorComboBox);

            int option = JOptionPane.showConfirmDialog(this, panel, "Editar Produto", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    double preco = Double.parseDouble(precoField.getText().trim());

                    em.getTransaction().begin();
                    produto.setNome(nomeField.getText().trim());
                    produto.setPreco(preco);
                    produto.setFornecedor((Fornecedor) fornecedorComboBox.getSelectedItem());
                    em.merge(produto);
                    em.getTransaction().commit();

                    JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                    atualizarLista();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Preço inválido. Insira um valor numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void excluirProduto() {
        int selectedRow = produtoTable.getSelectedRow();
        if (selectedRow != -1) {
            Long produtoId = (Long) produtoTable.getValueAt(selectedRow, 0);
            Produto produto = em.find(Produto.class, produtoId);

            int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este produto?", "Excluir", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    em.getTransaction().begin();
                    em.remove(produto);
                    em.getTransaction().commit();

                    JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                    atualizarLista();
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void carregarFornecedores(JComboBox<Fornecedor> comboBox) {
        List<Fornecedor> fornecedores = em.createQuery("FROM Fornecedor", Fornecedor.class).getResultList();
        for (Fornecedor f : fornecedores) {
            comboBox.addItem(f);
        }
    }
}
