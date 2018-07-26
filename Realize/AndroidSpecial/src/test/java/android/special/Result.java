package android.special;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Result {
    private List<Double> cpuList;
    private List<Double> memoryList;
    private Map<String, Double> netCardTrafficMap;
    private double loadTime;
    private double startLoadTime;
    private double endLoadTime;
    private int index;
    private String page;
    private boolean isMark;

    public Result(String page, int index) {
        this(page, index, false);
    }

    public Result(String page, int index, boolean isMark) {
        this.page = page;
        this.index = index;
        this.isMark = isMark;
        this.loadTime = 0.0D;
        this.cpuList = new LinkedList();
        this.memoryList = new LinkedList();
        this.netCardTrafficMap = new HashMap();
    }

    public void setMark(boolean mark) {
        this.isMark = mark;
    }

    public void addMemory(double memory) {
        this.memoryList.add(memory);
    }

    public void addNetCardTraffic(String trafficName, double traffic) {
        this.netCardTrafficMap.put(trafficName, traffic);
    }

    public void addCpu(double cpu) {
        this.cpuList.add(cpu);
    }

    public void setLoadTime(double loadTime) {
        this.loadTime += loadTime;
    }

    public void setStartLoadTime(double start) {
        this.startLoadTime = start;
    }

    public void setEndLoadTime(double end) {
        this.endLoadTime = end;
    }

    public int getIndex() {
        return this.index;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject item = new JSONObject();
        item.put("loadTime", Formater.format(this.loadTime));
        item.put("index", this.index);
        item.put("page", this.page);
        item.put("mark", this.isMark);
        JSONArray memoryArray = new JSONArray();
        double totalMemory = 0.0D;

        double val;
        for(Iterator var5 = this.memoryList.iterator(); var5.hasNext(); totalMemory += val) {
            val = (Double)var5.next();
            memoryArray.put(val);
        }

        double avgMemory = totalMemory / (double)this.memoryList.size();
        item.put("memory", avgMemory);
        JSONObject netCard = new JSONObject(this.netCardTrafficMap);
        item.put("netCardTraffic", netCard.toString());
        JSONArray cpuArray = new JSONArray();
        double totalCPU = 0.0D;

        //double val;
        for(Iterator var11 = this.cpuList.iterator(); var11.hasNext(); totalCPU += val) {
            val = (Double)var11.next();
            cpuArray.put(val);
        }

        double avgCPU = totalCPU / (double)this.cpuList.size();
        item.put("cpu", avgCPU);
        return item;
    }
}

