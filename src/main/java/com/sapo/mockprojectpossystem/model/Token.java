package com.sapo.mockprojectpossystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String token;

    private String tokenType = "BEARER";

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;
}
