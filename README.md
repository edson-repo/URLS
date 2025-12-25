# 🔗 Encurtador de URL — Java (JAX-RS + WildFly)

Aplicação web para **encurtamento e gerenciamento de URLs**, desenvolvida em **Java EE** utilizando **JAX-RS**, **WildFly**, **JPA/Hibernate** e **Bootstrap**.

O sistema permite **cadastrar**, **editar**, **remover**, **listar** e **redirecionar URLs**, com controle de acessos (*hits*) e um **login mock** para simulação de autenticação.

---

## 📌 Visão Geral

Este projeto foi criado com foco em:

- Demonstração de **arquitetura Java EE tradicional**
- Desenvolvimento de **APIs REST com JAX-RS**
- Integração **Frontend (HTML/JS) + Backend**
- Boas práticas de organização e separação de camadas
- CRUD completo com persistência em banco de dados

---

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 8
- JAX-RS (RESTEasy)
- CDI
- JPA / Hibernate
- MySQL
- JWT (uso demonstrativo)
- WildFly

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)
- Bootstrap 5

---

## 📁 Estrutura do Projeto

```
encurtador-url/
├── src/main/java
│   └── br.com.encurtador
│       ├── auth        # Login mock e geração de JWT
│       ├── url         # CRUD e redirecionamento de URLs
│       ├── config      # Configuração JAX-RS
│       └── util        # Classes utilitárias
│
├── src/main/webapp
│   ├── login.html      # Tela de login
│   └── bemvindo.html   # Dashboard (CRUD)
│
├── pom.xml
└── README.md
```

---

## 🔐 Autenticação (Mock)

A autenticação é **simulada**, apenas para controle de fluxo da aplicação.

**Credenciais padrão:**

Usuário: user  
Senha: 123456

Após o login:
- O usuário é armazenado no `localStorage`
- Um token JWT simples é gerado (apenas para demonstração)
- O acesso à tela principal é liberado

> ⚠️ Atenção: não se trata de um mecanismo de segurança real.

---

## 🔁 Redirecionamento de URLs

O redirecionamento ocorre por meio de um endpoint REST que aceita **alias** ou **shortCode**.

Exemplo:

```
GET /encurtador-url/rest/api/url/redirecionamento/{codigo}
```

```
http://localhost:8080/encurtador-url/rest/api/url/redirecionamento/g1
```

---

## 📡 Endpoints Principais

### Autenticação
- POST `/rest/api/auth/login`

### URLs
- GET `/rest/api/url/list`
- POST `/rest/api/url/save`
- PUT `/rest/api/url/update/{id}`
- DELETE `/rest/api/url/delete/{id}`
- GET `/rest/api/url/redirecionamento/{code}`

---

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior
- Maven
- WildFly
- MySQL configurado como DataSource no WildFly

### Build
```
mvn clean package
```

### Deploy
Copiar o arquivo:

```
target/encurtador-url.war
```

Para:

```
WILDFLY_HOME/standalone/deployments
```

Iniciar o WildFly:

```
standalone.bat
```

Acessar no navegador:

```
http://localhost:8080/encurtador-url/login.html
```

---

## 👨‍💻 Autor

Edson  
Projeto desenvolvido para fins de estudo e demonstração técnica.
