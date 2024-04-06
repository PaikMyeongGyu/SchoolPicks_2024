package org.schoolPicks.api.service;

import org.springframework.stereotype.Service;

@Service
public class MockMvcService {

    public String message(){
        return "MockMvcController is operated";
    }

    public String messageWithName(String name){
        return "MockMvcController is operated by " + name;
    }

}
