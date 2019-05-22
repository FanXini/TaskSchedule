import config.Config;
import entity.EdgeNode;
import entity.FogBlockQueue;
import entity.Request;
import entity.Terminal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String args[]){
        Terminal terminal=new Terminal(1,1);
        List<Terminal> list=new ArrayList<>();
        List<Terminal> list1=list;
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){

        }
        list.add(terminal);
        terminal.setId(3);
        System.out.println(list1.get(0).getId());
        /*ApplicationContext applicationContext=new AnnotationConfigApplicationContext(Config.class);
        FogBlockQueue<Request> blockQueue=applicationContext.getBean(FogBlockQueue.class);
        EdgeNode node=applicationContext.getBean(EdgeNode.class);
        System.out.println(node.toString());*/
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    Request request=new Request(200);
//                    try {
//                        blockQueue.put(request);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    try {
//                        Request request=blockQueue.take();
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }
}
