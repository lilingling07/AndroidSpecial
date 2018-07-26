package special.realize;

import special.Common;
import special.Recorder;
import special.Result;

import java.util.ArrayList;
import java.util.Iterator;

public class Memory extends Recorder {
    public  static void main(String[] args){
        Memory memory=new Memory("com.dianping.v1","B2T5T16C06018639",null);
        Double memorynumber=memory.getMemoryByPackageName();
        System.out.println("memory:"+memorynumber);
    }
    public Memory(String pkg, String deviceNum, Result result) {
        super(pkg, deviceNum, result);
    }


    public Double getMemoryByPackageName() {
        Double memoryM = 0.0D;
        String command = String.format("adb -s %s shell dumpsys meminfo %s", this.deviceNum, this.pkg);
        ArrayList<String> result = Common.execCommand(command);
        Iterator var4 = result.iterator();

        while(var4.hasNext()) {
            String line = (String)var4.next();
            if (line.contains("TOTAL")) {
                memoryM = Double.parseDouble(line.split("\\s+")[2]);
                break;
            }
        }

        return memoryM / 1024.0D;
    }

    public void run() {
        while(this.isRun) {
            double memory = this.getMemoryByPackageName();
            this.result.addMemory(memory);
            this.sleep(1000L);
        }

    }
}
