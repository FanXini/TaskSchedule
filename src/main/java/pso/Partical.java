package pso;

import entity.Global;
import lombok.Data;

import java.util.Random;


/**
 * 粒子
 */
@Data
public class Partical {
    private int dimension;
    private int solution[];
    private int bestSolution[];
    private double velocity[];//速度
    //适应度
    private float fitness;
    private float bestFitness;

    public Partical(int dimension) {
        this.dimension = dimension;
    }

    public void selectBestSolution() {
        if (fitness < bestFitness) {
            bestFitness = fitness;
            bestSolution = solution;
        }
    }

    public void init() {
        solution = new int[dimension];

        //速度初始值全部为0;
        velocity = new double[dimension];

        bestSolution = new int[dimension];

        bestFitness = Float.MAX_VALUE;

        //随机生成初始位置
        for (int index = 0; index < dimension; index++) {
            //生成随机解 -1到Global.EDGENODENUM-1
            solution[index] = (int) (Math.random() * (Global.EDGENODENUM + 1)) - 1;
        }
        //随机生成速度:区间[-3,-3]
//        for(int index=0;index<dimension;index++){
//            velocity[index]=nextDouble(-3,3);
//        }


    }



}
