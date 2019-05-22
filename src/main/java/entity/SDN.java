package entity;

import Dicision.Dicision;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

@Component
@Data
public class SDN {
    private  LinkedTransferQueue<Request> requestsQueue; //请求队列

    private List<EdgeNode> edgeNodeList;  //维护边缘节点信息

    private Dicision dicision;

    public SDN(){
        requestsQueue=new LinkedTransferQueue<>();
    }


}
