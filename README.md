# 📎 Encurtador de URL – Desafio Técnico

Projeto backend desenvolvido como **desafio técnico**, implementando um **encurtador de URLs** com Java EE, JAX-RS, JPA/Hibernate e testes de integração.

O foco do projeto é demonstrar **boa arquitetura**, **separação de responsabilidades**, **boas práticas de backend** e **testes reais de API**.

---

## 🚀 Funcionalidades

- Criar URL encurtada  
- Listar URLs cadastradas  
- Buscar URL por ID  
- Atualizar URL (original e alias)  
- Remover URL  
- Redirecionar usando alias ou shortCode  
- Contabilizar acessos (hits)  
- Autenticação **mock** com sessão + JWT (demonstração)  
- Testes de integração com **RestAssured**  

---

## 🧱 Arquitetura do Projeto

Arquitetura em camadas:

```
Controller  →  Service  →  Repository  →  Banco de Dados
```

### 📦 Pacotes

```
br.com.encurtador
 ├── auth        → autenticação mock
 ├── url         → domínio principal
 ├── generic     → contratos genéricos
 ├── util        → utilitários
 └── config      → configuração JAX-RS
```

---

## 🔐 Autenticação (Mock)

Endpoint:
```
POST /rest/api/auth/login
```

Credenciais:
```
user / 123456
```

Cria sessão (`JSESSIONID`) e retorna um JWT apenas para demonstração.

---

## 🌐 Endpoints

### Criar URL
```
POST /rest/api/url/save
```

Body:
```json
{
  "originalUrl": "https://www.google.com",
  "alias": "meu-alias"
}
```

### Listar URLs
```
GET /rest/api/url/list
```

### Buscar por ID
```
GET /rest/api/url/find/{id}
```

### Atualizar URL
```
PUT /rest/api/url/update/{id}
```

### Deletar URL
```
DELETE /rest/api/url/delete/{id}
```

### Redirecionamento
```
GET /rest/api/url/redirecionamento/{aliasOuShortCode}
```

---

## 🧪 Testes de Integração

- Implementados com **RestAssured**
- Executados via **Maven Failsafe**
- Testes reais contra aplicação em execução

Executar:
```bash
mvn clean verify -DbaseUrl=http://localhost:8080/encurtador-url
```

---

## ▶️ Como Executar

### Pré-requisitos
- Java 8
- Maven
- WildFly
- MySQL
- DataSource configurado no WildFly:
```
java:/MySqlDS
```

### Build
```bash
mvn clean package
```

### Deploy
Copiar o WAR para:
```
wildfly/standalone/deployments/encurtador-url.war
```

---

## 🗄️ Banco de Dados

- JPA / Hibernate
- MySQL InnoDB
- Criação automática:
```
hibernate.hbm2ddl.auto=update
```

Tabela:
```
url
```

---

## 👨‍💻 Autor

**Edson Aquino**  
Analista de Sistemas | Backend Java  

Projeto desenvolvido para fins de **avaliação técnica e estudo**.
