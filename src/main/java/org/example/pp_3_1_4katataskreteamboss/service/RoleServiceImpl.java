package org.example.pp_3_1_4katataskreteamboss.service;

import org.example.pp_3_1_4katataskreteamboss.dao.RoleDAO;
import org.example.pp_3_1_4katataskreteamboss.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleRepository) {
        this.roleDAO = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    @Transactional
    public void addNewRole(Role role) {
        roleDAO.addNewRole(role);
    }

    @Override
    @Transactional
    public Role getRoleById(Long id) {
        return roleDAO.getRoleById(id);
    }


}
