package Util;

import java.util.Random;

public class Poission {
    //生成泊松随机数，其中lambda是到达率
    private static float lambda=0.1f;
    float [] arrival_time=new float[100];
    public int nextPoisson() {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        Random r = new Random();

        do {
            k++;
            p *= r.nextDouble();
        } while (p > L);
        return k - 1;
    }
    //生成事件的到达时间
    public void generateArrival(){
        arrival_time[0]=nextPoisson();
        for(int i=1;i<arrival_time.length;i++){
            arrival_time[i]=arrival_time[i-1]+nextPoisson();
        }
    }

    public static double nextTime(float rate){
        return  - Math.log(1.0-Math.random())/rate;
    }


    public static void main(String agrs[]){
        Poission poission=new Poission();
        poission.generateArrival();
//        for(int i=0;i<poission.arrival_time.length;i++){
//           System.out.println(poission.arrival_time[i]);
//        }
        double data=0;
        for(int i=0;i<10;i++){
            double next=nextTime(10);
            data+=next;
            System.out.println(next);
        }
        System.out.println("time="+data);
    }

}
