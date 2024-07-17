package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.moms.entity.Obligation;
import ru.hse.moms.entity.User;

import java.util.List;

@Repository
public interface ObligationRepository extends JpaRepository<Obligation, Long> {
    List<Obligation> findAllToUser(User toUser);
    List<Obligation> findAllFromUser(User fromUser);
}
