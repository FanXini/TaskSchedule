package cluster_algorithm;

import entity.Node;
import entity.Request;

import java.util.*;

public class K_Means implements ClusterAlgorithm {

    private int maxIterations=10;

    private class RequestComparator implements Comparator<Request> {
        @Override
        public int compare(Request o1, Request o2) {
            return Double.valueOf(Math.sqrt(Math.pow(o1.getNormalizedDelaySensitivity(),2)+Math.pow(o1.getNormalizedDate(),2))).compareTo(Double.valueOf(Math.sqrt(Math.pow(o2.getNormalizedDelaySensitivity(),2)+Math.pow(o2.getNormalizedDate(),2))));
        }
    }

    private List<Request> requestList;


    public K_Means(List<Request>requestList){
        this.requestList=requestList;
        normalized(); //归一化
    }
    @Override
    public Map<Request,List<Request>> clustering(int k) {
        Map<Request,List<Request>> clusters=new TreeMap<>(new RequestComparator());
        List<Integer> indexList=new ArrayList<>();
        //创建几个随机的簇中心点
        for(int i=0;i<k;i++){
            int index=getUnitRandomNumber(indexList);
            //System.out.println(index);
            clusters.put(requestList.get(index),new ArrayList<>());
        }
        int count=0;
        while (true){
            for (Request request:requestList){
                double minSimilarity=Double.MAX_VALUE; //相似度
                Request selectedCluster=null;
                //计算相似度，找到最小相似度的簇
                for(Request clusterCenter:clusters.keySet()){
                    double similarity=Math.sqrt(Math.pow(request.getNormalizedDate()-clusterCenter.getNormalizedDate(),2)+Math.pow(request.getNormalizedDelaySensitivity()-clusterCenter.getNormalizedDelaySensitivity(),2));
                    if(similarity<minSimilarity){
                        minSimilarity=similarity;
                        selectedCluster=clusterCenter;
                    }
                }
                clusters.get(selectedCluster).add(request);
            }
            if(++count>maxIterations){
                for(Map.Entry<Request,List<Request>> keyset:clusters.entrySet()){
                    keyset.getValue().sort(new RequestComparator());
                }
                return clusters;
            }
            //重新计算簇
            Map<Request,List<Request>> newClusters=new  TreeMap<>(new RequestComparator());
            for(Map.Entry<Request,List<Request>> keyset:clusters.entrySet()){
                List<Request> requestInCluster=keyset.getValue();
                float totalSensitivity=0;
                float totalData=0;
                for(Request request:requestInCluster){
                    totalData+=request.getNormalizedDate();
                    totalSensitivity+=request.getNormalizedDelaySensitivity();
                }
                //求得平均值,作为新的簇心
                Request newClusterCenter=new Request(totalData/requestInCluster.size(),totalSensitivity/requestInCluster.size());
                //System.out.println("新的簇心"+count+"  "+newClusterCenter.toString());
                newClusters.put(newClusterCenter,new ArrayList<Request>());
            }
            clusters=newClusters;
        }
    }

    private int getUnitRandomNumber(List<Integer> indexList){
        int index;
        restart:
        while (true){
            index=(int)(Math.random()*requestList.size());
            for(Integer existIndex:indexList){
                if(existIndex==index){
                    continue restart;
                }
            }
            indexList.add(index);
            return index;
        }
    }


    //归一化
    private void normalized(){
        float totalData=0;
        float totalDelaySensitivity=0;
        for(Request request:requestList){
            totalData+=request.getData();
            request.setDelaySensitivity((int)request.getData()/100);
            totalDelaySensitivity+=request.getDelaySensitivity();
        }
        for(Request request:requestList){
            request.setNormalizedDelaySensitivity(request.getDelaySensitivity()/totalDelaySensitivity);
            request.setNormalizedDate(request.getData()/totalData);
        }
    }
}
