package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cost;
    private String description;
    private int quantity;
    private String shortName;

    public Reward(int cost,
                  String description,int quantity, String shortName) {
        this.shortName = shortName;
        this.quantity = quantity;
        this.cost = cost;
        this.description = description;
    }
}
