package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.moms.entity.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
}
