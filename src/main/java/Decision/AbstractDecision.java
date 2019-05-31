package Decision;

import Util.Help;
import Util.IOUtils;
import entity.Global;

import java.math.BigDecimal;

public abstract class AbstractDecision implements Decision {
    protected int taskCount=0;

    protected boolean isStoppingConditionReached(){
        if(taskCount%100==0){
            System.out.println("调度完成"+taskCount+"个任务");
        }
        if(taskCount>= Global.TERMINLNUM*Global.REQUESTNUM){
            //决策完成
            Help.scheduleCompleteFlag=true;
            Float num=Help.toltalCost;
            String str=new BigDecimal(num.toString()).toString();
            IOUtils.println("SDN","totalCost"+ str);
            IOUtils.println("SDN","总任务数量："+taskCount+"\n拒绝量："+Help.refuseCount+"\n拒绝率："+(float)Help.refuseCount/taskCount);
            return true;
        }else{
            return false;
        }
    }
}
