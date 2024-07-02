package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.moms.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);
}
