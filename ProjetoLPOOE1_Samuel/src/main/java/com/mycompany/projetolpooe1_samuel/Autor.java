import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "autor")
    private List<Livro> livros;

    // getters e setters
}
