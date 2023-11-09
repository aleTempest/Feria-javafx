package edu.upvictoria.poo.Lib;

public class UserFactory {
    public User createUser(Integer id, String firstName, String lastName, String email, String phoneNumber) {
        return new User(id,firstName,lastName,email,phoneNumber);
    }
}
