package com.bancosangue.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class LiquibaseRollbackScriptApplication {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3300/bancosangue";
        String username = "bancosangue";
        String password = "123admin";
        String changelogFile = "db/changelog/changelog-create-table.xml"; //edita o número do changelog

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            DataSource dataSource = createDataSource(url, username, password);
            Database database = createDatabase(connection);
            Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);

            liquibase.rollback(30, null);
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    private static DataSource createDataSource(String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private static Database createDatabase(Connection connection) throws DatabaseException {
        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
    }
}
