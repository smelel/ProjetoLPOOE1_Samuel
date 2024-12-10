package br.edu.ifsul.samuellpoo;

import org.junit.jupiter.api.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");

    @Test
    public void testPersistProduto() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Criando um fornecedor
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678000199");

        // Criando um produto associado ao fornecedor
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(150.0);
        produto.setFornecedor(fornecedor);

        // Persistindo fornecedor e produto
        em.persist(fornecedor);
        em.persist(produto);
        em.getTransaction().commit();

        // Validando a persistência
        Produto p = em.find(Produto.class, produto.getId());
        assertNotNull(p);
        assertEquals("Produto Teste", p.getNome());
        assertEquals(150.0, p.getPreco());
        assertEquals("Fornecedor Teste", p.getFornecedor().getNome());

        em.close();
    }
}
