package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.moms.entity.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
}
