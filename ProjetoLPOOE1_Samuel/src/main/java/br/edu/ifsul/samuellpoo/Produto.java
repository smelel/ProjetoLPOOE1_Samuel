package br.edu.ifsul.samuellpoo;

import javax.persistence.*;

@Entity
@Table(name = "tb_produto")
public class Produto extends ProdutoBase {
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    // Getters e Setters

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
