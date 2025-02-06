package com.kamuridesu.count.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue()
    Long id;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    Long counter;
}
