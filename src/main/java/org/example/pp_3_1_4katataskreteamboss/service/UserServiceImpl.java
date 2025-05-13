package org.example.pp_3_1_4katataskreteamboss.service;

import org.example.pp_3_1_4katataskreteamboss.dao.RoleDAO;
import org.example.pp_3_1_4katataskreteamboss.dao.UserDAO;
import org.example.pp_3_1_4katataskreteamboss.entity.Role;
import org.example.pp_3_1_4katataskreteamboss.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    private static final Long DEFAULT_USER_ROLE = 2L;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void saveNewUser(User user) {

        user.setEnabled(true);

        Set<Role> resolvedRoles = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                Role persisted = roleDAO.getRoleById(role.getId());
                if (persisted != null) {
                    resolvedRoles.add(persisted);
                }
            }
        }

        if (resolvedRoles.isEmpty()) {
            Role defaultRole = roleDAO.getRoleById(DEFAULT_USER_ROLE);
            if (defaultRole != null) {
                resolvedRoles.add(defaultRole);
            }
        }
        user.setRoles(resolvedRoles);

        if (user.getDepartment() == null) {
            user.setDepartment("IT");
        }

        if (user.getId() != null && user.getId() != 0) {
            User existing = userDAO.getUserById(user.getId());
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existing.getPassword());
            }
            user.setEnabled(existing.isEnabled());
        }

        userDAO.saveNewUser(user);
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userDAO.deleteUserById(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }
        return user;
    }
}
