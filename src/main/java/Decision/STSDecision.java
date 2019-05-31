package Decision;

import Util.Help;
import Util.IOUtils;
import entity.*;

import java.math.BigDecimal;

public class STSDecision extends AbstractDecision {
    @Override
    public void run() {
        dicision();
    }

    public void dicision() {
        SDN SDN= Help.getSDN();
        while (true){
            try{
                Request request=SDN.getRequestsQueue().take();
                long decisionStartTime=System.currentTimeMillis();
                Terminal originTerminal=request.getTerminal();
                taskCount++;
                //IOUtils.println("SDN","调度第"+(++taskCount)+"个任务，来自设备："+originTerminal.getId()+"的请求，请求量"+request.getData());
                float minWaitTimeInEdgeNode=Integer.MAX_VALUE;
                EdgeNode condidateEdgeNode=null;
                //选择等待时延最短的边缘节点
                while(condidateEdgeNode==null){
                    for(EdgeNode edgeNode:SDN.getEdgeNodeList()){
                        FogBlockQueue<Request> queue=edgeNode.getQueue();
                        //缓存不够
                        if(queue.getRemainCapcity()<request.getData()){
                            continue;
                        }
                        //计算出在该节点上的排队时延+正在处理的任务还需要时间
                        float waitTimeInThisEdgeNode=(queue.getCapacity()-queue.getRemainCapcity())/Global.EDGENODEFREQUENCE+edgeNode.getCurrentRequestNeedTime();
                        if(waitTimeInThisEdgeNode<minWaitTimeInEdgeNode){
                            minWaitTimeInEdgeNode=waitTimeInThisEdgeNode;
                            condidateEdgeNode=edgeNode;
                        }
                    }
                    if(condidateEdgeNode==null&&originTerminal.getQueue().getRemainCapcity()>=request.getData()){
                        break;
                    }
                }
                //如果边缘节点和终端设备的缓存都不足,拒绝任务
                if(originTerminal.getQueue().getRemainCapcity()<request.getData()&&condidateEdgeNode==null){
                    Help.refuseCount++;
                    IOUtils.println("SDN","没有足够的资源，拒绝任务");
                }
                //如果终端设备的缓存不足,任务直接发送给边缘节点
                else if(originTerminal.getQueue().getRemainCapcity()<request.getData()&&condidateEdgeNode!=null){
                    float processDelayInEdge=request.getData()/Global.EDGENODEFREQUENCE;
                    //总时延(传输+排队+执行)    注意：传输=边缘节点执行时间*10
                    float totalDelay=processDelayInEdge*30+minWaitTimeInEdgeNode+processDelayInEdge;
                    //总开支=时延开支(传输+排队+执行)+资源消耗开支
                    float costInEdgeNode=totalDelay+processDelayInEdge*Global.CostPRInEdgeNode;
                    condidateEdgeNode.getQueue().put(request);
                    Help.addCost(costInEdgeNode);
                    //IOUtils.println("SDN","将任务调度给"+condidateEdgeNode.getId()+"号边缘结点");
                }
                //如果边缘节点的缓存不足,但终端有足够的缓存，任务直接发送给终端
                else if(originTerminal.getQueue().getRemainCapcity()>request.getData()&&condidateEdgeNode==null){
                    float proccessDelayInTerminal=request.getData()/Global.TERMINALFREQUENCE;
                    //总时延(排队+执行)
                    float totalDelay=(originTerminal.getQueue().getCapacity()-originTerminal.getQueue().getRemainCapcity())/Global.TERMINALFREQUENCE+originTerminal.getCurrentRequestNeedTime()+proccessDelayInTerminal;
                    //总开支=时延开支(传输+排队+执行)+资源消耗开支
                    float costInTerminal=totalDelay+proccessDelayInTerminal*Global.CostPRInTerminal;
                    originTerminal.getQueue().put(request);
                    Help.addCost(costInTerminal);
                    //IOUtils.println("SDN","将任务调度给"+originTerminal.getId()+"号终端");
                }
                else if (originTerminal.getQueue().getRemainCapcity()>=request.getData()&&condidateEdgeNode!=null){
                    float processDelayInEdge=request.getData()/Global.EDGENODEFREQUENCE;
                    float proccessDelayInTerminal=request.getData()/Global.TERMINALFREQUENCE;
                    //边缘节点总时延(传输+排队+执行) *传输=边缘节点执行时间*10
                    float totalDelayInEdge=processDelayInEdge*30+minWaitTimeInEdgeNode+processDelayInEdge;
                    //总时延(排队+执行)
                    float totalDelayInTerminal=(originTerminal.getQueue().getCapacity()-originTerminal.getQueue().getRemainCapcity())/Global.TERMINALFREQUENCE+originTerminal.getCurrentRequestNeedTime()+proccessDelayInTerminal;
                    //总开支=时延开支(传输+排队+执行)+资源消耗开支
                    float costInEdgeNode=totalDelayInEdge+processDelayInEdge*Global.CostPRInEdgeNode;
                    float costInTerminal=totalDelayInTerminal+proccessDelayInTerminal*Global.CostPRInTerminal;
                    if(costInEdgeNode<=costInTerminal){
                        condidateEdgeNode.getQueue().put(request);
                        Help.addCost(costInEdgeNode);
                        //IOUtils.println("SDN","将任务调度给"+condidateEdgeNode.getId()+"号边缘结点");
                    }else{
                        originTerminal.getQueue().put(request);
                        Help.addCost(costInTerminal);
                        //IOUtils.println("SDN","将任务调度给"+originTerminal.getId()+"号终端");
                    }
                }
                if(isStoppingConditionReached()){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
