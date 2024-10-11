package com.example.demo.entities;
import com.example.demo.Enum.Activity;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
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
