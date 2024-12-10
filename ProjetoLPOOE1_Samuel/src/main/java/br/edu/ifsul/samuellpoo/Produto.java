package br.edu.ifsul.samuellpoo;

import javax.persistence.*;

@Entity
@Table(name = "tb_produto")
public class Produto extends ProdutoBase {
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "Produto: " + getNome() + " (Preço: R$ " + String.format("%.2f", getPreco()) + ")";
    }
}
