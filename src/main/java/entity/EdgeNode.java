package entity;


import Runner.Runner;
import Util.IOUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Data
public class EdgeNode implements Runnable{

    int id;
    @Autowired
    private FogBlockQueue<Request> queue;
    private float frequent;
    private float currentRequestNeedTime=0;
    public EdgeNode(int id,float frequent){
        this.id=id;
        this.queue=new FogBlockQueue<>("egeNode"+id,Global.EDGNODEQUEUECAPACITY,true);
        this.frequent=frequent;
    }

    @Override
    public void run() {
        while (true){
            currentRequestNeedTime=0;
            try{
                Request request=queue.take();
                IOUtils.println("edgeNode"+id,"执行终端：+"+request.getTerminal().getId()+"+发出的请求，数据量："+request.getData()+"剩余的缓存量："+queue.getRemainCapcity());
                currentRequestNeedTime=(request.getData()/frequent)*1000;
                double startTime=System.currentTimeMillis();
                while (System.currentTimeMillis()-startTime<currentRequestNeedTime){

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
