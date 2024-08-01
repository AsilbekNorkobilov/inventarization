package com.example.demo.repo;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {

    @Query(value = "select t from Role t where t.roleName=:roleName")
    List<Role> findAllByRole(String roleName);
}
