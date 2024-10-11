package com.example.demo.controller;
import com.example.demo.Enum.Activity;
import com.example.demo.entities.Log;
import com.example.demo.entities.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.session.HttpSessionParam;
import com.example.demo.session.HttpSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

public class LogController {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

    private String token;

    private Activity activity;

    private String table;

    private Long id;

    public LogController() {}

    public LogController(String token, String table, Long id, Activity activity) {
this.token = token;
this.table = table;
this.id = id;
this.activity = activity;
    }


    public void save(){
        String t = token.split(" ")[1];
        HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
        User u = new User();
        u.setId(http.getUserDetails().getId());
        Log log = new Log();
        log.setUser(u);
        log.setActivity(activity);
        log.setDate(new Date());
        log.setTableName(table);
        log.setTableId(id);
        logRepository.save(log);
    }

}
