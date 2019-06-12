package entity;

import java.util.concurrent.ArrayBlockingQueue;

public class Global {


    /*决策算法选择*/
    //public final static String decisionName="stsDecision";
    //public final static String decisionName="allToEdgeNodeDecision";
    public final static String decisionName="mtsDecision";

    public final static int lambda=50; //任务到达频率


    //终端设备数量
    public final static int TERMINLNUM=50;

    //边缘节点数量
    public final static int EDGENODENUM=10;


    //每个终端发出的请求数量
    public final static int REQUESTNUM=500;

    //终端设备处理能力
    public final static float TERMINALFREQUENCE=500; //单位时间内能处理100kb字节

    //边缘节点处理能力 是终端处理能力的500倍
    public final static float EDGENODEFREQUENCE=50000; //单位时间能处理40960kb（40M）字节

    //边缘节点等待队列(缓存)的大小 是移动终端的100倍
    public final static int EDGNODEQUEUECAPACITY=500*1024; //500MB

    //终端设备等待队列(缓存)的大小
    public final static int TERMINALQUEUECAPACITY=5*1024; //5MB

    //传输速率
    public final static int BRANDWIDTH=50;

    //在终端消耗计算资源的开支为每单位时间消耗10开支
    public final static int CostPRInTerminal=10;
    //在边缘节点消耗计算资源的开支为终端的100倍
    public final static int CostPRInEdgeNode=CostPRInTerminal*100;

    public static String resultFilePath="F:\\A_LaboratoryData\\MyPaper\\边端调度\\";


}
