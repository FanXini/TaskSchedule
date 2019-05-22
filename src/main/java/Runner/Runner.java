package Runner;

import Dicision.AllToEdgeNodeDicision;
import Dicision.STMDicision;
import Util.Help;
import entity.EdgeNode;
import entity.Global;
import entity.SDN;
import entity.Terminal;
import lombok.Data;

import java.math.BigDecimal;
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
            case "stmDicision":
                Help.getSDN().setDicision(new STMDicision());
                Global.resultFilePath+="stmDicision\\edge"+Global.EDGENODENUM+"_terminal_400"+Global.TERMINLNUM;
                break;
            case "allToEdgeNodeDicision":
                Help.getSDN().setDicision(new AllToEdgeNodeDicision());
                Global.resultFilePath+="allToEdgeNodeDicision\\edge"+Global.EDGENODENUM+"_terminal"+Global.TERMINLNUM;
                break;
            default:throw new IllegalArgumentException("没有对应的决策器");
        }
        Help.createDir(Global.resultFilePath);
        //启动SDN
        threadPoolExecutor.execute(Help.getSDN().getDicision());
        //启动终端设备
        for(Terminal terminal:terminalList){
            terminal.run();
        }
        try{
            Thread.sleep(30000);
            Float num=Help.toltalCost;
            String str=new BigDecimal(num.toString()).toString();
            System.out.println(str);
            System.out.println("拒绝量："+Help.refuseCount);
        }catch (Exception e){

        }
    }
}
