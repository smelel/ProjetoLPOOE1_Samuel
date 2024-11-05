# ProjetoLPOOE1_Samuel

Este projeto foi desenvolvido para a disciplina de Linguagem de Programação Orientada a Objetos e tem como objetivo principal a prática de mapeamento objeto-relacional em Java, utilizando frameworks e tecnologias modernas, como JDBC, JPA e PostgreSQL. O projeto também integra o uso de ferramentas como Maven para gerenciar dependências e GitHub para versionamento.

O sistema implementado consiste em um gerenciamento básico de dados de fornecedores, produtos e pedidos, usando herança para representar hierarquias de classes no modelo de dados. Os dados são armazenados em um banco de dados PostgreSQL e acessados via JDBC e JPA, permitindo operações CRUD nas entidades principais.

## Funcionalidades Principais

- Cadastro de fornecedores, produtos e pedidos.
- Consultas aos dados persistidos no banco de dados.
- Relacionamento entre entidades com mapeamento JPA.
- Uso de herança para estruturar classes base e subclasses.

## Estrutura das Entidades

O projeto é estruturado em torno de quatro entidades principais, incluindo uma hierarquia de classes para produtos:

1. **Fornecedor**: Representa uma empresa ou pessoa que fornece produtos.
2. **ProdutoBase** (classe abstrata): Representa os atributos básicos comuns a todos os produtos.
3. **Produto** (subclasse de ProdutoBase): Extende `ProdutoBase` para incluir o relacionamento com `Fornecedor`.
4. **Pedido**: Representa uma solicitação de produtos, associada a um produto específico.

### Estrutura de Atributos das Entidades

Conforme ilustrado no diagrama abaixo:

**Fornecedor**
- `id`: Long
- `nome`: String  
- `cnpj`: String
- `produtos`: List\<Produto\>

**ProdutoBase** (classe abstrata)
- `id`: Long
- `nome`: String
- `preco`: Double

**Produto** (subclasse de ProdutoBase)
- `fornecedor`: Fornecedor

**Pedido**
- `id`: Long
- `dataPedido`: Date
- `produto`: Produto

### Diagrama de Relacionamento

```plaintext
Fornecedor
    |
    | 1
    |___*
    |
Produto <|-- ProdutoBase (abstract)
    |
    | 1
    |___*
    |
Pedido


*Requisitos*
Java JDK 17 ou superior
Maven
PostgreSQL (com pgAdmin opcional para administração do banco de dados)
Configuração do Banco de Dados
Este projeto se conecta a um banco de dados PostgreSQL. As configurações de conexão estão localizadas no arquivo application.properties no diretório src/main/resources.

*Estrutura de Testes*
Os testes estão localizados no diretório src/test/java e incluem:

Casos para validação dos getters e setters nas entidades.
Testes de persistência, especialmente para a hierarquia de classes usando herança e os relacionamentos entre Produto, Fornecedor e Pedido.
