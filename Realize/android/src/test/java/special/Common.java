package special;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Common {
    private static Runtime runtime = Runtime.getRuntime();

    public Common() {
    }

    public static ArrayList<String> execCommand(String command) {
        ArrayList result = new ArrayList();

        try {
            Process p = runtime.exec(command);
            InputStream output = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(output));
            String line = null;

            while((line = reader.readLine()) != null) {
                result.add(line);
            }

            p.waitFor();
            p.destroy();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return result;
    }

    public static ArrayList<Integer> getPidsByPackageName(String packageName, String deviceNum) {
        String command = String.format("adb -s %s shell ps | grep %s", deviceNum, packageName);
        ArrayList<String> result = execCommand(command);
        ArrayList<Integer> pidList = new ArrayList();

        for(int i = 0; i < result.size(); ++i) {
            pidList.add(Integer.parseInt(((String)result.get(i)).split("\\s+")[1]));
        }

        return pidList;
    }

    public static int getUidByPackageName(String packageName, String deviceNum) {
        String command = String.format("adb -s %s shell ps | grep %s", deviceNum, packageName);
        ArrayList<String> result = execCommand(command);
        int uid = 0;
        int pid = 0;
        int i = 0;
        if (i < result.size()) {
            pid = Integer.parseInt(((String)result.get(i)).split("\\s+")[1]);
        }

        command = String.format("adb -s %s shell cat /proc/%d/status", deviceNum, pid);
        result = execCommand(command);
        Iterator var8 = result.iterator();

        while(var8.hasNext()) {
            String line = (String)var8.next();
            if (line.startsWith("Uid:")) {
                uid = Integer.parseInt(line.split("\\s+")[1]);
                break;
            }
        }

        return uid;
    }

    public static boolean hasInstall(String deviceNum, String packageName) {
        String cmd = String.format("adb -s %s shell pm list package %s", deviceNum, packageName);
        List<String> result = execCommand(cmd);
        String match = String.format("package:%s", packageName);
        boolean hasInstall = false;
        Iterator var6 = result.iterator();

        while(var6.hasNext()) {
            String line = (String)var6.next();
            System.out.println(line);
            if (line.trim().equals(match)) {
                hasInstall = true;
                break;
            }
        }

        return hasInstall;
    }

    public static void stopApp(String deviceNum, String packageName) {
        try {
            execCommand(String.format("adb -s %s shell am force-stop %s", deviceNum, packageName));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void startApp(String deviceNum, String packageName, String launcher) {
        try {
            execCommand(String.format("adb -s %s shell am start %s/%s", deviceNum, packageName, launcher));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
