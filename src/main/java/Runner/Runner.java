package Runner;

import Decision.AllToEdgeNodeDecision;
import Decision.MTSDecision;
import Decision.STSDecision;
import Util.Help;
import Util.IOUtils;
import entity.EdgeNode;
import entity.Global;
import entity.Terminal;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
@Data
public class Runner {

    public   List<EdgeNode> edgeNodeList;

    private  List<Terminal> terminalList;

    public Runner(String decisionName){
        init();
        run(decisionName);
    }

    public void init(){
        edgeNodeList= Help.createEdgeNodeList(Global.EDGENODENUM);
        Help.getSDN().setEdgeNodeList(edgeNodeList);
        terminalList=Help.createTerminalList(Global.TERMINLNUM);
        System.out.print(terminalList.size());
    }

    public void run(String decisionName){
        ThreadPoolExecutor threadPoolExecutor=Help.getThreadPool();
        //启动边缘节点
        for(EdgeNode edgeNode:edgeNodeList){
            threadPoolExecutor.execute(edgeNode);
        }
        switch (decisionName){
            case "stsDecision":
                Help.getSDN().setDicision(new STSDecision());
                Global.resultFilePath+="stsDecision\\edge"+Global.EDGENODENUM+"_terminal"+Global.TERMINLNUM;
                break;
            case "allToEdgeNodeDecision":
                Help.getSDN().setDicision(new AllToEdgeNodeDecision());
                Global.resultFilePath+="allToEdgeNodeDicision\\edge"+Global.EDGENODENUM+"_terminal"+Global.TERMINLNUM;
                break;
            case "mtsDecision":
                Help.getSDN().setDicision(new MTSDecision());
                Global.resultFilePath+="mtsDecision\\edge"+Global.EDGENODENUM+"_terminal"+Global.TERMINLNUM;
                break;
            default:throw new IllegalArgumentException("没有对应的决策器");
        }
        Help.createDir(Global.resultFilePath);
        //启动SDN
        threadPoolExecutor.execute(Help.getSDN().getDicision());
        //启动终端设备
        long processStartTime=System.currentTimeMillis();
        for(Terminal terminal:terminalList){
            terminal.run();
        }
        try{
            Help.getCountDownLatch().await();
            Help.getThreadPool().shutdown();
            for(EdgeNode edgeNode:edgeNodeList){
                if(edgeNode.getTotalProcessTime()>Help.totalProcessTime){
                    Help.totalProcessTime=edgeNode.getTotalProcessTime();
                }
            }
            IOUtils.println("SDN","执行完毕,耗时："+ Help.totalProcessTime);
            System.out.println("执行完毕,耗时："+ Help.totalProcessTime);
            System.out.println("执行完毕,开支："+ Help.toltalCost);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
