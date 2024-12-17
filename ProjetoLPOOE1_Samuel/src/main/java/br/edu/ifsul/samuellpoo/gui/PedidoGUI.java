package br.edu.ifsul.samuellpoo.gui;

import br.edu.ifsul.samuellpoo.Pedido;
import br.edu.ifsul.samuellpoo.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PedidoGUI extends JFrame {
    private EntityManager em;
    private JTable pedidoTable;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, searchButton;

    public PedidoGUI() {
        setTitle("Gerenciar Pedidos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");
        em = emf.createEntityManager();

        initComponents();
        atualizarLista();
        setVisible(true);
    }

    private void initComponents() {
        pedidoTable = new JTable();
        searchField = new JTextField(15);
        searchButton = new JButton("Buscar");
        addButton = new JButton("Adicionar");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Excluir");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pesquisar (Produto):"));
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
        add(new JScrollPane(pedidoTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> filtrarPedidos());
        addButton.addActionListener(e -> adicionarPedido());
        editButton.addActionListener(e -> editarPedido());
        deleteButton.addActionListener(e -> excluirPedido());
    }

    private void atualizarLista() {
        List<Pedido> pedidos = em.createQuery("FROM Pedido", Pedido.class).getResultList();
        atualizarTabela(pedidos);
    }

    private void atualizarTabela(List<Pedido> pedidos) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Data do Pedido", "Produto"}, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Pedido pedido : pedidos) {
            model.addRow(new Object[]{
                    pedido.getId(),
                    sdf.format(pedido.getDataPedido()),
                    pedido.getProduto().getNome()
            });
        }
        pedidoTable.setModel(model);
    }

    private void filtrarPedidos() {
        String filtro = searchField.getText().trim();
        List<Pedido> pedidos;

        if (filtro.isEmpty()) {
            pedidos = em.createQuery("FROM Pedido", Pedido.class).getResultList();
        } else {
            pedidos = em.createQuery(
                            "FROM Pedido p WHERE p.produto.nome LIKE :nome", Pedido.class)
                    .setParameter("nome", "%" + filtro + "%")
                    .getResultList();
        }
        atualizarTabela(pedidos);
    }

    private void adicionarPedido() {
        JComboBox<Produto> produtoComboBox = new JComboBox<>();
        carregarProdutos(produtoComboBox);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Data do Pedido (dd/MM/yyyy):"));
        JFormattedTextField dataField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        dataField.setColumns(10);
        panel.add(dataField);
        panel.add(new JLabel("Produto:"));
        panel.add(produtoComboBox);

        int option = JOptionPane.showConfirmDialog(this, panel, "Adicionar Pedido", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String dataStr = dataField.getText().trim();
            Produto produtoSelecionado = (Produto) produtoComboBox.getSelectedItem();

            if (dataStr.isEmpty() || produtoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Date dataPedido = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);

                em.getTransaction().begin();
                Pedido pedido = new Pedido();
                pedido.setDataPedido(dataPedido);
                pedido.setProduto(produtoSelecionado);
                em.persist(pedido);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "Pedido adicionado com sucesso!");
                atualizarLista();
            } catch (Exception ex) {
                em.getTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarProdutos(JComboBox<Produto> comboBox) {
        List<Produto> produtos = em.createQuery("FROM Produto", Produto.class).getResultList();
        for (Produto p : produtos) {
            comboBox.addItem(p);
        }
    }

    private void editarPedido() {
        int selectedRow = pedidoTable.getSelectedRow();
        if (selectedRow != -1) {
            Long pedidoId = (Long) pedidoTable.getValueAt(selectedRow, 0);
            Pedido pedido = em.find(Pedido.class, pedidoId);

            JComboBox<Produto> produtoComboBox = new JComboBox<>();
            carregarProdutos(produtoComboBox);
            produtoComboBox.setSelectedItem(pedido.getProduto());

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Data do Pedido (dd/MM/yyyy):"));
            JFormattedTextField dataField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            dataField.setText(new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataPedido()));
            dataField.setColumns(10);
            panel.add(dataField);
            panel.add(new JLabel("Produto:"));
            panel.add(produtoComboBox);

            int option = JOptionPane.showConfirmDialog(this, panel, "Editar Pedido", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Date dataPedido = new SimpleDateFormat("dd/MM/yyyy").parse(dataField.getText().trim());

                    em.getTransaction().begin();
                    pedido.setDataPedido(dataPedido);
                    pedido.setProduto((Produto) produtoComboBox.getSelectedItem());
                    em.merge(pedido);
                    em.getTransaction().commit();

                    JOptionPane.showMessageDialog(this, "Pedido atualizado com sucesso!");
                    atualizarLista();
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void excluirPedido() {
        int selectedRow = pedidoTable.getSelectedRow();
        if (selectedRow != -1) {
            Long pedidoId = (Long) pedidoTable.getValueAt(selectedRow, 0);
            Pedido pedido = em.find(Pedido.class, pedidoId);

            int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este pedido?", "Excluir", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    em.getTransaction().begin();
                    em.remove(pedido);
                    em.getTransaction().commit();
                    JOptionPane.showMessageDialog(this, "Pedido excluído com sucesso!");
                    atualizarLista();
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
