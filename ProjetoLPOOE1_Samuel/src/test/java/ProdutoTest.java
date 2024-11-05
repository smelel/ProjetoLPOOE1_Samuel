package br.edu.ifsul.samuellpoo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    private Produto produto;
    private Fornecedor fornecedor;

    @BeforeEach
    public void setUp() {
        fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12.345.678/0001-99"); // Agora deve funcionar

        produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(100.0);
        produto.setFornecedor(fornecedor);
    }

    @AfterEach
    public void tearDown() {
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
}
