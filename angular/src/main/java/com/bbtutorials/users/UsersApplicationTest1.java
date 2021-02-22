package com.bbtutorials.users;

import com.bbtutorials.users.controller.UsersController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.bbtutorials.users.entity.Users;

import performance_testing_framework.Tester;
import performance_testing_framework.subscribers.DatabaseSubscriber;
import performance_testing_framework.subscribers.FileSubscriber;

import java.sql.DriverManager;
import java.sql.*;

@SpringBootApplication
public class UsersApplicationTest1 {
	public static Connection testedDatabaseConnection;
	public static Connection resultsDatabaseConnection;

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);

		try {
			String urlTestedDb = "jdbc:mysql://localhost:3306/exampledb?autoReconnect=true";
			String urlResultsDb = "jdbc:mysql://localhost:3306/exampledb?autoReconnect=true";
			String userdb = "root";
			String password = "admin";
			testedDatabaseConnection = DriverManager.getConnection(urlTestedDb, userdb, password);
			resultsDatabaseConnection = DriverManager.getConnection(urlResultsDb, userdb, password);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		Users user = new Users(10, "a", "a", "a");
		Object[] params = {user};
		UsersController examplesInstance = new UsersController();

		Tester tester = new Tester.Builder()
				.testedClass(examplesInstance)
				.parameters(params)
				.dbConnector(testedDatabaseConnection)
				.build();

		tester.performTest();
		tester.showResults();

	}

}
