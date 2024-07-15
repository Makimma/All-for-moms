package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.moms.entity.RecurringTaskInfo;
import ru.hse.moms.entity.User;

import java.util.Optional;

public interface RecurringTaskInfoRepository extends JpaRepository<RecurringTaskInfo, Long> {

}
