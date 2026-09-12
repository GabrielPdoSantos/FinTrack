CREATE DATABASE IF NOT EXISTS FinTrack;
USE FinTrack;

CREATE TABLE IF NOT EXISTS transacoes (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    descricao TEXT,
    valor REAL,
    ehReceita BOOLEAN,
    data DATE,
    is_mensal BOOLEAN,
    dia_vencimento INTEGER,
    data_inicio DATE,
    ativa BOOLEAN
);