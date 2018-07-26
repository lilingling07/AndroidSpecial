package special.realize;



import special.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NetCardTraffic {
    public  static void main(String[] args){
        NetCardTraffic netCardTraffic=new NetCardTraffic("B2T5T16C06018639");
        Map<String, Double> netmap=new HashMap<String, Double>();
        netmap=netCardTraffic.getTrafficKb("com.dianping.v1");
        Iterator<Map.Entry<String,Double>> it=netmap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Double> entry=it.next();

            System.out.println(entry.getKey()+":"+entry.getValue());
        }



    }
    private String deviceNum;

    public NetCardTraffic(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Map<String, Double> getTrafficKb(String pkg) {
        Map<String, Double> trafficMap = new HashMap();
        int uid = Common.getUidByPackageName(pkg, this.deviceNum);

        try {
            String command = String.format("adb -s %s shell cat /proc/net/xt_qtaguid/stats", this.deviceNum);
            ArrayList<String> result = Common.execCommand(command);
            long tx = 0L;
            long rx = 0L;
            Iterator var10 = result.iterator();

            while(var10.hasNext()) {
                String line = (String)var10.next();
                String[] args = line.split("\\s+");
                if (args[3].endsWith(String.valueOf(uid))) {
                    rx += (long)Integer.parseInt(args[5]);
                    tx += (long)Integer.parseInt(args[7]);
                }
            }

            trafficMap.put("tx", (double)tx / 1024.0D);
            trafficMap.put("rx", (double)rx / 1024.0D);
        } catch (Exception var13) {
            var13.printStackTrace();
            trafficMap.put("tx", 0.0D);
            trafficMap.put("rx", 0.0D);
        }

        return trafficMap;
    }
}

