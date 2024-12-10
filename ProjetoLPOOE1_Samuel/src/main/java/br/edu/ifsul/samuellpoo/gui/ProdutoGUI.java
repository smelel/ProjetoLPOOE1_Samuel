package br.edu.ifsul.samuellpoo.gui;

import br.edu.ifsul.samuellpoo.Fornecedor;
import br.edu.ifsul.samuellpoo.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProdutoGUI extends JFrame {
    private EntityManager em;
    private JList<Produto> listProdutos;
    private DefaultListModel<Produto> listModel;

    public ProdutoGUI() {
        setTitle("Gerenciar Produtos");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");
        em = emf.createEntityManager();

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<>();
        listProdutos = new JList<>(listModel);
        atualizarLista();
        panel.add(new JScrollPane(listProdutos), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
    }

    private void atualizarLista() {
        listModel.clear();
        List<Produto> produtos = em.createQuery("FROM Produto", Produto.class).getResultList();
        produtos.forEach(listModel::addElement);
    }

    private void adicionarProduto() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome:");
            String precoStr = JOptionPane.showInputDialog(this, "Preço:");
            String fornecedorNome = JOptionPane.showInputDialog(this, "Nome do Fornecedor:");

            if (nome != null && precoStr != null && fornecedorNome != null) {
                Double preco = Double.parseDouble(precoStr);

                em.getTransaction().begin();

                Fornecedor fornecedor = em.createQuery(
                        "FROM Fornecedor f WHERE f.nome = :nome", Fornecedor.class)
                        .setParameter("nome", fornecedorNome)
                        .getResultStream().findFirst().orElse(null);

                if (fornecedor == null) {
                    fornecedor = new Fornecedor();
                    fornecedor.setNome(fornecedorNome);
                    fornecedor.setCnpj(JOptionPane.showInputDialog("CNPJ do Fornecedor:"));
                    em.persist(fornecedor);
                }

                Produto produto = new Produto();
                produto.setNome(nome);
                produto.setPreco(preco);
                produto.setFornecedor(fornecedor);

                em.persist(produto);
                em.getTransaction().commit();
                atualizarLista();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }

    private void editarProduto() {
        try {
            Produto produto = listProdutos.getSelectedValue();
            if (produto != null) {
                String nome = JOptionPane.showInputDialog(this, "Nome:", produto.getNome());
                String precoStr = JOptionPane.showInputDialog(this, "Preço:", produto.getPreco().toString());
                String fornecedorNome = JOptionPane.showInputDialog(this, "Fornecedor:", produto.getFornecedor().getNome());

                if (nome != null && precoStr != null && fornecedorNome != null) {
                    Double preco = Double.parseDouble(precoStr);

                    em.getTransaction().begin();

                    Fornecedor fornecedor = em.createQuery(
                            "FROM Fornecedor f WHERE f.nome = :nome", Fornecedor.class)
                            .setParameter("nome", fornecedorNome)
                            .getResultStream().findFirst().orElse(null);

                    if (fornecedor == null) {
                        fornecedor = new Fornecedor();
                        fornecedor.setNome(fornecedorNome);
                        fornecedor.setCnpj(JOptionPane.showInputDialog("CNPJ do Fornecedor:"));
                        em.persist(fornecedor);
                    }

                    produto.setNome(nome);
                    produto.setPreco(preco);
                    produto.setFornecedor(fornecedor);

                    em.merge(produto);
                    em.getTransaction().commit();
                    atualizarLista();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar produto: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }

    private void excluirProduto() {
        try {
            Produto produto = listProdutos.getSelectedValue();
            if (produto != null) {
                em.getTransaction().begin();
                em.remove(em.find(Produto.class, produto.getId()));
                em.getTransaction().commit();
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }
}
