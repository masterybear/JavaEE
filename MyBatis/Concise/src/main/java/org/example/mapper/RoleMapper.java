package org.example.mapper;

import org.example.pojo.Role;

import java.util.List;

public interface RoleMapper {
    int insertRole(Role role);

    int deleteRole(Long id);

    int updateRole(Role role);

    Role getRole(Long id);

    List<Role> findRoles(String roleName);
}
