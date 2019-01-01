package com.newcoder.projectnowcoder.service;

import com.newcoder.projectnowcoder.model.User;
import org.springframework.stereotype.Service;

@Service
public class WendaService {


    public String getMessage (int userId) {
        return "Hello Message" + String.valueOf(userId);
    }
}
