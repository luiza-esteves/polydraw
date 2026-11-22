# Draw Magic — Cloud-Native CAD Platform

[![Pipeline CI/CD](https://github.com/luiza-esteves/polydraw/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/luiza-esteves/polydraw/actions)
![Java 21](https://img.shields.io/badge/Java-21-orange?style=flat&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Containers-blue?style=flat&logo=docker)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Helm-326CE5?style=flat&logo=kubernetes)
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
 ┌──────────────────────┐        ┌────────────────────────┐
 │  GitHub (Monorepo)   │──push─>│  GitHub Actions CI/CD  │  build · testes · scan (Trivy)
 └──────────────────────┘        └───────────┬────────────┘
                                             │ push de imagens (tag do commit + SemVer)
                                             ▼
                                 ┌────────────────────────┐
                                 │  GitHub Registry GHCR  │
                                 └───────────┬────────────┘
                                             │ pull de imagens
 Máquina do usuário                          ▼
┌──────────────────────┐   ┌───────────────────────────────────────────────────────────────┐
│ Client (JavaFX CAD)  │   │ Ambiente de execução: Compose local → Kubernetes (kind → k3s) │
│  app desktop, fora   │   │                                                               │
│  do cluster          │──>│               ┌──────────────────────────┐                    │
└──────────────────────┘   │               │   Spring Cloud Gateway   │  (só roteia)       │
        HTTPS / JWT        │               │       (Porta 8080)       │                    │
                           │               └─────┬──────────────┬─────┘                    │
                           │                     │              │                          │
                           │       ┌─────────────▼──────┐ ┌─────▼────────────────┐         │
                           │       │   Auth Service     │ │   Project Service    │         │
                           │       └─────────┬──────────┘ └──┬──────────────┬────┘         │
                           │                 │               │              │ (evento)     │
                           │       ┌─────────▼──────────┐ ┌──▼───────────┐ ┌▼───────────┐  │
                           │       │ PostgreSQL (Auth)  │ │ PostgreSQL   │ │  RabbitMQ  │  │
                           │       └────────────────────┘ │ (Projects)   │ └─────┬──────┘  │
                           │                              └──────────────┘       │         │
                           │                                     ┌───────────────▼──────┐  │
                           │                                     │ Render Worker Service│  │
                           │                                     └───────────┬──────────┘  │
                           └─────────────────────────────────────────────────┼─────────────┘
                                                                             │ AWS SDK
                                                                             ▼
                                                                  ┌──────────────────────┐
                                                                  │    AWS S3 Bucket     │
                                                                  └──────────────────────┘
```

* O **cliente JavaFX** é uma aplicação desktop: roda na máquina do usuário e conversa com o Gateway pela rede. Ele não é conteinerizado.
* O **Gateway** apenas roteia e valida tokens; cada serviço acessa o seu próprio banco.
* Os bancos e o broker ficam em rede interna, sem exposição pública.

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
* **Kubernetes (kind / k3s) & Helm:** Orquestração com Deployments, StatefulSets, probes, NetworkPolicy, RBAC e empacotamento em charts.
* **Argo CD:** Entrega contínua no modelo GitOps.
* **Trivy:** Varredura de vulnerabilidades das imagens no pipeline.
* **AWS (EC2 & S3) & Terraform:** Infraestrutura em nuvem de curta duração (ligada só durante os testes) e armazenamento de objetos para as imagens renderizadas.
* **Semantic Versioning (SemVer) & Git Tags:** Controle de releases automatizado (`v0.1.0-alpha`, `v1.0.0`) com geração automática de *Changelogs* no GitHub Releases.

---

## 🚀 Roadmap de Evolução & Releases

O roadmap foi reorganizado para que **cada versão corresponda a uma unidade da disciplina de containers**. O backend é o laboratório de DevOps; o cliente gráfico evolui numa trilha paralela, sem bloquear as entregas.

* [ ] **v0.1 — Base que funciona:** CI verde com Maven Wrapper; Compose corrigido (volumes nomeados, healthchecks, `.env`, portas em `127.0.0.1`).
* [ ] **v0.2 — Um serviço bem conteinerizado:** `project-service` (CRUD com PostgreSQL); Dockerfile multi-stage, usuário não-root, imagem enxuta, Spring Actuator; CI com scan Trivy e publicação no GHCR (tag do commit + SemVer).
* [ ] **v0.3 — Microsserviços no Compose:** `gateway`, `auth-service` e evento para o RabbitMQ; bancos em rede `internal`; `depends_on` com `condition: service_healthy`.
* [ ] **v0.4 — Kubernetes local (kind):** Deployments, Services, ConfigMap e Secret; PostgreSQL como StatefulSet com PVC; probes de liveness/readiness via Actuator; requests e limits; Ingress ou Gateway API.
* [ ] **v0.5 — Segurança no cluster:** NetworkPolicy *default-deny*; ServiceAccount com RBAC mínimo para o worker; Pod Security `restricted`.
* [ ] **v0.6 — Helm e GitOps:** chart dos serviços com `values-dev.yaml` e `values-prod.yaml`; Argo CD sincronizando a partir do Git.
* [ ] **v1.0 — Nuvem:** k3s numa EC2 maior (ligada só durante os testes) ou cluster gerenciado de curta duração via Terraform; `render-worker` gravando no S3.

**Trilha paralela — Cliente gráfico:** motor 2D em Java (`Vector2D`, `Matrix3x3`, transformações afins, Scene Graph com exportação JSON) e interface JavaFX. Até lá, os serviços são testados com `curl`.

---

## ⚠️ Problemas Conhecidos

* **CI:** o workflow usa `mvn` em vez de `./mvnw` e o Maven Wrapper ainda não está versionado em `client/`; o `chmod +x ./mvnw || true` mascara essa ausência.
* **Compose:** usa o atributo obsoleto `version`, não tem volumes nomeados nem healthchecks, publica as portas dos bancos em todas as interfaces e mantém senhas fixas no arquivo.
* **Dimensionamento:** uma EC2 `t2.micro` (1 GB de RAM) não comporta quatro JVMs Spring Boot, dois PostgreSQL e um RabbitMQ — os containers seriam finalizados por OOM (código 137).

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
│   ├── docker/
│   │   ├── docker-compose.yml     # Ambiente de desenvolvimento local
│   │   └── .env.example           # Variáveis de ambiente (copiar para .env)
│   ├── k8s/                       # Manifests Kubernetes (v0.4+)
│   └── helm/                      # Chart Helm (v0.6+)
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
git clone https://github.com/luiza-esteves/polydraw.git
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

> Enquanto o Maven Wrapper não estiver versionado (ver *Problemas Conhecidos*), use `mvn clean test` com um Maven local.

---

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

*Desenvolvido por **Luíza Esteves** como projeto prático de aprofundamento em Computação Gráfica, Engenharia de Software e DevOps.*

```
