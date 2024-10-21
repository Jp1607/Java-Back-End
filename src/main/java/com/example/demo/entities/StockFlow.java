package com.example.demo.entities;

import com.example.demo.Enum.Flow;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock_local_change")
public class StockFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Long prodId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Long stockId;

    @Column(name = "quantity")
    private Long qnt;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type")
    private Flow flow;

    @Column(name = "date")
    private Date date;
}
