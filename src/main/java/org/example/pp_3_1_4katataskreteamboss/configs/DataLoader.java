package org.example.pp_3_1_4katataskreteamboss.configs;

import org.example.pp_3_1_4katataskreteamboss.entity.Role;
import org.example.pp_3_1_4katataskreteamboss.entity.User;
import org.example.pp_3_1_4katataskreteamboss.service.RoleService;
import org.example.pp_3_1_4katataskreteamboss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DataLoader(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin") == null) {
            Role adminRole = new Role(1L, "ROLE_ADMIN");
            Role userRole = new Role(2L, "ROLE_USER");
            roleService.addNewRole(adminRole);
            roleService.addNewRole(userRole);
            User admin = new User("admin", "Adminov", "admin", "admin", 100000, "IT");
            admin.setRoles(Set.of(adminRole, userRole));
            userService.saveNewUser(admin);
            User user = new User("user", "Userov", "user", "user", 50000, "Sales");
            user.setRoles(Set.of(userRole));
            userService.saveNewUser(user);
        }
    }
}
