package br.com.gabriel.fintrack.dao;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USUARIO = dotenv.get("DB_USER");
    private static final String SENHA = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);

    }

 }
