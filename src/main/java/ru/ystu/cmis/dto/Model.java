package ru.ystu.cmis.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Model {
    private Map<String,Object> obj;
    public Model(){
        obj = new HashMap<>();
    }
    public void put(String key, Object val){
        obj.put(key,val);
    }
    public Map<String,Object> get(){
        return obj;
    }
}
