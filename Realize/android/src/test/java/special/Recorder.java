package special;

public abstract class Recorder implements Runnable {
    protected static final int POLL_TIME = 1000;
    protected String pkg;
    protected boolean isRun = true;
    protected String deviceNum;
    protected Result result;

    public Recorder(String pkg, String deviceNum, Result result) {
        this.pkg = pkg;
        this.deviceNum = deviceNum;
        this.result = result;
    }

    public void stop() {
        this.isRun = false;
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException var4) {
            ;
        }

    }
}

