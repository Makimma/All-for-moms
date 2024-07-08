package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.moms.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
