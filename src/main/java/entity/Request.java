package entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class Request {
    private int id;
    private Terminal terminal; //是哪个terminal发出的任务请求
    private float data;

    public Request(int id,Terminal terminal,float data){
        this.id=id;
        this.terminal=terminal;
        this.data=data;
    }

}
