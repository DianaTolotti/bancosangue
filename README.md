# bancosangue
# Aplicação Spring Boot bancosangue

Este é um projeto Spring Boot que fornece informações sobre um banco de sangue. A aplicação usa o Gradle para gerenciamento de dependências e pode ser executada com um banco de dados MySQL em um contêiner Docker.

## Pré-requisitos

Certifique-se de que você tenha as seguintes ferramentas e tecnologias instaladas antes de executar a aplicação:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) - Versão 17 (conforme configurado no `build.gradle`). Versão SDK utilizada chama-se: corretto-17
- [Gradle](https://gradle.org/) - Ferramenta de construção e gerenciamento de dependências.
- [Docker](https://www.docker.com/) - Para executar o banco de dados MySQL em um contêiner.

## Configuração

## 1. Clone o repositório

```bash
git clone https://github.com/DianaTolotti/bancosangue.git
```

### 2. Inicializar o Banco de Dados MySQL com Docker
Descompacte em uma pasta de sua preferência o arquivo banco de dados.zip, navegue até localizar o arquivo docker-compose.yml e execute no terminal o comando:
```bash
docker-compose up -d
```
Banco: bancosangue
usuário: bancosangue
Senha: 123admin
Porta: 3300
OBS: o projeto utiliza Liquibase para versionamento do banco de dados e faz a inserção automatica das informações baseado no arquivo json.


## 3. Execução do projeto
Execute a classe BancoSangueApplication


## Contato
Para dúvidas ou sugestões, entre em contato com diana_tolotti@hotmail.com ou (16)982513661
