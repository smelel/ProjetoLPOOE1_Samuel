import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String titulo;

    @OneToMany(mappedBy = "livro")
    private List<Emprestimo> emprestimos;

    // Getters e setters
}
