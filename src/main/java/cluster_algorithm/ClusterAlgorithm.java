package cluster_algorithm;

import entity.Request;

import java.util.List;
import java.util.Map;

public interface ClusterAlgorithm {

    public Map<Request,List<Request>> clustering(int k);

}
