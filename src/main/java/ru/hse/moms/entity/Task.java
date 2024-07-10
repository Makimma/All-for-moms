package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int rewardPoint;
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "setter_id")
    private User taskSetter;

    @ManyToOne
    @JoinColumn(name = "getter_id")
    private User taskGetter;

    private boolean isRecurring;
    private RecurrenceInterval recurrenceInterval;

    public enum RecurrenceInterval {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
