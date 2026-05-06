-- Tabela Pessoa (base para Aluno e Professor)
CREATE TABLE pessoa (
    cpf bigint NOT NULL PRIMARY KEY,
    nome varchar(100) NOT NULL,
    data_nascimento date NOT NULL
);

-- Tabela Aluno (herda de Pessoa)
CREATE TABLE aluno (
    cpf bigint NOT NULL PRIMARY KEY,
    FOREIGN KEY (cpf) REFERENCES pessoa(cpf) ON DELETE CASCADE
);

-- Tabela Professor (herda de Pessoa)
CREATE TABLE professor (
    cpf bigint NOT NULL PRIMARY KEY,
    titulacao varchar(50) NOT NULL,
    FOREIGN KEY (cpf) REFERENCES pessoa(cpf) ON DELETE CASCADE
);

-- Tabela Curso
CREATE TABLE curso (
    id int NOT NULL PRIMARY KEY,
    nome varchar(100) NOT NULL,
    carga_horaria int NOT NULL
);

-- Tabela Turma
CREATE TABLE turma (
    id SERIAL PRIMARY KEY,
    horario varchar(20) NOT NULL,
    limite_alunos int NOT NULL,
    fechada boolean NOT NULL DEFAULT false,
    data_inicio date NOT NULL,
    data_fim date NOT NULL,
    curso_id int NOT NULL,
    professor_cpf bigint NOT NULL,
    FOREIGN KEY (curso_id) REFERENCES curso(id) ON DELETE CASCADE,
    FOREIGN KEY (professor_cpf) REFERENCES professor(cpf) ON DELETE CASCADE
);

-- Tabela turma_alunos (relação muitos-para-muitos entre Turma e Aluno, sem atributos de matrícula)
CREATE TABLE turma_alunos (
    turma_id int NOT NULL,
    aluno_cpf bigint NOT NULL,
    PRIMARY KEY (turma_id, aluno_cpf),
    FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    FOREIGN KEY (aluno_cpf) REFERENCES aluno(cpf) ON DELETE CASCADE
);

-- Índices para melhor performance em queries
CREATE INDEX idx_turma_alunos_turma ON turma_alunos(turma_id);
CREATE INDEX idx_turma_alunos_aluno ON turma_alunos(aluno_cpf);
