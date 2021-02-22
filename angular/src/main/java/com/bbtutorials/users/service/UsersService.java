package com.bbtutorials.users.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bbtutorials.users.entity.Users;
import com.bbtutorials.users.repository.UsersRepository;

@Component
public class UsersService {
	
	private static UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        UsersService.usersRepository = usersRepository;
    }

    public static List<Users> getUsers() {
        return usersRepository.findAll();
    }
    
    public static Users saveUser(Users users) {
    	return usersRepository.save(users);
    }

}
