package entity;

import lombok.Data;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Data
public class FogBlockQueue<T> {
    private String serial;
    private Lock lock;
    private Float capacity;
    private Float remainCapcity;
    private Stack<T> items;
    private final Condition notFull;
    private final Condition notEmpty;

    public FogBlockQueue(String serial,float capacity,boolean fair){
        this.serial=serial;
        this.capacity=capacity;
        this.remainCapcity=capacity;
        items=new Stack<T>();
        lock=new ReentrantLock(fair);
        notEmpty=lock.newCondition();
        notFull=lock.newCondition();
    }

    public T take() throws InterruptedException{
        final Lock lock=this.lock;
        lock.lockInterruptibly();
        try{
            while(getSize()==0){
                //System.out.println(serial+":暂无任务请求");
                notEmpty.await();
            }
            T t= items.pop();
            Request request=(Request) t;
            remainCapcity+=request.getData();
            System.out.println(serial+":remain="+remainCapcity);
            notFull.signalAll();
            return t;
        }finally {
            lock.unlock();
        }
    }

    public void put(T t) throws InterruptedException{
        checkNotNull(t);
        final Lock lock=this.lock;
        lock.lockInterruptibly();
        try{
            while (remainCapcity<=((Request)t).getData()){
                //System.out.println(serial+":队列空间已满");
                notFull.await();
            }
            items.push(t);
            remainCapcity-=((Request) t).getData();
            notEmpty.signalAll();
        }finally {
            lock.unlock();
        }
    }

    private int getSize(){
        return items.size();
    }

    private void checkNotNull(Object o){
        if(o==null){
            throw new NullPointerException("request 为空");
        }
    }

}
