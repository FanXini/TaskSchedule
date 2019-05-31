package pso;

import Util.DeepCopy;
import Util.Help;
import Util.RandomUtil;
import entity.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.*;
@Data
public class PSO {

    public   int swarmSize=20;

    public  int maxIterations=50;

    private List<Request> requests; //请求

    private List<Partical> swarm; //粒子群

    private int currentIteration;

    private int globalBestSolution[];

    private float globalBestFitness;

    private float W = 0.5f;

    private int c1 = 2, c2 = 2;

    private final int MaxConstraint = Global.EDGENODENUM - 1;

    private final int MinConstraint = -1;

    public PSO(List<Request> requests) {
        this.requests = requests;
        globalBestFitness = Float.MAX_VALUE;
    }

    public void run() {
        initSwarm();  //初始化粒子群
        while (!isStoppingConditionReached()) {
            evaluateSwarm(); //评估粒子群
            updateBestSolution(); //选择全局最好的解
            variance(); //变异
            updateVelocityAndSolution(); //修改速度和解
            //System.out.println("cost"+globalBestFitness);
        }

    }

    //初始化粒子群
    private void initSwarm() {
        swarm=new ArrayList<>();
        int dimension = requests.size();
        for (int i = 0; i < swarmSize; i++) {
            Partical partical = new Partical(dimension);
            partical.init();
            swarm.add(partical);
        }
    }

    /**
     * 评估粒子群，计算每一个的适应值
     */
    private void evaluateSwarm() {
        for (int i = 0; i < swarm.size(); i++) {
            fitnessFunction(swarm.get(i));
        }
    }

    private void updateBestSolution() {
        for (int i = 0; i < swarm.size(); i++) {
            Partical partical = swarm.get(i);
            if (partical.getBestFitness() < globalBestFitness) {
                globalBestFitness = partical.getBestFitness();
                globalBestSolution = partical.getBestSolution();
            }
        }
    }

    /**
     * 更新速度和解
     */
    private void updateVelocityAndSolution() {
        double r1, r2;
        for (int i = 0; i < swarm.size(); i++) {
            Partical partical = swarm.get(i);
            r1 = RandomUtil.nextDouble(0, 1);
            r2 = 1 - r1;  //r1+r2=1;
            int solution[] = partical.getSolution();
            int bestSolution[] = partical.getBestSolution();
            double velocity[] = partical.getVelocity();
            for (int index = 0; index < partical.getDimension(); index++) {
                velocity[index] = W * velocity[index] + c1 * r1 * (bestSolution[index] - solution[index]) + c2 * r2 * (globalBestSolution[index] - solution[index]);
                int newLocation = (int) (Math.round(solution[index] + velocity[index]));
                if (newLocation > MaxConstraint) {
                    newLocation = MaxConstraint;
                }
                if (newLocation < MinConstraint) {
                    newLocation = MinConstraint;
                }
                solution[index] = newLocation;
            }
            partical.setSolution(solution);
            partical.setVelocity(velocity);
        }

    }

    private void variance(){
        for(int i=0;i<swarm.size();i++){
            Partical partical=swarm.get(i);
            if(partical.getFitness()==partical.getBestFitness()){
                //变异，生成随机解
                int newSolution[]=new int[partical.getDimension()];
                double newVelocity[]=new double[partical.getDimension()];
                for(int index=0;index<partical.getDimension();index++){
                    newSolution[index] = (int) (Math.random() * (Global.EDGENODENUM + 1)) - 1;
                }
                partical.setSolution(newSolution);
                partical.setVelocity(newVelocity);
            }
        }
    }


    //计算适应值
    private void fitnessFunction(Partical partical) {
        //深度复制edgNodeList
        List<EdgeNode> simulationEdgeNodeList = DeepCopy.deepCopyList(Help.getSDN().getEdgeNodeList());
        Map<Integer, Terminal> terminalMap = new HashMap<>();
        int solution[] = partical.getSolution();
        float costs = 0;
        for (int index = 0; index < solution.length; index++) {
            int des = solution[index];
            Request request = requests.get(index);
            if (des == -1) { //调度给终端
                //复制品，避免影响到程序运作
                Terminal simulationTerminal;
                //teminal已被缓存
                if (terminalMap.containsKey(request.getTerminal().getId())) {
                    simulationTerminal = terminalMap.get(request.getTerminal().getId());
                } else {  //深度复制
                    simulationTerminal = DeepCopy.deepCopyList(request.getTerminal());
                    terminalMap.put(simulationTerminal.getId(), simulationTerminal);
                }
                FogBlockQueue<Request> queue = simulationTerminal.getQueue();
                //终端缓存不够，解无效
                if (queue.getRemainCapcity() < request.getData()) {
                    partical.setFitness(Float.MAX_VALUE);
                    return;
                } else {
                    float proccessDelayInTerminal = request.getData() / Global.TERMINALFREQUENCE;
                    //总时延(排队+执行)
                    float totalDelay = (simulationTerminal.getQueue().getCapacity() - simulationTerminal.getQueue().getRemainCapcity()) / Global.TERMINALFREQUENCE + simulationTerminal.getCurrentRequestNeedTime() + proccessDelayInTerminal;
                    //总开支=时延开支(传输+排队+执行)+资源消耗开支
                    float costInTerminal = totalDelay + proccessDelayInTerminal * Global.CostPRInTerminal;
                    costs += costInTerminal;
                    try {
                        queue.put(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else { //调度给边缘节点
                EdgeNode simulationEdgeNode = simulationEdgeNodeList.get(des);
                FogBlockQueue<Request> queue = simulationEdgeNode.getQueue();
                //缓存不够,解无效
                if (queue.getRemainCapcity() < request.getData()) {
                    partical.setFitness(Float.MAX_VALUE);
                    return;
                } else {
                    float processDelayInEdge = request.getData() / Global.EDGENODEFREQUENCE;

                    float WaitTimeInEdgeNode = (queue.getCapacity() - queue.getRemainCapcity()) / Global.EDGENODEFREQUENCE + simulationEdgeNode.getCurrentRequestNeedTime();
                    //总时延(传输+排队+执行)    注意：传输=边缘节点执行时间*10
                    float totalDelay = processDelayInEdge * 30 + WaitTimeInEdgeNode + processDelayInEdge;
                    //总开支=时延开支(传输+排队+执行)+资源消耗开支
                    float costInEdgeNode = totalDelay + processDelayInEdge * Global.CostPRInEdgeNode;

                    costs += costInEdgeNode;
                }
            }
        }
        partical.setFitness(costs);
        partical.selectBestSolution();
    }

    private boolean isStoppingConditionReached() {
        return ++currentIteration > maxIterations;
    }

}
