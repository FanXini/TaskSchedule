package Util;

import entity.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Help {

    private static volatile SDN SDN;

    private volatile static ThreadPoolExecutor threadPoolExecutor;

    private volatile static CountDownLatch countDownLatch;

    public static float toltalCost=0;

    public static float totalProcessTime=0;

    public static int  refuseCount=0;

    public static volatile boolean scheduleCompleteFlag=false;


    public static void createDir(String dirName){
        try{
            Files.createDirectories(Paths.get(dirName));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<EdgeNode> createEdgeNodeList(int edgeNum){
        List<EdgeNode> edgeNodeList=new ArrayList<>();
        for(int i=0;i<edgeNum;i++){
            edgeNodeList.add(new EdgeNode(i+1,Global.EDGENODEFREQUENCE));
        }
        return edgeNodeList;
    }

    public static List<Terminal> createTerminalList(int teminalNum){
        List<Terminal> terminalList=new ArrayList<Terminal>();
        for(int i=0;i<teminalNum;i++){
            terminalList.add(new Terminal(i+1,Global.TERMINALFREQUENCE));
        }
        return terminalList;
    }

    public static SDN getSDN(){
        if(SDN==null){
            synchronized (Help.class){
                if(SDN==null){
                    SDN=new SDN();
                }
            }
        }
        return SDN;
    }

    public static ThreadPoolExecutor getThreadPool(){
        if(threadPoolExecutor==null){
            synchronized (Help.class){
                if(threadPoolExecutor==null){
                    threadPoolExecutor=new ThreadPoolExecutor(100,1000,500,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
                }
            }
        }
        return threadPoolExecutor;
    }

    public static CountDownLatch getCountDownLatch(){
        if(countDownLatch==null){
            synchronized (Help.class){
                if(countDownLatch==null){
                    switch (Global.decisionName){
                        case "stsDecision":
                            countDownLatch=new CountDownLatch(Global.EDGENODENUM+Global.TERMINLNUM);
                            break;
                        case "allToEdgeNodeDecision":
                            countDownLatch=new CountDownLatch(Global.EDGENODENUM);
                            break;
                        case "mtsDecision":
                            countDownLatch=new CountDownLatch(Global.EDGENODENUM+Global.TERMINLNUM);
                            break;
                        default:throw new IllegalArgumentException("没有对应的决策器");
                    }
                }
            }
        }
        return  countDownLatch;
    }


    public static void addCost(float cost){
            toltalCost+=cost;
    }



}
