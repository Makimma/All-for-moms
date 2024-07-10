package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.moms.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}