package org.example.pp_3_1_4katataskreteamboss.controller;

import org.example.pp_3_1_4katataskreteamboss.entity.User;
import org.example.pp_3_1_4katataskreteamboss.service.RoleService;
import org.example.pp_3_1_4katataskreteamboss.service.UserService;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "roles",
                new CustomCollectionEditor(Set.class) {
                    @Override
                    protected Object convertElement(Object element) {
                        if (element instanceof String) {
                            try {
                                Long roleId = Long.parseLong((String) element);
                                return roleService.getRoleById(roleId);
                            } catch (NumberFormatException ignored) {}
                        }
                        return null;
                    }
                });
    }


    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String showUserPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/api/admin/users")
    @ResponseBody
    public List<User> apiGetAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/admin/user/{id}")
    @ResponseBody
    public ResponseEntity<User> apiGetUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/api/admin/user")
    @ResponseBody
    public ResponseEntity<User> apiCreateUser(@RequestBody User user) {
        userService.saveNewUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/api/admin/user")
    @ResponseBody
    public ResponseEntity<User> apiUpdateUser(@RequestBody User user) {
        userService.saveNewUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/api/admin/user/{id}")
    @ResponseBody
    public ResponseEntity<Void> apiDeleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/admin/user/{id}/enabled")
    @ResponseBody
    public ResponseEntity<Void> apiToggleEnabled(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body
    ) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setEnabled(Boolean.TRUE.equals(body.get("enabled")));
        userService.saveNewUser(user);
        return ResponseEntity.ok().build();
    }
}
