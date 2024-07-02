package ru.hse.moms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String name;
    private String password;
    private Date dateOfBirth;
    private int balance;

    @OneToMany(
            mappedBy = "taskSetter",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_rewards",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "reward_id"))
    private List<Reward> rewards;

    public User(String email,
                String name,
                String password,
                Date dateOfBirth) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }
}
