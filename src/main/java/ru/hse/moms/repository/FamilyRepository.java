package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.moms.entity.Family;
import ru.hse.moms.entity.User;

import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
    Optional<Family> findByHostsContaining(User host);
    boolean existsByHostsContains(User host);
    Optional<Family> findByMembersContaining(User host);
}
