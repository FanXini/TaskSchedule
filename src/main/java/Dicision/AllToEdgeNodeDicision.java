package Dicision;

import Util.Help;
import Util.IOUtils;
import entity.*;

import java.math.BigDecimal;

public class AllToEdgeNodeDicision implements Dicision {
    @Override
    public void dicision() {
        int taskCount = 0;
        SDN SDN = Help.getSDN();
        while (true) {
            try {
                Request request = SDN.getRequestsQueue().take();
                Terminal originTerminal = request.getTerminal();
                IOUtils.println("SDN", "调度第" + (++taskCount) + "个任务，来自设备：" + originTerminal.getId() + "的请求，请求量" + request.getData());
                float minWaitTimeInEdgeNode=Integer.MAX_VALUE;
                EdgeNode condidateEdgeNode=null;
                while (condidateEdgeNode==null){
                    for(EdgeNode edgeNode:SDN.getEdgeNodeList()){
                        FogBlockQueue<Request> queue=edgeNode.getQueue();
                        //缓存不够
                        if(queue.getRemainCapcity()<request.getData()){
                            continue;
                        }
                        //计算出在该节点上的排队时延
                        float waitTimeInThisEdgeNode=(queue.getCapacity()-queue.getRemainCapcity())/Global.EDGENODEFREQUENCE;
                        if(waitTimeInThisEdgeNode<minWaitTimeInEdgeNode){
                            minWaitTimeInEdgeNode=waitTimeInThisEdgeNode;
                            condidateEdgeNode=edgeNode;
                        }
                    }
                }
                //如果边缘节点和终端设备的缓存都不足,拒绝任务
                if(condidateEdgeNode==null){
                    Help.refuseCount++;
                    IOUtils.println("SDN","没有足够的资源，拒绝任务");
                }else{
                    float processDelayInEdge=request.getData()/Global.EDGENODEFREQUENCE;
                    //总开支=时延开支(传输+排队+执行)+资源消耗开支
                    float costInEdgeNode=processDelayInEdge*10+minWaitTimeInEdgeNode+processDelayInEdge+processDelayInEdge*Global.CostPRInEdgeNode;
                    condidateEdgeNode.getQueue().put(request);
                    Help.addCost(costInEdgeNode);
                    IOUtils.println("SDN","将任务调度给"+condidateEdgeNode.getId()+"号边缘结点");
                }
                if(taskCount>=Global.TERMINLNUM*Global.REQUESTNUM){
                    Float num=Help.toltalCost;
                    String str=new BigDecimal(num.toString()).toString();
                    IOUtils.println("SDN","totalCost"+ str);
                    IOUtils.println("SDN","总任务数量："+taskCount+"\n拒绝量："+Help.refuseCount+"\n拒绝率："+(float)Help.refuseCount/taskCount);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        dicision();
    }
}
