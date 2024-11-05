package br.edu.ifsul.samuellpoo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    private Produto produto;
    private Fornecedor fornecedor;
    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        // Criação do EntityManagerFactory e EntityManager
        emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");
        em = emf.createEntityManager();
        
        // Criação do fornecedor
        fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12.345.678/0001-99"); 

        // Criação do produto e associação ao fornecedor
        produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(100.0);
        produto.setFornecedor(fornecedor);
    }

    @AfterEach
    public void tearDown() {
        // Fecha o EntityManager após os testes
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        produto = null;
        fornecedor = null;
    }

    @Test
    public void testGetNome() {
        assertEquals("Produto Teste", produto.getNome());
    }

    @Test
    public void testGetPreco() {
        assertEquals(100.0, produto.getPreco());
    }

    @Test
    public void testGetFornecedor() {
        assertEquals(fornecedor, produto.getFornecedor());
    }

    @Test
    public void testPersistProduto() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Criando o fornecedor com o campo CNPJ preenchido
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678000199"); // Definindo o CNPJ

        // Criando o produto e associando o fornecedor
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(99.99);
        produto.setFornecedor(fornecedor);

        // Persistindo o fornecedor antes de persistir o produto
        em.persist(fornecedor); // Persistindo o fornecedor
        em.persist(produto); // Persistindo o produto, que referencia o fornecedor
        em.getTransaction().commit();

        em.close();
    }
}
