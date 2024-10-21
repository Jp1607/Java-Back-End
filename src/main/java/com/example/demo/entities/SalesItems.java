package com.example.demo.entities;

import com.example.demo.Enum.Discount;

import javax.persistence.*;

@Entity
@Table
public class SalesItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @JoinColumn(name = "sales_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Long salesId;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Long prodId;

    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Long stockId;

    @Column(name = "discount_type")
    private Discount discount;

    @Column(name = "quantity")
    private Long qnt;

    @Column(name = "product_value")
    private Long prodValue;

    @Column(name = "sub_total")
    private Long subTotal;

}
