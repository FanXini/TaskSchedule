package entity;

public class Global {

    public final static int lambda=100; //1秒生成100个任务

    //终端设备数量
    public final static int TERMINLNUM=100;

    //边缘节点数量
    public final static int EDGENODENUM=10;


    //每个终端发出的请求数量
    public final static int REQUESTNUM=1000;

    //终端设备处理能力
    public final static float TERMINALFREQUENCE=100; //单位时间内能处理100kb字节

    //边缘节点处理能力 是终端处理能力的500倍
    public final static float EDGENODEFREQUENCE=20480; //单位时间能处理20480kb（20M）字节

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

    public static String resultFilePath="F:\\Literature\\第一个点\\";


}
