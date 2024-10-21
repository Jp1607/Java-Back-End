package com.example.demo.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @Column(name = "total")
    private Long total;

    @Column(name = "date")
    private Date date;

    @Column(name = "sale_type")
    private Enum type;

}
