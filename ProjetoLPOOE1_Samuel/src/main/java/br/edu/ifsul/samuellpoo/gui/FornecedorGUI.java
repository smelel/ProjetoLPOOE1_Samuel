package br.edu.ifsul.samuellpoo.gui;

import br.edu.ifsul.samuellpoo.Fornecedor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FornecedorGUI extends JFrame {
    private EntityManager em;
    private JList<Fornecedor> listFornecedores;
    private DefaultListModel<Fornecedor> listModel;

    public FornecedorGUI() {
        setTitle("Gerenciar Fornecedores");
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
        listFornecedores = new JList<>(listModel);
        atualizarLista();
        panel.add(new JScrollPane(listFornecedores), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarFornecedor());
        btnEditar.addActionListener(e -> editarFornecedor());
        btnExcluir.addActionListener(e -> excluirFornecedor());
    }

    private void atualizarLista() {
        listModel.clear();
        List<Fornecedor> fornecedores = em.createQuery("FROM Fornecedor", Fornecedor.class).getResultList();
        fornecedores.forEach(listModel::addElement);
    }

    private void adicionarFornecedor() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome:");
            String cnpj = JOptionPane.showInputDialog(this, "CNPJ:");

            if (nome != null && cnpj != null) {
                em.getTransaction().begin();
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setNome(nome);
                fornecedor.setCnpj(cnpj);
                em.persist(fornecedor);
                em.getTransaction().commit();
                atualizarLista();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar fornecedor: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }

    private void editarFornecedor() {
        try {
            Fornecedor fornecedor = listFornecedores.getSelectedValue();
            if (fornecedor != null) {
                String nome = JOptionPane.showInputDialog(this, "Nome:", fornecedor.getNome());
                String cnpj = JOptionPane.showInputDialog(this, "CNPJ:", fornecedor.getCnpj());

                if (nome != null && cnpj != null) {
                    em.getTransaction().begin();
                    fornecedor.setNome(nome);
                    fornecedor.setCnpj(cnpj);
                    em.merge(fornecedor);
                    em.getTransaction().commit();
                    atualizarLista();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar fornecedor: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }

    private void excluirFornecedor() {
        try {
            Fornecedor fornecedor = listFornecedores.getSelectedValue();
            if (fornecedor != null) {
                em.getTransaction().begin();
                em.remove(em.find(Fornecedor.class, fornecedor.getId()));
                em.getTransaction().commit();
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir fornecedor: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }
}
