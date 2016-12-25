package futuredata;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager {

    public String getOutputPath() {

        String filePath = "";
        File directory = new File("");

        try {

            filePath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.format(new Date());

        File folder = new File(filePath + "\\output\\" + df.format(new Date()));

        folder.mkdirs();

        return folder.getPath();
    }

    public String getInputPath() {

        String filePath = "";
        File directory = new File("");

        try {

            filePath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        filePath = filePath + "\\input\\config.xml";

        return filePath;
    }
}
