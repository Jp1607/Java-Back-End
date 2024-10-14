package com.example.demo.service;

import com.example.demo.Enum.Activity;
import com.example.demo.entities.Log;
import com.example.demo.entities.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.session.HttpSessionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogService {

    @Autowired
    private HttpSessionService httpSessionService;

    @Autowired
    private LogRepository logRepository;

    public LogService() {
        System.out.println(" estou sendo criada construtor vazio");
    }

    public LogService(HttpSessionService httpSessionService, LogRepository logRepository) {
        this.httpSessionService = httpSessionService;
        this.logRepository = logRepository;
    }

    public void saveLog(User user, Activity activity, String table, Long id) {
        Log log = new Log();
        log.setUser(user);
        log.setActivity(activity);
        log.setDate(new Date());
        log.setTableName(table);
        log.setTableId(id);
        System.out.println(logRepository == null);
        logRepository.save(log);
    }

    public void save(String token, Activity activity, String table, Long id) {
        String t = token.split(" ")[1];
        HttpSessionParam http = httpSessionService.getHttpSessionParam(t);
        User u = new User();
        u.setId(http.getUserDetails().getId());
        saveLog(u, activity, table, id);
    }

}
