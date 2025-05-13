package org.example.pp_3_1_4katataskreteamboss.dao;

import org.example.pp_3_1_4katataskreteamboss.entity.Role;

import java.util.List;

public interface RoleDAO {

    List<Role> getAllRoles();

    void addNewRole(Role role);

    Role getRoleById(Long id);

}
