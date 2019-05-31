import entity.Global;
import pso.Partical;

import java.util.ArrayList;
import java.util.List;

public class WhileTest {

    public static void main(String arge[]){
        long lastTime=System.currentTimeMillis();
        int i=0;
//        while (i++<100){
//            long now=System.currentTimeMillis();
//            //System.out.println(now-lastTime);
//            lastTime=now;
//            int center1=(int)(Math.random()*(Global.EDGENODENUM+1))-1;
//            Partical partical=new Partical(5);
//            double test=partical.nextDouble(-3,3);
//            System.out.println(test+" ");
//        }
        int a[]=new int[10];
        int b=(int)Math.round(1.5);
        System.out.println(b);

//        List<Integer> list=new ArrayList<>();
//        list.add(2);
//        test(list);
//        for(Integer data:list){
//            System.out.println(data);
//        }

    }

    public static void test(List<Integer> list){
        list.add(1);
    }
}
