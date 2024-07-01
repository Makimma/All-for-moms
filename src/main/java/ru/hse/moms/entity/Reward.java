package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cost;
    private String description;
}
