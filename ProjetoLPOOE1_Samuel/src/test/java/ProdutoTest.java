import org.junit.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProdutoTest {
    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeClass
    public static void setUpClass() {
        // Cria a fábrica de EntityManager antes de todos os testes
        emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU");
    }

    @Before
    public void setUp() {
        // Cria o EntityManager antes de cada teste
        em = emf.createEntityManager();
    }

    @Test
    public void testPersistirEntidades() {
        em.getTransaction().begin();

        // Criação de um fornecedor
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");

        // Criação de um produto
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(10.0);
        produto.setFornecedor(fornecedor);

        // Adiciona o produto à lista do fornecedor
        if (fornecedor.getProdutos() == null) {
            fornecedor.setProdutos(new ArrayList<>());
        }
        fornecedor.getProdutos().add(produto);

        // Criação de um pedido
        Pedido pedido = new Pedido();
        pedido.setDataPedido(new Date());
        pedido.setProduto(produto);

        // Persistindo as entidades no banco de dados
        em.persist(fornecedor); // O produto será persistido devido à cascata
        em.persist(pedido);

        em.getTransaction().commit();

        // Verificação se as entidades foram persistidas corretamente
        Assert.assertNotNull(fornecedor.getId());
        Assert.assertNotNull(produto.getId());
        Assert.assertNotNull(pedido.getId());
    }

    @Test
    public void testBuscarProduto() {
        em.getTransaction().begin();

        // Busca um produto pelo nome
        TypedQuery<Produto> query = em.createQuery("SELECT p FROM Produto p WHERE p.nome = :nome", Produto.class);
        query.setParameter("nome", "Produto Teste");
        List<Produto> produtos = query.getResultList();

        em.getTransaction().commit();

        // Verificação se o produto foi encontrado
        Assert.assertFalse(produtos.isEmpty());
        Assert.assertEquals("Produto Teste", produtos.get(0).getNome());
    }

    @After
    public void tearDown() {
        // Fecha o EntityManager após cada teste
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        // Fecha a fábrica de EntityManager após todos os testes
        if (emf != null) {
            emf.close();
        }
    }
}

public class ProdutoTest {
    
}
