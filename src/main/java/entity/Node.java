package entity;

import lombok.Data;

@Data
public class Node {
    //延时敏感度
    public float delaySensitivity;

    //任务计算量
    public float data;

    public Node(float delaySensitivity,float data){
        this.delaySensitivity=delaySensitivity;
        this.data=data;
    }
}
