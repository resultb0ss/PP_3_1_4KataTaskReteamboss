package org.example.pp_3_1_4katataskreteamboss.service;

import org.example.pp_3_1_4katataskreteamboss.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void saveNewUser(User user);

    User getUserById(Long id);

    void deleteUserById(Long id);

    User findByUsername(String username);

}
