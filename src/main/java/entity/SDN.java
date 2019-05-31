package entity;

import Decision.Decision;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

@Component
@Data
public class SDN {
    private  LinkedTransferQueue<Request> requestsQueue; //请求队列

    private List<EdgeNode> edgeNodeList;  //维护边缘节点信息

    private Decision dicision;

    public SDN(){
        requestsQueue=new LinkedTransferQueue<>();
    }


}
