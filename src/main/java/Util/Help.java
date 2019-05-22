package Util;

import entity.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Help {

    private static volatile SDN SDN;

    private static ThreadPoolExecutor threadPoolExecutor;

    public static float toltalCost=0;

    public static int  refuseCount=0;

    private static Lock lock=new ReentrantLock();

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
                    threadPoolExecutor=new ThreadPoolExecutor(200,1000,500,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
                }
            }
        }
        return threadPoolExecutor;
    }

    public static void addCost(float cost){
        lock.lock();
        try {
            toltalCost+=cost;
        }finally {
            lock.unlock();
        }
    }


}
