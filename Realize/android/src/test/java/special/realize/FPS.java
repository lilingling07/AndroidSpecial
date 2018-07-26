package special.realize;


import special.Common;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FPS {
    private String packageName;
    private String deviceNum;
    private static final String FPS_FILE_PATH = "/Users/lili/fps.txt";
    private Map<String, List<String>> fpsMap = new HashMap();

    public FPS(String pkg, String deviceNum) {
        this.packageName = pkg;
        this.deviceNum = deviceNum;
    }

    public long getFPS() {
        long total = 0L;
        String command = String.format("adb -s %s shell dumpsys gfxinfo %s >> fps.txt", this.deviceNum, this.packageName);
        ArrayList<String> result = Common.execCommand(command);
        return total;
    }

    public static void main(String[] args) {
        FPS fpsinfo = new FPS("com.dianping.v1", "B2T5T16C06018639");

        fpsinfo.generateFpsHtmlInfo();
        fpsinfo.calculateFps("fps_data.txt");
    }

    public void generateFpsHtmlInfo() {
        try {
            this.parseFpsFile("/Users/lili/fps.txt");
            this.writeFpsInfo();
        } catch (Exception var2) {
            System.out.println("fpsinfo prase error!");
            var2.printStackTrace();
        }

    }

    private void parseFpsFile(String path) {
        File file = new File(path);
        if (file != null && file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String lastLine = null;
                String activity = null;
                boolean isStart = false;

                while(true) {
                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        String[] array = line.trim().split("\\s+");
                        if (array != null && (array.length == 4 || array.length == 3) && array[0].equals("Draw") && array[array.length - 2].contains("Process") && array[array.length - 1].contains("Execute")) {
                            isStart = true;
                            String[] names = lastLine.split("/");
                            activity = names != null && names.length > 2 ? names[1] : lastLine;
                            lastLine = line;
                        } else if (!isStart) {
                            lastLine = line;
                            isStart = false;
                            activity = "";
                        } else {
                            try {
                                if (array != null && (array.length == 4 || array.length == 3)) {
                                    for(int i = 0; i < array.length; ++i) {
                                        if (this.fpsMap.size() != 0 && this.fpsMap.containsKey(activity)) {
                                            ((List)this.fpsMap.get(activity)).add(line.trim());
                                        } else {
                                            List<String> memList = new ArrayList();
                                            memList.add(line.trim());
                                            this.fpsMap.put(activity, memList);
                                        }
                                    }
                                } else {
                                    lastLine = line;
                                    isStart = false;
                                    activity = "";
                                }
                            } catch (Exception var11) {
                                var11.printStackTrace();
                                lastLine = line;
                                isStart = false;
                                activity = "";
                            }
                        }
                    }

                    return;
                }
            } catch (FileNotFoundException var12) {
                var12.printStackTrace();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }
    }

    private void writeFpsInfo() {
        File file = new File("fps_data.txt");
        if (file.exists()) {
            file.delete();
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            Iterator it = this.fpsMap.keySet().iterator();

            while(true) {
                List values;
                do {
                    do {
                        if (!it.hasNext()) {
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            System.out.println("fps info create");
                            return;
                        }

                        String activity = (String)it.next();
                        values = (List)this.fpsMap.get(activity);
                    } while(null == values);
                } while(values.size() == 0);

                Iterator var6 = values.iterator();

                while(var6.hasNext()) {
                    String value = (String)var6.next();
                    bufferedWriter.write(value + "\n");
                }
            }
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }

    private void calculateFps(String filePath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            new ArrayList();
            List<Double> timeList = new ArrayList();
            int jankyCount = 0;
            int frameNumber = 0;
            Boolean jankyFlag = false;
            System.out.println(bufferedReader.readLine());

            String line;
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(bufferedReader.readLine());
                ++frameNumber;
                String[] array = line.trim().split("\\s+");
                System.out.println("array[0]:" + array[0] + " array[1]:" + array[1] + " array[2]" + array[2]);
                if (array[0] != null) {
                    double timeOfEachFrame = Double.valueOf(array[0]) + Double.valueOf(array[1]) + Double.valueOf(array[2]);
                    timeList.add(timeOfEachFrame);
                    if (timeOfEachFrame > 16.67D) {
                        ++jankyCount;
                        jankyFlag = true;
                        System.out.println(line + " " + timeOfEachFrame + " janky!");
                    } else {
                        System.out.println(line + " " + timeOfEachFrame);
                    }
                }
            }

            System.out.println("帧数：" + frameNumber);
            System.out.println("丢帧数:" + jankyCount);
            System.out.println("丢帧率：" + (double)jankyCount / (double)frameNumber * 100.0D);
        } catch (FileNotFoundException var14) {
            var14.printStackTrace();
        } catch (IOException var15) {
            var15.printStackTrace();
        }

    }
}


