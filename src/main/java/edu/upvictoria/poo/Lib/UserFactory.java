package edu.upvictoria.poo.Lib;

public class UserFactory {
    public static User createUser(Integer id, String firstName, String lastName, String email, String phoneNumber, String username, String password) {
        return new User(id,firstName,lastName,email,phoneNumber,username,password);
    }
}
