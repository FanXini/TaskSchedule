package Decision;

import Util.Help;
import cluster_algorithm.K_Means;
import entity.EdgeNode;
import entity.Global;
import entity.Request;
import entity.SDN;
import pso.PSO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MTSDecision extends AbstractDecision {

    private int ealta=50; //每次调度的任务数量

    private int categoryNum=4; //簇的数量
    @Override
    public void dicision() {
        SDN SDN= Help.getSDN();
        List<EdgeNode> edgeNodeList=SDN.getEdgeNodeList();
        while (true){
            try{
                int scheduleTask=0;
                List<Request> watingClusterRequests=new ArrayList<>();
                while (scheduleTask++<ealta){
                    Request request=SDN.getRequestsQueue().take();
                    taskCount++;
                    watingClusterRequests.add(request);
                }
                Map<Request,List<Request>> clusters=new K_Means(watingClusterRequests).clustering(categoryNum);
//                for(Map.Entry<Request,List<Request>> entry:clusters.entrySet()){
//                    Request clusterCenter=entry.getKey();
//                    List<Request> requestInCluster=entry.getValue();
//                    System.out.println("簇心："+clusterCenter.getNormalizedDelaySensitivity()+" "+clusterCenter.getNormalizedDate());
//                    for(Request request:requestInCluster){
//                        System.out.println(request.getData()+" "+request.getDelaySensitivity());
//                    }
//                }
                System.out.println("***************************************************************");
                for(Map.Entry<Request,List<Request>> entry:clusters.entrySet()){
                    PSO pso=new PSO(entry.getValue());
                    pso.run();
                    float cost=(pso.getGlobalBestFitness()+ealta*(ealta-1)/(2* Global.lambda));
                    addCost(cost);
                    int solution[]=pso.getGlobalBestSolution();
                    for(int i=0;i<solution.length;i++){
                        int des=solution[i];
                        //调度给终端
                        if(des==-1){
                            watingClusterRequests.get(i).getTerminal().getQueue().put(watingClusterRequests.get(i));
                        }
                        else{
                            edgeNodeList.get(des).getQueue().put(watingClusterRequests.get(i));
                        }
                    }
                }
                if (isStoppingConditionReached()){
                    break;
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        dicision();
    }
}
