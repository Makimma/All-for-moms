package ru.hse.moms.periods;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Interval {
    private int duration;
}
