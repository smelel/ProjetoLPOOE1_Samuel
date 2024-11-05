package br.edu.ifsul.samuellpoo;

import javax.persistence.*;

@MappedSuperclass
public abstract class ProdutoBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {  // Verifique se este método está presente
        return preco;
    }

    public void setPreco(Double preco) {  // Verifique se este método está presente
        this.preco = preco;
    }
}
