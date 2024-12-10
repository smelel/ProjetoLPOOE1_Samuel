package br.edu.ifsul.samuellpoo.gui;

import br.edu.ifsul.samuellpoo.Pedido;
import br.edu.ifsul.samuellpoo.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PedidoGUI extends JFrame {
    private EntityManager em;
    private JList<Pedido> listPedidos;
    private DefaultListModel<Pedido> listModel;
    private JComboBox<Produto> comboBoxProdutos;

    public PedidoGUI() {
        setTitle("Gerenciar Pedidos");
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
        listPedidos = new JList<>(listModel);
        atualizarLista();
        panel.add(new JScrollPane(listPedidos), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarPedido());
        btnEditar.addActionListener(e -> editarPedido());
        btnExcluir.addActionListener(e -> excluirPedido());
    }

    private void atualizarLista() {
        listModel.clear();
        List<Pedido> pedidos = em.createQuery("FROM Pedido", Pedido.class).getResultList();
        pedidos.forEach(listModel::addElement);
    }

    private void carregarProdutosNoComboBox() {
        comboBoxProdutos = new JComboBox<>();
        List<Produto> produtos = em.createQuery("FROM Produto", Produto.class).getResultList();
        produtos.forEach(comboBoxProdutos::addItem);
    }

    private void adicionarPedido() {
        try {
            carregarProdutosNoComboBox();

            JTextField campoData = new JTextField();
            comboBoxProdutos.setSelectedIndex(0);

            Object[] inputs = {
                "Data do Pedido (dd/MM/yyyy):", campoData,
                "Produto:", comboBoxProdutos
            };

            int result = JOptionPane.showConfirmDialog(this, inputs, "Adicionar Pedido", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String dataStr = campoData.getText();
                Produto produtoSelecionado = (Produto) comboBoxProdutos.getSelectedItem();

                if (dataStr != null && produtoSelecionado != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataPedido = sdf.parse(dataStr);

                    em.getTransaction().begin();

                    Pedido pedido = new Pedido();
                    pedido.setDataPedido(dataPedido);
                    pedido.setProduto(produtoSelecionado);

                    em.persist(pedido);
                    em.getTransaction().commit();
                    atualizarLista();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar pedido: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }

    private void editarPedido() {
        try {
            Pedido pedido = listPedidos.getSelectedValue();
            if (pedido != null) {
                carregarProdutosNoComboBox();

                JTextField campoData = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataPedido()));
                comboBoxProdutos.setSelectedItem(pedido.getProduto());

                Object[] inputs = {
                    "Data do Pedido (dd/MM/yyyy):", campoData,
                    "Produto:", comboBoxProdutos
                };

                int result = JOptionPane.showConfirmDialog(this, inputs, "Editar Pedido", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String dataStr = campoData.getText();
                    Produto produtoSelecionado = (Produto) comboBoxProdutos.getSelectedItem();

                    if (dataStr != null && produtoSelecionado != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataPedido = sdf.parse(dataStr);

                        em.getTransaction().begin();

                        pedido.setDataPedido(dataPedido);
                        pedido.setProduto(produtoSelecionado);

                        em.merge(pedido);
                        em.getTransaction().commit();
                        atualizarLista();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar pedido: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }

    private void excluirPedido() {
        try {
            Pedido pedido = listPedidos.getSelectedValue();
            if (pedido != null) {
                em.getTransaction().begin();
                em.remove(em.find(Pedido.class, pedido.getId()));
                em.getTransaction().commit();
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir pedido: " + ex.getMessage());
            em.getTransaction().rollback();
        }
    }
}
