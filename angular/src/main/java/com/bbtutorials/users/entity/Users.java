package com.bbtutorials.users.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


import lombok.Data;

@Entity
@Data
public class Users {
	
	@Id
	@Column
    private long id;

    @Column
    @NotNull(message="{NotNull.User.firstName}")
    private String firstName;
    
    @Column
    @NotNull(message="{NotNull.User.lastName}")
    private String lastName;
    
    @Column
    @NotNull(message="{NotNull.User.email}")
    private String email;


    public Users(long id, @NotNull(message = "{NotNull.User.firstName}") String firstName, @NotNull(message = "{NotNull.User.lastName}") String lastName, @NotNull(message = "{NotNull.User.email}") String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Users() {
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String toStringDB(){
        return "VALUES ('"+ id +"', '" + firstName + "', '" + lastName + "', '" + email + "') ";
    }
}
