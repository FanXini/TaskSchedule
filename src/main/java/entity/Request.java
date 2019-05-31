package entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
public class Request implements Serializable{
    private static final long serialVersionUID = 58545921921688207L;
    private int id;
    private Terminal terminal; //是哪个terminal发出的任务请求
    private float data;
    //延时敏感度
    private float delaySensitivity;

    //归一化后的延时敏感度
    private float  normalizedDelaySensitivity;

    //归一化后的任务计算量
    private float normalizedDate;


    public Request(){

    }

    public Request(int id,Terminal terminal,float data){
        this.id=id;
        this.terminal=terminal;
        this.data=data;
    }

    public Request(float normalizedDate,float normalizedDelaySensitivity){
        this.normalizedDate=normalizedDate;
        this.normalizedDelaySensitivity=normalizedDelaySensitivity;
    }


}
