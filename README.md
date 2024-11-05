# ProjetoLPOOE1_Samuel

Este projeto foi desenvolvido para a disciplina de Linguagem de Programação Orientada a Objetos e tem como objetivo principal a prática de mapeamento objeto-relacional em Java, utilizando frameworks e tecnologias modernas, como JDBC, JPA e PostgreSQL. O projeto também integra o uso de ferramentas como Maven para gerenciar dependências e GitHub para versionamento.

O Projeto consiste em um sistema básico para o gerenciamento de dados de fornecedores, produtos e pedidos. Os dados são armazenados em um banco de dados PostgreSQL e acessados via JDBC e JPA, permitindo operações CRUD para as entidades principais.

O projeto inclui as seguintes funcionalidades:

Cadastro de fornecedores, produtos e pedidos.
Consultas aos dados persistidos no banco de dados.
Relacionamento entre entidades com mapeamento JPA.
Estrutura das Entidades
O projeto é estruturado em torno de três entidades principais:

Fornecedor: Representa uma empresa ou pessoa que fornece produtos.
Produto: Representa os itens que podem ser adquiridos de um fornecedor.
Pedido: Representa uma solicitação de produtos, associada a um fornecedor.
Conforme ilustrado no diagrama abaixo.


Fornecedor
- id: Long
- nome: String  
- endereco: String 
- telefone: String 

1
*

Produto
- id: Long       
- nome: String   
- descricao: String 
- preco: Double  
- fornecedor: Fornecedor 

1
*

Pedido       
- id: Long       
- data: Date    
- produto: Produto 
- quantidade: int


*Requisitos*

Java JDK 17 ou superior
Maven
PostgreSQL (com pgAdmin opcional para administração do banco de dados)
Configuração do Banco de Dados
Este projeto se conecta a um banco de dados PostgreSQL. As configurações de conexão estão localizadas no arquivo application.properties no diretório src/main/resources.

Os testes estão localizados no diretório src/test/java.

