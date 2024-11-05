import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Emprestimo> emprestimos;

    // Getters e setters
}
