package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Date startDate;
    private Date endDate;
    private int rewardPoint;

    @ManyToOne
    @JoinColumn(name = "setter_id")
    private User taskSetter;

    @ManyToOne
    @JoinColumn(name = "getter_id")
    private User taskGetter;

    public Task(String description,
                Date startDate,
                Date endDate,
                int rewardPoint) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardPoint = rewardPoint;
    }
}
