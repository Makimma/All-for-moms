package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.moms.entity.Type;

public interface TypeRepository extends JpaRepository<Type, Long> {

}
