import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BibliotecaTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("ProjetoLPOOE1_SamuelPU"); // nome da unidade de persistência no persistence.xml
        em = emf.createEntityManager();
    }

    @Test
    public void testPersistirLivro() {
        em.getTransaction().begin();
        
        Livro livro = new Livro();
        livro.setTitulo("Java para Iniciantes");
        livro.setIsbn("978-1234567890");
        
        em.persist(livro);
        em.getTransaction().commit();

        Livro livroPersistido = em.find(Livro.class, livro.getId());
        assertNotNull(livroPersistido);
        assertEquals("Java para Iniciantes", livroPersistido.getTitulo());
    }

    @Test
    public void testPersistirAutorComLivro() {
        em.getTransaction().begin();
        
        Autor autor = new Autor();
        autor.setNome("Samuel Nascimento");

        Livro livro = new Livro();
        livro.setTitulo("Aprendendo JPA");
        livro.setIsbn("978-0987654321");
        livro.setAutor(autor);

        em.persist(autor);
        em.persist(livro);
        em.getTransaction().commit();

        Livro livroPersistido = em.find(Livro.class, livro.getId());
        assertNotNull(livroPersistido);
        assertEquals("Aprendendo JPA", livroPersistido.getTitulo());
        assertEquals("Samuel Nascimento", livroPersistido.getAutor().getNome());
    }

    @Test
    public void testPersistirBibliotecaComLivros() {
        em.getTransaction().begin();

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome("Biblioteca Central");

        Livro livro1 = new Livro();
        livro1.setTitulo("JDBC Avançado");
        livro1.setIsbn("978-1122334455");
        
        Livro livro2 = new Livro();
        livro2.setTitulo("Estruturas de Dados");
        livro2.setIsbn("978-9988776655");

        biblioteca.getLivros().add(livro1);
        biblioteca.getLivros().add(livro2);

        em.persist(biblioteca);
        em.persist(livro1);
        em.persist(livro2);
        em.getTransaction().commit();

        Biblioteca bibliotecaPersistida = em.find(Biblioteca.class, biblioteca.getId());
        assertNotNull(bibliotecaPersistida);
        assertEquals(2, bibliotecaPersistida.getLivros().size());
    }

    // Encerrando a conexão ao banco de dados após os testes
    @BeforeEach
    public void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
