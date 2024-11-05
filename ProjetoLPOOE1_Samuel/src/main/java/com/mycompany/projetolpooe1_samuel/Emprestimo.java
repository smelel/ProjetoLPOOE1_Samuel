import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_emprestimo")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    // Getters e setters
}
