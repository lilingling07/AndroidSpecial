package special;

import java.util.List;
import java.util.Map;

public class Result {
    private List<Double> cpuList;
    private List<Double> memoryList;
    private Map<String, Double> netCardTrafficMap;

    public void addMemory(double memory) {
        this.memoryList.add(memory);
    }

    public void addNetCardTraffic(String trafficName, double traffic) {
        this.netCardTrafficMap.put(trafficName, traffic);
    }

    public void addCpu(double cpu) {
        this.cpuList.add(cpu);
    }
}
