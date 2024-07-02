package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.moms.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
