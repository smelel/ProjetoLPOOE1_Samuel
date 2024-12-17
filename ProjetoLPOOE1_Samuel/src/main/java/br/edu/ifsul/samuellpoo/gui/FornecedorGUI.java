package br.edu.ifsul.samuellpoo.gui;

import br.edu.ifsul.samuellpoo.Fornecedor;

import javax.persistence.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FornecedorGUI extends JFrame {
    private EntityManager em;
    private JTable fornecedorTable;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, searchButton;

    public FornecedorGUI() {
        setTitle("Gerenciar Fornecedores");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");
        em = emf.createEntityManager();

        initComponents();
        atualizarLista();
        setVisible(true);
    }

    private void initComponents() {
        fornecedorTable = new JTable();
        searchField = new JTextField(15);
        searchButton = new JButton("Buscar");
        addButton = new JButton("Adicionar");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Excluir");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pesquisar:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

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

        setLayout(new BorderLayout(10, 10));
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(fornecedorTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> filtrarFornecedores());
        addButton.addActionListener(e -> adicionarFornecedor());
        editButton.addActionListener(e -> editarFornecedor());
        deleteButton.addActionListener(e -> excluirFornecedor());
    }

    private void atualizarLista() {
        List<Fornecedor> fornecedores = em.createQuery("FROM Fornecedor", Fornecedor.class).getResultList();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "CNPJ"}, 0);
        for (Fornecedor f : fornecedores) {
            model.addRow(new Object[]{f.getId(), f.getNome(), f.getCnpj()});
        }
        fornecedorTable.setModel(model);
    }

    private void filtrarFornecedores() {
        String filtro = searchField.getText().trim();
        List<Fornecedor> fornecedores;

        if (filtro.isEmpty()) {
            fornecedores = em.createQuery("FROM Fornecedor", Fornecedor.class).getResultList();
        } else {
            fornecedores = em.createQuery("FROM Fornecedor f WHERE f.nome LIKE :nome", Fornecedor.class)
                    .setParameter("nome", "%" + filtro + "%").getResultList();
        }
        atualizarLista(fornecedores);
    }

    private void atualizarLista(List<Fornecedor> fornecedores) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "CNPJ"}, 0);
        for (Fornecedor f : fornecedores) {
            model.addRow(new Object[]{f.getId(), f.getNome(), f.getCnpj()});
        }
        fornecedorTable.setModel(model);
    }

    private void adicionarFornecedor() {
        JTextField nomeField = new JTextField(20);
        JTextField cnpjField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("CNPJ:"));
        panel.add(cnpjField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Adicionar Fornecedor", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String cnpj = cnpjField.getText().trim();

            if (nome.isEmpty() || cnpj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            em.getTransaction().begin();
            Fornecedor f = new Fornecedor();
            f.setNome(nome);
            f.setCnpj(cnpj);
            em.persist(f);
            em.getTransaction().commit();

            atualizarLista();
        }
    }

    private void editarFornecedor() {
        int selectedRow = fornecedorTable.getSelectedRow();
        if (selectedRow != -1) {
            Long fornecedorId = (Long) fornecedorTable.getValueAt(selectedRow, 0);
            Fornecedor fornecedor = em.find(Fornecedor.class, fornecedorId);

            JTextField nomeField = new JTextField(fornecedor.getNome(), 20);
            JTextField cnpjField = new JTextField(fornecedor.getCnpj(), 20);

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("CNPJ:"));
            panel.add(cnpjField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Editar Fornecedor", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                em.getTransaction().begin();
                fornecedor.setNome(nomeField.getText().trim());
                fornecedor.setCnpj(cnpjField.getText().trim());
                em.merge(fornecedor);
                em.getTransaction().commit();
                atualizarLista();
            }
        }
    }

    private void excluirFornecedor() {
        int selectedRow = fornecedorTable.getSelectedRow();
        if (selectedRow != -1) {
            Long fornecedorId = (Long) fornecedorTable.getValueAt(selectedRow, 0);
            Fornecedor fornecedor = em.find(Fornecedor.class, fornecedorId);

            int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este fornecedor?", "Excluir", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                em.getTransaction().begin();
                em.remove(fornecedor);
                em.getTransaction().commit();
                atualizarLista();
            }
        }
    }
}
