package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Obligation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double money;

    @OneToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    private String description;

    @OneToOne
    @JoinColumn(name = "to_user")
    private User toUser;

    @Builder.Default
    private boolean isTransferred = false;
}
