Perfeito 🚀
Vou organizar e formatar seu README em Markdown para que fique padronizado, bem estruturado e fácil de ler no GitHub.

Aqui está a versão revisada e formatada:

````markdown
# 📊 Sistema de Gerenciamento de Projetos

Sistema completo para gerenciamento de projetos, equipes e tarefas com controle de usuários e relatórios.

---

## 🚀 Funcionalidades

### 👥 Gestão de Usuários
- Cadastro de usuários com perfis (Admin, Gerente, Colaborador)  
- Controle de acesso baseado em *roles*  
- Autenticação segura com hash de senhas  

### 📋 Gestão de Projetos
- Criação e acompanhamento de projetos  
- Controle de prazos e status  
- Atribuição de gerentes responsáveis  

### 👥 Gestão de Equipes
- Criação de equipes de trabalho  
- Alocação de membros com papéis definidos  
- Associação de equipes a projetos  

### ✅ Gestão de Tarefas
- Criação e atribuição de tarefas  
- Controle de status e prazos  
- Histórico de alterações  

### 📊 Relatórios e Dashboards
- Andamento de projetos (% concluído)  
- Desempenho por colaborador  
- Alertas de projetos em risco  

---

## 🛠️ Tecnologias Utilizadas
- **Java** – Linguagem principal  
- **MySQL** – Banco de dados  
- **Swing** – Interface gráfica  
- **JDBC** – Conexão com banco  

---

## 📋 Pré-requisitos

### Software Necessário
- Java JDK 17 ou superior  
- MySQL Server 8.0 ou superior  
- IntelliJ IDEA (recomendado) ou outra IDE Java  

### Dependências
- MySQL Connector/J `8.0.33`  

---

## 🚀 Como Executar

### 1. Configurar o Banco de Dados
```bash
# Iniciar MySQL
sudo service mysql start

# Acessar MySQL
mysql -u root -p

# Executar script de criação do banco
source caminho/para/script.sql
````

### 2. Clonar e Abrir o Projeto

```bash
# Clonar ou baixar o projeto
git clone <url-do-projeto>

# Abrir no IntelliJ
File → Open → Selecionar pasta do projeto
```

### 3. Adicionar Driver MySQL

No IntelliJ:

```
File → Project Structure (Ctrl+Alt+Shift+S)
Modules → Dependencies
+ → JARs or directories
Selecionar mysql-connector-java-8.0.33.jar
```

### 4. Configurar Conexão com Banco

Editar `DBConnection.java` com suas credenciais:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gerenciador_projetos";
private static final String USER = "root";
private static final String PASSWORD = "sua_senha";
```

### 5. Executar a Aplicação

```bash
# Executar classe principal
java -cp .;mysql-connector-java-8.0.33.jar UI.GerenciadorApp
```

Ou pelo IntelliJ: botão **Run** ao lado da classe `GerenciadorApp`.

---

## 👤 Primeiro Acesso

Credenciais padrão:

* **Login:** admin
* **Senha:** admin123

---

## 🏗️ Estrutura do Projeto

```text
src/
├── br/com/seuempresa/gerenciador/
│   ├── model/          # Entidades (User, Project, Task, Team)
│   ├── dao/            # Acesso a dados (UserDAO, ProjectDAO)
│   ├── service/        # Lógica de negócio
│   ├── util/           # Utilidades (DBConnection)
│   └── security/       # Segurança (PasswordHasher)
├── UI/                 # Interfaces gráficas
└── resources/          # Recursos (se houver)
```

---

## 📊 Script do Banco de Dados

O banco inclui as seguintes tabelas:

* `users` – Usuários do sistema
* `roles` – Perfis de acesso
* `projects` – Projetos
* `teams` – Equipes
* `tasks` – Tarefas
* `team_members` – Membros das equipes
* `project_teams` – Equipes alocadas em projetos
* `task_status_history` – Histórico de tarefas

---

## 🔧 Configurações Importantes

### Variáveis de Ambiente

Certifique-se de que:

* MySQL está rodando na porta **3306**
* O banco **gerenciador\_projetos** existe
* O usuário possui privilégios necessários

---

## 🐞 Troubleshooting

### Erro de driver MySQL

```bash
# Verificar se o driver está no classpath
java -cp .;mysql-connector-java-8.0.33.jar SuaClasse
```

### Erro de conexão

* Verificar se MySQL está rodando
* Conferir usuário e senha no `DBConnection.java`
* Testar conexão com:

```bash
mysql -u root -p
```

### "Driver MySQL não encontrado"

* Verificar se o JAR do MySQL está no classpath
* Recompilar o projeto após adicionar o driver

### "Acesso negado ao banco"

* Verificar credenciais no `DBConnection.java`
* Conferir privilégios do usuário MySQL

### "Tabela não existe"

* Executar script SQL completo
* Verificar se o banco foi criado corretamente

---

## 📈 Funcionalidades por Perfil

### Administrador

* CRUD completo de usuários
* Acesso a todos os projetos e relatórios
* Gestão de perfis de acesso

### Gerente

* Criar e gerenciar seus projetos
* Atribuir tarefas à equipe
* Visualizar relatórios dos seus projetos

### Colaborador

* Visualizar tarefas atribuídas
* Atualizar status das tarefas
* Visualizar projetos participantes

---

## 📝 Licença

Este projeto é para fins **educacionais e de portfólio**.

---

## 👨‍💻 Desenvolvido por

**Hilary Castelar**


```
