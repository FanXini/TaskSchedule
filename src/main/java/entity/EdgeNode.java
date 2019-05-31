package entity;


import Runner.Runner;
import Util.Help;
import Util.IOUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Data
public class EdgeNode implements Runnable,Serializable{

    private static final long serialVersionUID = 585451921688207L;
    int id;
    @Autowired
    private FogBlockQueue<Request> queue;
    private float totalProcessTime;
    private float frequent;
    private volatile float currentRequestNeedTime=0;
    public EdgeNode(int id,float frequent){
        this.id=id;
        this.queue=new FogBlockQueue<>("egeNode"+id,Global.EDGNODEQUEUECAPACITY,true);
        this.frequent=frequent;
    }

    @Override
    public void run() {
        while (true){
            try{
                if(Help.scheduleCompleteFlag&&Math.abs(queue.getRemainCapcity()-queue.getCapacity())<1){
                    System.out.println("edgeNode"+id+"执行完成");
                    Help.getCountDownLatch().countDown();
                    break;
                }
                Request request=queue.poll(100,TimeUnit.MILLISECONDS);
                if(request!=null){
                    //IOUtils.println("edgeNode"+id,"执行终端：+"+request.getTerminal().getId()+"+发出的请求，数据量："+request.getData()+"剩余的缓存量："+queue.getRemainCapcity());
                    currentRequestNeedTime=(request.getData()/frequent)*1000;
                    totalProcessTime+=currentRequestNeedTime;
                    double lastTime=System.currentTimeMillis();
                    for (;;){
                        if(currentRequestNeedTime<=0){
                            break;
                        }
                        long now=System.currentTimeMillis();
                        currentRequestNeedTime-=now-lastTime;
                        lastTime=now;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public float getCurrentRequestNeedTime(){
        return currentRequestNeedTime/1000;
    }

}
