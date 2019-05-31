package entity;

import Util.Help;
import Util.IOUtils;
import Util.Poission;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.sql.Time;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Data
public class Terminal implements Serializable{

     private static final long serialVersionUID = 5854592621L;

    private int id;

    private float frequent;

    private FogBlockQueue<Request> queue;

    private volatile float currentRequestNeedTime = 0;

    public Terminal() {
    }
    public Terminal(int id, float frequent) {
        this.id = id;
        this.frequent = frequent;
        this.queue = new FogBlockQueue<Request>("terminal" + id, Global.TERMINALQUEUECAPACITY, true);
    }

    public void run() {
        ThreadPoolExecutor poolExecutor = Help.getThreadPool();
        poolExecutor.execute(new CreateRequest());
        if(!("allToEdgeNodeDecision").equals(Global.decisionName)){
            poolExecutor.execute(new ProcessRequest());
        }
    }

    class CreateRequest implements Runnable {
        @Override
        public void run() {
            int i = 1;
            try {
                while (i <= Global.REQUESTNUM) { //
                    float data = (float) (400 * Math.random()) + 100; //数据量 100-500
                    Request request = new Request(i,Terminal.this, data);
                    Help.getSDN().getRequestsQueue().put(request);
                    //IOUtils.println("terminal" + id, "生成任务" + i + "，任务量:" + data);
                    i++;
                    double nextTime = (Poission.nextTime(Global.lambda) * 1000);
                    double startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < nextTime) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ProcessRequest implements Runnable {

        private float spinForTimeoutThread = 1000;

        @Override
        public void run() {
            while (true) {
                try {
                    if(Help.scheduleCompleteFlag&&Math.abs(queue.getRemainCapcity()-queue.getCapacity())<1){
                        System.out.println("terminal"+id+"执行完成");
                        Help.getCountDownLatch().countDown();
                        break;
                    }
                    Request request = queue.poll(100,TimeUnit.MILLISECONDS);
                    if(request!=null){
                        IOUtils.println("terminal" + id,"处理第"+request.getId()+"个任务");
                        currentRequestNeedTime = (request.getData() / frequent) * 1000; //转换为millis

                        long lastTime = System.currentTimeMillis();
                        for (; ; ) { //自旋
                            if (currentRequestNeedTime <= 0) {
                                break;
                            }
                            if (currentRequestNeedTime > spinForTimeoutThread) { //没有超过自旋阈值
                                TimeUnit.MILLISECONDS.sleep((int) currentRequestNeedTime);
                            }
                            long now = System.currentTimeMillis();
                            currentRequestNeedTime -= (now-lastTime);
                            lastTime = now;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public float getCurrentRequestNeedTime(){
        return currentRequestNeedTime/1000;
    }

}
