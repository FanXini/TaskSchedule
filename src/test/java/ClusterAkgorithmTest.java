import Util.Help;
import Util.Poission;
import cluster_algorithm.ClusterAlgorithm;
import cluster_algorithm.K_Means;
import entity.Global;
import entity.Request;
import entity.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClusterAkgorithmTest {

    public static void main(String agres[]){
        int i=0;
        List<Request> requestList=new ArrayList<>();
        while (i++ < 500) { //
            float data = (float) (400 * Math.random()) + 100; //数据量 100-500
            Request request = new Request(i, null, data);
            requestList.add(request);
//            double nextTime = (Poission.nextTime(Global.lambda) * 1000);
//            double startTime = System.currentTimeMillis();
//            while (System.currentTimeMillis() - startTime < nextTime) {
//            }
        }
        long startTime=System.currentTimeMillis();
        ClusterAlgorithm clusterAlgorithm=new K_Means(requestList);
        Map<Request,List<Request>> map=clusterAlgorithm.clustering(10);
        System.out.println("time"+(System.currentTimeMillis()-startTime));
        for(Map.Entry<Request,List<Request>> entry:map.entrySet()){
            Request clusterCenter=entry.getKey();
            List<Request> requestInCluster=entry.getValue();
            System.out.println("簇心："+clusterCenter.getNormalizedDelaySensitivity()+" "+clusterCenter.getNormalizedDate());
            for(Request request:requestInCluster){
                System.out.println(request.getData()+" "+request.getDelaySensitivity());
            }
        }
    }
}
