# Sistema de Gerenciamento de Usuários

Este é um projeto de API RESTful para um sistema de gerenciamento de usuários que oferece funcionalidades como cadastro, autenticação, atualização e remoção de usuários, além de envio de mensagem para um tópico no Apache Kafka sempre que um novo usuário for registrado com sucesso.

## Funcionalidades

- Cadastro de Usuário: Permite que os usuários se cadastrem fornecendo um nome de usuário, endereço de e-mail e senha. Valida os campos de entrada para garantir que o endereço de e-mail seja válido e que a senha tenha pelo menos 8 caracteres.
  
- Autenticação de Usuário: Permite que os usuários façam login fornecendo seu endereço de e-mail e senha. Implementa autenticação básica usando Spring Security.
  
- CRUD: Implementação do CREATE,READ,UPDATE E DELETE
  
- Envio de Mensagem: Implementa um endpoint para enviar uma mensagem para um tópico no Apache Kafka sempre que um novo usuário for registrado com sucesso.

## Requisitos Técnicos

- **Spring Boot**: Utilizado para criar a aplicação.
  
- **Spring Security**: Utilizado para autenticação.
  
- **JPA com Hibernate**: Utilizado para persistência dos dados dos usuários.
  
- **Hibernate Validator**: Utilizado para validar as entradas da API.
  
- **JUnit e Mockito**: Implementação de testes unitários para os serviços da aplicação.
  
- **Apache Kafka**: Integrado para enviar mensagens sempre que um novo usuário for registrado.

## Pontos Extras (opcionais)
  
- **Autorização com Base em Funções**: Implementação opcional de autorização com base em funções (por exemplo, usuário regular vs. administrador) usando Spring Security.

- **Consumo de Mensagens Kafka**: Implementação opcional do consumo das mensagens Kafka em uma funcionalidade adicional da aplicação.

## Como Usar

1. Clone este repositório.
2. Certifique-se de ter o JDK, Maven e Docker instalados em sua máquina.
5. A aplicação estará disponível em `http://localhost:8080`.
6. Para utilizar a funcionalidade de envio de mensagem com Kafka, certifique-se de ter o Kafka configurado e em execução.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir um issue ou enviar um pull request.

![Visitors](https://api.visitorbadge.io/api/visitors?path=https%3A%2F%2Fgithub.com%2Fpblda13%2FUser_Management_System&label=Visitors&countColor=%23263759)

