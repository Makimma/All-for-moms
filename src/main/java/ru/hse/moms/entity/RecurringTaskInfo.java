package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.hse.moms.periods.Interval;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class RecurringTaskInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User setter;

    @ManyToMany
    private List<User> getters;

    @ElementCollection
    private List<Interval> intervals;

    @ElementCollection
    private List<Integer> userOrder;

    private Date startDate;
    private Date endDate;

    private boolean isDeleted;
}