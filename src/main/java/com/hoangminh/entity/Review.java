package com.hoangminh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer rating;

    private String comment;

    @Column(name = "created_at")
    private Date createdAt;
}