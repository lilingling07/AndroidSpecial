package android.special;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Files {
    public Files() {
    }

    public static void delete(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    File[] var2 = files;
                    int var3 = files.length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                        File f = var2[var4];
                        delete(f);
                    }
                }
            }

            file.delete();
        }
    }

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = (new FileInputStream(source)).getChannel();
            outputChannel = (new FileOutputStream(dest)).getChannel();
            outputChannel.transferFrom(inputChannel, 0L, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }

    }
}

