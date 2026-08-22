# Draw Magic — Cloud-Native CAD Platform

[![Pipeline CI/CD](https://github.com/luiza-esteves/polydraw/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/luiza-esteves/polydraw/actions)
![Java 21](https://img.shields.io/badge/Java-21-orange?style=flat&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Containers-blue?style=flat&logo=docker)
![AWS](https://img.shields.io/badge/AWS-EC2_%7C_S3-FF9900?style=flat&logo=amazon-aws)
![License](https://img.shields.io/badge/License-MIT-green.svg)

Uma plataforma CAD 2D/3D distribuída construída com o objetivo de realizar a aplicação prática de **Computação Gráfica (vetores e transformações matriciais)**, **Arquitetura de Microsserviços com Java & Spring Boot**, **Processamento Assíncrono Orientado a Eventos** e **Práticas Avançadas de DevOps (CI/CD, GitHub Actions, Dockerização e Deploy Automatizado na AWS)**.

---

## 🎯 Objetivo do Projeto

O **Draw Magic** foi projetado para simular o ciclo completo de engenharia de software de uma plataforma de design/engenharia escalável na nuvem (no estilo Figma/AutoCAD Web). 

O projeto aplica o princípio da **separação de responsabilidades**: a baixa latência e os cálculos geométricos necessários para a interatividade da interface ficam no cliente gráfico, enquanto a persistência de dados, autenticação e o processamento pesado de renderização em alta definição são delegados a uma malha de microsserviços desacoplados. Meu objetivo é reforçar meus conhecimentos em processo de desenvolvimento e aplicar os aprendizados adquiros na pós graduação em DevOps.

---

## 🏗️ Arquitetura do Sistema

```text
                           ┌────────────────────────┐
                           │   GitHub Repository    │
                           │      (Monorepo)        │
                           └───────────┬────────────┘
                                       │ Push Git Tag (v1.0.0)
                                       ▼
                           ┌────────────────────────┐
                           │  GitHub Actions CI/CD  │
                           └───────────┬────────────┘
                                       │ Build & Push Containers
                                       ▼
                           ┌────────────────────────┐
                           │  GitHub Registry GHCR  │
                           └───────────┬────────────┘
                                       │ SSH / Pull Images
                                       ▼
┌───────────────────────────────────────────────────────────────────────────┐
│ AWS EC2 Instance (t2.micro)                                               │
│                                                                           │
│  ┌────────────────────────┐       ┌──────────────────────────┐            │
│  │ Client (JavaFX CAD App)│──────>│   Spring Cloud Gateway   │            │
│  └────────────────────────┘       │       (Porta 8080)       │            │
│                                   └────────────┬─────────────┘            │
│                                                │                          │
│          ┌─────────────────────────────────────┼──────────────────────┐   │
│          │                                     │                      │   │
│ ┌────────▼───────────┐            ┌────────────▼─────────┐   ┌────────▼─┐ │
│ │ Auth Service       │            │ Project Service      │   │PostgreSQL│ │
│ └────────┬───────────┘            └────────────┬─────────┘   └──────────┘ │
│          │                                     │                          │
│          │                            (Event)  │                          │
│          │                                     ▼                          │
│ ┌────────▼───────────┐            ┌──────────────────────┐                │
│ │ PostgreSQL (Auth DB)│            │  RabbitMQ Broker     │                │
│ └────────────────────┘            └────────────┬─────────┘                │
│                                                │                          │
│                                                ▼                          │
│                                   ┌──────────────────────┐                │
│                                   │ Render Worker Service│                │
│                                   └────────────┬─────────┘                │
└────────────────────────────────────────────────┼──────────────────────────┘
                                                 │ AWS SDK Upload
                                                 ▼
                                    ┌──────────────────────┐
                                    │    AWS S3 Bucket     │
                                    └──────────────────────┘

```

---

## 🛠️ Tecnologias

### **Computação Gráfica e Cliente**

* **Java 21 / JavaFX (Canvas):** Manipulação de formas geométricas e renderização em malha interativa.
* **Álgebra Linear:** Vetores (`Vector2D`), Matrizes de Transformação Afim 3x3 (`Matrix3x3` para translação, rotação e escala) e algoritmos de detecção de seleção (*picking*).
* **Padrão MVC:** Desacoplamento entre o grafo de cena (*Scene Graph* Model), a camada de renderização no Canvas (View) e manipuladores de eventos (Controller).

### **Backend & Microsserviços**

* **Spring Boot 3.x & Spring Cloud Gateway:** Ponto único de entrada com roteamento de APIs e validação centralizada de tokens JWT.
* **Spring Security & OAuth2/JWT:** Autenticação *stateless* e controle de autorização.
* **Spring Data JPA & PostgreSQL:** Persistência relacional para metadados de projetos e suporte a campos `JSONB` para armazenamento da árvore de objetos vetoriais.
* **RabbitMQ (AMQP):** Comunicação assíncrona orientada a eventos para desacoplar solicitações de renderização pesada.

### **DevOps, Nuvem & Qualidade**

* **GitHub Actions:** Esteira de CI/CD automatizada para execução de testes unitários, compilação paralela e verificação *fail-fast*.
* **Docker & Docker Compose:** Contenerização multi-stage e orquestração do ambiente de desenvolvimento local e produção.
* **AWS (EC2 & S3):** Hospedagem em nuvem da infraestrutura de containers e armazenamento de objetos para entrega das imagens renderizadas.
* **Semantic Versioning (SemVer) & Git Tags:** Controle de releases automatizado (`v0.1.0-alpha`, `v1.0.0`) com geração automática de *Changelogs* no GitHub Releases.

---

## 🚀 Roadmap de Evolução & Releases

O projeto está sendo desenvolvido em **5 fases graduais**, utilizando o modelo de branches *Feature Branch Workflow*:

* [x] **v0.1.0-alpha — Setup de Arquitetura & Monorepo:** Estruturação da hierarquia do repositório, Docker Compose local e pipeline inicial de CI/CD.
* [ ] **v0.2.0-beta — Motor Gráfico 2D & Álgebra Linear:** Implementação do motor gráfico em Java (vetores, matrizes, transformações afins e exportação JSON do Scene Graph).
* [ ] **v0.3.0-rc — Monólito Spring Boot & Persistência SQL:** Criação da API REST para salvamento e leitura de projetos com Spring Security (JWT) e PostgreSQL (`JSONB`).
* [ ] **v1.0.0 — Migração para Microsserviços, CI/CD & Deploy na AWS:** Separação dos serviços via Spring Cloud Gateway, publicação de containers imutáveis no GHCR e deploy automatizado via SSH na instância AWS EC2.
* [ ] **v1.1.0 — Worker Assíncrono de Renderização & AWS S3:** Fila de tarefas com RabbitMQ para renderização em background e upload automático da mídia gerada para o AWS S3.

---

## 📂 Estrutura do Repositório (Monorepo)

```text
polydraw/
├── .github/
│   └── workflows/
│       └── ci-cd.yml             # Pipeline de CI/CD no GitHub Actions
├── client/                        # Motor gráfico e interface CAD em Java (Maven)
├── backend/
│   ├── gateway/                   # Spring Cloud Gateway
│   ├── auth-service/              # Autenticação e Gestão de Usuários (Spring Boot)
│   ├── project-service/           # CRUD de projetos e emissão de eventos (Spring Boot)
│   └── render-worker/             # Consumidor RabbitMQ e renderizador (Spring Boot)
├── infra/
│   └── docker/
│       ├── docker-compose.yml     # Ambiente de desenvolvimento local
│       └── docker-compose.prod.yml# Infraestrutura de deploy na AWS
├── LICENSE
└── README.md

```
---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos

* **JDK 21** ou superior instalado.
* **Docker** e **Docker Compose** ativos.
* **Git**.

### 1. Clonar o Repositório

```bash
git clone [https://github.com/luiza-esteves/polydraw.git](https://github.com/luiza-esteves/polydraw.git)
cd polydraw

```

### 2. Subir a Infraestrutura Local (PostgreSQL & RabbitMQ)

```bash
docker compose -f infra/docker/docker-compose.yml up -d

```

### 3. Compilar e Testar o Módulo Client

```bash
cd client
./mvnw clean test

```

---

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](https://www.google.com/search?q=LICENSE) para mais detalhes.

---

*Desenvolvido por **Luíza Esteves** como projeto prático de aprofundamento em Computação Gráfica, Engenharia de Software e DevOps.*

```
