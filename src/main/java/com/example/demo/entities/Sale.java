package com.example.demo.entities;

import com.example.demo.Enum.Payment;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Entity
@Table
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @Column(name = "total")
    private Double total;

    @Column(name = "date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type", columnDefinition = "varchar(50)", length = 50, nullable = false)
    private Payment payment;

    @Column(name = "exclude")
    private Integer excluded = 0;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    public Sale(){}
    public Sale(  Date date, Payment payment,  User user, Double total) {
        this.date = date;
        this.payment = payment;
        this.user = user;
        this.total = total;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Integer getExcluded() {
        return excluded;
    }

    public void setExcluded(Integer excluded) {
        this.excluded = excluded;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
