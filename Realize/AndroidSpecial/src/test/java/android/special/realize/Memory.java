package android.special.realize;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.special.Common;
import android.special.Recorder;
import android.special.Result;

import java.util.ArrayList;
import java.util.Iterator;

public class Memory extends Recorder {
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

