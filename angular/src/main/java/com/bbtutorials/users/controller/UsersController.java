package com.bbtutorials.users.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbtutorials.users.entity.Users;
import com.bbtutorials.users.links.UserLinks;
import com.bbtutorials.users.service.UsersService;

import lombok.extern.slf4j.Slf4j;
import performance_testing_framework.TestMethod;
import performance_testing_framework.Tester;

@Slf4j
@RestController
@RequestMapping("/api/")
public class UsersController {
    public static Connection testedDatabaseConnection;
	@Autowired
	UsersService usersService;

	@GetMapping(path = UserLinks.LIST_USERS)
    @TestMethod(testedValue = {Tester.TestStrategy.CLOCK, Tester.TestStrategy.PROCESSOR, Tester.TestStrategy.QUERY}, indicesOfParameters = {}, dbUser = "root")
    public ResponseEntity<?> listUsers(Optional<String> token) {
        log.info("UsersController:  list users");
        //List<Users> resource = UsersService.getUsers();
        List<Users> resource2 = new ArrayList<>();
        try {
            String urlTestedDb = "jdbc:mysql://localhost:3306/exampledb?autoReconnect=true";
            String user = "root";
            String password = "admin";
            Connection testedDatabaseConnection = DriverManager.getConnection(urlTestedDb, user, password);

            Statement myStmt = testedDatabaseConnection.createStatement();

            String sql = "SELECT * FROM exampledb.users "+ (token.isPresent() ? String.valueOf(token).replaceAll("\\[|\\]", "") : "") + ";";
            System.out.printf("\n\n" + sql + "\n\n");
            ResultSet rs = myStmt.executeQuery(sql);
            while (rs.next()) {
                Users one_user = new Users(rs.getLong("id"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"),rs.getString("EMAIL"));
                resource2.add(one_user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ResponseEntity.ok(resource2);
    }
	
	@PostMapping(path = UserLinks.ADD_USER)
    @TestMethod(testedValue = {Tester.TestStrategy.CLOCK, Tester.TestStrategy.PROCESSOR}, indicesOfParameters = {0}, dbUser = "root")
	public ResponseEntity<?> saveUser(@RequestBody Users userToAdd, Optional<String> token) {
        log.info("UsersController:  save user");
        //Users resource = UsersService.saveUser(userToAdd);

        try {
            String urlTestedDb = "jdbc:mysql://localhost:3306/exampledb?autoReconnect=true";
            String user = "root";
            String password = "admin";
            Connection testedDatabaseConnection = DriverManager.getConnection(urlTestedDb, user, password);

            Statement myStmt = testedDatabaseConnection.createStatement();

            String values = userToAdd.toStringDB() ;
            String sql =  "INSERT IGNORE INTO exampledb.users (id, FIRST_NAME, LAST_NAME, EMAIL) " +
                    values + ";";
            myStmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ResponseEntity.ok(userToAdd.toString());
    }
}
