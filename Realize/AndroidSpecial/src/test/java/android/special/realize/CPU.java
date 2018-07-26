package android.special.realize;

import android.special.Common;
import android.special.Formater;
import android.special.Recorder;
import android.special.Result;

import java.util.ArrayList;
import java.util.Iterator;

public class CPU extends Recorder {
    public CPU(String pkg, String deviceNum, Result result) {
        super(pkg, deviceNum, result);
    }

    public long getTotalCpu() {
        long total = 0L;
        String command = String.format("adb -s %s shell cat /proc/stat", this.deviceNum);
        ArrayList<String> result = Common.execCommand(command);
        String line = "";

        for(int i = 0; i < result.size(); ++i) {
            line = (String)result.get(i);
            if (line.startsWith("cpu")) {
                String[] tokens = line.split("\\s+");
                total += Long.parseLong(tokens[1]) + Long.parseLong(tokens[2]) + Long.parseLong(tokens[3]) + Long.parseLong(tokens[4]) + Long.parseLong(tokens[6]) + Long.parseLong(tokens[5]) + Long.parseLong(tokens[7]);
                break;
            }
        }

        return total;
    }

    public long getCpuByPid(ArrayList<Integer> pidArr) {
        long processCpu = 0L;
        Iterator var4 = pidArr.iterator();

        while(var4.hasNext()) {
            int pid = (Integer)var4.next();
            String command = String.format("adb -s %s shell cat /proc/%d/stat", this.deviceNum, pid);
            ArrayList<String> result = Common.execCommand(command);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setLength(0);
            Iterator var9 = result.iterator();

            while(var9.hasNext()) {
                String line = (String)var9.next();
                stringBuffer.append(line + "\n");
            }

            String[] tokens = stringBuffer.toString().split(" ");

            try {
                processCpu += Long.parseLong(tokens[13]) + Long.parseLong(tokens[14]) + Long.parseLong(tokens[15] + Long.parseLong(tokens[16]));
            } catch (ArrayIndexOutOfBoundsException var11) {
                return 0L;
            }
        }

        return processCpu;
    }

    public double getProcessCpuRatioByPackageName() {
        int time = 200;
        int n = 1000 / time;
        double sum = 0.0D;
        ArrayList<Integer> pidArr = Common.getPidsByPackageName(this.pkg, this.deviceNum);
        long tCpu1 = this.getTotalCpu();
        long pCpu1 = this.getCpuByPid(pidArr);

        for(int i = 0; i < n; ++i) {
            this.sleep((long)time);
            long tCpu2 = this.getTotalCpu();
            long pCpu2 = this.getCpuByPid(pidArr);
            sum += Math.abs(100.0D * (double)(pCpu2 - pCpu1) / (double)(tCpu2 - tCpu1));
            tCpu1 = tCpu2;
            pCpu1 = pCpu2;
        }

        double ratio = sum / (double)n;
        return Formater.format(ratio);
    }

    public void run() {
        while(this.isRun) {
            double ratio = this.getProcessCpuRatioByPackageName();
            ratio = Formater.format(ratio);
            if (ratio < 100.0D) {
                this.result.addCpu(ratio);
            }
        }

    }
}
