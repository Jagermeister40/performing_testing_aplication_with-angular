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
public class UsersApplication {
	public static Connection testedDatabaseConnection;
	public static Connection resultsDatabaseConnection;

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

}
