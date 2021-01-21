/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author syahirghariff
 */
public class ResponseUtil {
    
    public ResponseUtil(){
    }
    
    
    public static ResponseEntity success(Object content){
        
        Map res = new HashMap<>();
        res.put("status", HttpStatus.OK);
        res.put("content", content); 
        
        return ResponseEntity.ok(res);
    }
    
    public static ResponseEntity exception(Throwable err) {
        
        Map res = new HashMap<>();
        res.put("status", "ERROR");
        res.put("content", err.getMessage()); 
    
        return ResponseEntity.ok(res);
    }
    
}
