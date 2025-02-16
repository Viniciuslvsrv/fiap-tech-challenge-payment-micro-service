## Tech Challenge FIAP: fase 4 
**Objetivo:** separar o monolito Lanchonete-app em microsserviços.

**Microsserviços:** Pedido, Pagamento e Produção.

**Linguagem:** Spring Boot , Java 17.

**Banco de dados:** MongoDB

# Microsserviço de Pagamento (Payment)

Este projeto é um microsserviço de pagamento desenvolvido em Java, utilizando Spring Boot. Ele inclui diversas funcionalidades, como integração com MercadoPago, integração com MongoDB, e análise de qualidade com SonarQube.

## Sumário

- [Instalação](#instalação)
- [Configuração](#configuração)
- [Dependências](#dependências)
- [Pipelines](#pipelines)
- [Testes](#testes)
- [Deploy](#deploy)
- [Colaboradores](#colaboradores)

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/Viniciuslvsrv/fiap-tech-challenge-payment-micro-service.git
   cd fiap-tech-challenge-payment-micro-service

## Configuração

O projeto está configurado para utilizar Java 17 e Spring Boot. As configurações principais estão definidas no arquivo build.gradle, incluindo a configuração de plugins como o JaCoCo para relatórios de cobertura de teste e o SonarQube para análise de qualidade.

2. Variáveis de ambiente necessárias:
   ```bash
    export SONAR_TOKEN=your-sonar-token
    export DOCKER_USERNAME=your-docker-username
    export DOCKER_PASSWORD=your-docker-password
    export DISCORD_WEBHOOK_URL=your-discord-webhook-url

## Dependências 

As dependências do projeto estão gerenciadas no build.gradle e incluem bibliotecas essenciais como spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-amqp e springdoc-openapi. Também são incluídas dependências relacionadas ao MongoDB e testes (JUnit, Mockito).

## Pipelines

O projeto possui três pipelines principais configurados no GitHub Actions:

* CI/CD Pipeline: Executa testes e análises de qualidade sempre que há um push ou pull request na branch main.

* Terraform Deploy: Provisiona a infraestrutura utilizando Terraform e é acionado após a conclusão bem-sucedida do pipeline CI/CD.

* SonarQube Analysis: Realiza a análise de qualidade do código utilizando SonarQube e é acionado em push ou pull requests.

## Testes

Os testes são configurados e executados utilizando JUnit e Mockito. O Gradle está configurado para gerar relatórios de cobertura com JaCoCo e esses relatórios são enviados para o SonarQube para análise de qualidade.

## Deploy

O deploy é realizado utilizando Docker, onde a imagem é construída e enviada para o Docker Hub. Além disso, a infraestrutura é provisionada utilizando Terraform, que é validado e aplicado automaticamente.

## Colaboradores
## Desenvolvedores do Grupo 30

- Denis William Mamoni

- Gabriela Marques Fernandes Poncet

- Jessica Prado Costa

- Vinícius Saraiva

- Ludionei Reis

---