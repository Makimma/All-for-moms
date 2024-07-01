package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
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
}
