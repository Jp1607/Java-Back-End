package com.example.demo.entities;

import com.example.demo.Enum.Activity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "table_name", columnDefinition = "varchar(50)", length = 50, nullable = true)
    private String tableName;

    @Column(name = "table_id", nullable = true)
    private Long tableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity", columnDefinition = "varchar(50)", length = 50, nullable = false)
    private Activity activity;

    @Column(name = "date")
    private Date date;

    public Log(Long id, User user, String tableName, Long tableId, Activity activity, Date date) {
        this.id = id;
        this.user = user;
        this.tableName = tableName;
        this.tableId = tableId;
        this.activity = activity;
        this.date = date;
    }

    public Log() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
