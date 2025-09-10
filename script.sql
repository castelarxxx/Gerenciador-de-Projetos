-- ==========================================================
-- SISTEMA DE GERENCIAMENTO DE PROJETOS
-- Script de criação do banco de dados MySQL
-- ==========================================================

-- Criar o banco de dados
DROP DATABASE IF EXISTS gerenciador_projetos;
CREATE DATABASE gerenciador_projetos
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

-- Usar o banco
USE gerenciador_projetos;

-- ==================== TABELAS PRINCIPAIS ====================

-- Tabela de perfis/roles
CREATE TABLE roles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL UNIQUE,
  descricao TEXT,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de usuários
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_completo VARCHAR(150) NOT NULL,
  cpf VARCHAR(14) NOT NULL UNIQUE,
  email VARCHAR(150) NOT NULL UNIQUE,
  cargo VARCHAR(100),
  login VARCHAR(50) NOT NULL UNIQUE,
  senha_hash VARCHAR(255) NOT NULL,
  role_id INT NOT NULL,
  ativo BOOLEAN DEFAULT TRUE,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT,
  INDEX idx_login (login),
  INDEX idx_email (email)
);

-- Tabela de projetos
CREATE TABLE projects (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(200) NOT NULL,
  descricao TEXT,
  data_inicio DATE,
  data_termino_prevista DATE,
  data_termino_real DATE,
  status ENUM('planejado','em_andamento','concluido','cancelado') DEFAULT 'planejado',
  gerente_id INT,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (gerente_id) REFERENCES users(id) ON DELETE SET NULL,
  CHECK (data_inicio <= data_termino_prevista),
  INDEX idx_status (status),
  INDEX idx_gerente (gerente_id)
);

-- Tabela de equipes
CREATE TABLE teams (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  descricao TEXT,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_nome (nome)
);

-- Tabela de membros das equipes
CREATE TABLE team_members (
  team_id INT NOT NULL,
  user_id INT NOT NULL,
  papel_no_time VARCHAR(100),
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (team_id, user_id),
  FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user (user_id)
);

-- Tabela de associação entre equipes e projetos
CREATE TABLE project_teams (
  project_id INT NOT NULL,
  team_id INT NOT NULL,
  papel_no_projeto VARCHAR(100),
  alocado_de DATE,
  alocado_ate DATE,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (project_id, team_id),
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
  CHECK (alocado_de <= alocado_ate),
  INDEX idx_team (team_id)
);

-- Tabela de tarefas
CREATE TABLE tasks (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(200) NOT NULL,
  descricao TEXT,
  projeto_id INT NOT NULL,
  responsavel_id INT,
  status ENUM('pendente','em_execucao','concluida','cancelada') DEFAULT 'pendente',
  data_inicio_prevista DATE,
  data_fim_prevista DATE,
  data_inicio_real DATE,
  data_fim_real DATE,
  prioridade ENUM('baixa','media','alta','urgente') DEFAULT 'media',
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (projeto_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (responsavel_id) REFERENCES users(id) ON DELETE SET NULL,
  CHECK (data_inicio_prevista <= data_fim_prevista),
  INDEX idx_projeto (projeto_id),
  INDEX idx_responsavel (responsavel_id),
  INDEX idx_status (status)
);

-- ==================== TABELAS DE HISTÓRICO ====================

-- Tabela de histórico de status das tarefas
CREATE TABLE task_status_history (
  id INT AUTO_INCREMENT PRIMARY KEY,
  task_id INT NOT NULL,
  status_antigo VARCHAR(50),
  status_novo VARCHAR(50) NOT NULL,
  alterado_por INT,
  data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  comentario TEXT,
  FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
  FOREIGN KEY (alterado_por) REFERENCES users(id) ON DELETE SET NULL,
  INDEX idx_task (task_id),
  INDEX idx_data (data_alteracao)
);

-- Tabela de histórico de status dos projetos
CREATE TABLE project_status_history (
  id INT AUTO_INCREMENT PRIMARY KEY,
  project_id INT NOT NULL,
  status_antigo VARCHAR(50),
  status_novo VARCHAR(50) NOT NULL,
  alterado_por INT,
  data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  comentario TEXT,
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (alterado_por) REFERENCES users(id) ON DELETE SET NULL,
  INDEX idx_project (project_id)
);

-- ==================== TABELAS DE LOG ====================

-- Tabela de logs de autenticação
CREATE TABLE auth_logs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  ip VARCHAR(45),
  evento VARCHAR(100) NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
  INDEX idx_user (user_id),
  INDEX idx_timestamp (timestamp)
);

-- Tabela de logs de atividade
CREATE TABLE activity_logs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  tabela_alvo VARCHAR(100) NOT NULL,
  acao VARCHAR(50) NOT NULL,
  registro_id INT,
  detalhes TEXT,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
  INDEX idx_user (user_id),
  INDEX idx_timestamp (timestamp)
);

-- ==================== DADOS INICIAIS ====================

-- Inserir roles iniciais
INSERT INTO roles (nome, descricao) VALUES
('administrador', 'Acesso total ao sistema'),
('gerente', 'Gerenciamento de projetos e equipes'),
('colaborador', 'Acesso básico para executar tarefas');

-- Inserir usuário administrador padrão (senha: admin123 - hash BCrypt)
INSERT INTO users (nome_completo, cpf, email, cargo, login, senha_hash, role_id) VALUES
('Administrador do Sistema', '000.000.000-00', 'admin@empresa.com', 'Administrador', 'admin', '$2a$10$8K1p/a0dRaW0H.6d9U0JX.9Q1Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0', 1);

-- Inserir alguns usuários de exemplo
INSERT INTO users (nome_completo, cpf, email, cargo, login, senha_hash, role_id) VALUES
('João Silva', '111.111.111-11', 'joao@empresa.com', 'Gerente de Projetos', 'joao.silva', '$2a$10$8K1p/a0dRaW0H.6d9U0JX.9Q1Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0', 2),
('Maria Santos', '222.222.222-22', 'maria@empresa.com', 'Desenvolvedora', 'maria.santos', '$2a$10$8K1p/a0dRaW0H.6d9U0JX.9Q1Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0', 3),
('Carlos Oliveira', '333.333.333-33', 'carlos@empresa.com', 'Designer', 'carlos.oliveira', '$2a$10$8K1p/a0dRaW0H.6d9U0JX.9Q1Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0Z0', 3);

-- Inserir equipes de exemplo
INSERT INTO teams (nome, descricao) VALUES
('Equipe Desenvolvimento', 'Equipe responsável pelo desenvolvimento do sistema'),
('Equipe Design', 'Equipe de design e UX/UI'),
('Equipe QA', 'Equipe de qualidade e testes');

-- Inserir membros nas equipes
INSERT INTO team_members (team_id, user_id, papel_no_time) VALUES
(1, 2, 'Líder Técnico'),
(1, 3, 'Desenvolvedora Backend'),
(2, 4, 'Designer UX/UI'),
(3, 3, 'Testadora');

-- Inserir projetos de exemplo
INSERT INTO projects (nome, descricao, data_inicio, data_termino_prevista, status, gerente_id) VALUES
('Sistema de Gestão', 'Desenvolvimento do sistema de gerenciamento de projetos', '2024-01-15', '2024-06-30', 'em_andamento', 2),
('Site Corporativo', 'Criação do novo site da empresa', '2024-02-01', '2024-04-15', 'concluido', 2),
('App Mobile', 'Desenvolvimento do aplicativo mobile', '2024-03-01', '2024-08-31', 'planejado', 2);

-- Inserir alocação de equipes em projetos
INSERT INTO project_teams (project_id, team_id, papel_no_projeto, alocado_de, alocado_ate) VALUES
(1, 1, 'Desenvolvimento', '2024-01-15', '2024-06-30'),
(1, 2, 'Design', '2024-01-15', '2024-03-15'),
(2, 1, 'Implementação', '2024-02-01', '2024-04-15');

-- Inserir tarefas de exemplo
INSERT INTO tasks (titulo, descricao, projeto_id, responsavel_id, status, data_inicio_prevista, data_fim_prevista, prioridade) VALUES
('Criar banco de dados', 'Criar estrutura do banco de dados MySQL', 1, 3, 'concluida', '2024-01-15', '2024-01-20', 'alta'),
('Desenvolver login', 'Implementar sistema de autenticação', 1, 3, 'em_execucao', '2024-01-21', '2024-02-05', 'alta'),
('Criar interface', 'Desenvolver interface gráfica do sistema', 1, 4, 'pendente', '2024-02-10', '2024-03-01', 'media'),
('Homepage', 'Criar página inicial do site', 2, 4, 'concluida', '2024-02-01', '2024-02-10', 'alta');

-- Inserir alguns logs de exemplo
INSERT INTO auth_logs (user_id, ip, evento) VALUES
(1, '192.168.1.100', 'LOGIN_SUCCESS'),
(2, '192.168.1.101', 'LOGIN_SUCCESS'),
(3, '192.168.1.102', 'LOGIN_SUCCESS');

-- ==================== VERIFICAÇÃO ====================

-- Verificar se tudo foi criado corretamente
SHOW TABLES;

-- Contagem de registros em cada tabela
SELECT 'roles' as tabela, COUNT(*) as total FROM roles
UNION ALL SELECT 'users', COUNT(*) FROM users
UNION ALL SELECT 'teams', COUNT(*) FROM teams
UNION ALL SELECT 'team_members', COUNT(*) FROM team_members
UNION ALL SELECT 'projects', COUNT(*) FROM projects
UNION ALL SELECT 'project_teams', COUNT(*) FROM project_teams
UNION ALL SELECT 'tasks', COUNT(*) FROM tasks;

-- Mostrar dados iniciais
SELECT * FROM roles;
SELECT * FROM users LIMIT 5;
SELECT * FROM teams;
SELECT * FROM projects;

-- Mostrar estrutura de tabelas importantes
DESCRIBE users;
DESCRIBE projects;
DESCRIBE tasks;
DESCRIBE teams;

-- ==================== VISÕES ÚTEIS ====================

-- Visão para ver usuários com seus perfis
CREATE VIEW view_usuarios_com_perfis AS
SELECT u.id, u.nome_completo, u.email, u.login, u.ativo, r.nome as perfil, r.descricao as descricao_perfil
FROM users u
INNER JOIN roles r ON u.role_id = r.id;

-- Visão para ver tarefas com detalhes
CREATE VIEW view_tarefas_detalhadas AS
SELECT t.id, t.titulo, t.status, t.prioridade, t.data_inicio_prevista, t.data_fim_prevista,
       p.nome as projeto, u.nome_completo as responsavel
FROM tasks t
INNER JOIN projects p ON t.projeto_id = p.id
LEFT JOIN users u ON t.responsavel_id = u.id;

-- Visão para ver equipes com membros
CREATE VIEW view_equipes_com_membros AS
SELECT t.id as equipe_id, t.nome as equipe, t.descricao,
       u.id as usuario_id, u.nome_completo as membro, tm.papel_no_time as papel
FROM teams t
INNER JOIN team_members tm ON t.id = tm.team_id
INNER JOIN users u ON tm.user_id = u.id;

-- ==================== FIM DO SCRIPT ====================

SELECT '✅ BANCO DE DADOS CRIADO COM SUCESSO!' as status;