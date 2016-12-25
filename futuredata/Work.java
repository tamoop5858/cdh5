package futuredata;

import java.util.concurrent.atomic.AtomicInteger;

import futuredata.config.Config;
import futuredata.config.ConfigManager;
import futuredata.config.HttpConfig;

public class Work {

    public static void main(String args[]) {

        Work w = new Work();
        w.run();
    }

    public void run() {

        FileManager fileManager = new FileManager();
        String inputFilePath = fileManager.getInputPath();
        String outputDirectoryPath = fileManager.getOutputPath();

        Config config = ConfigManager.converyXmlToJavaBean(inputFilePath, Config.class);

        Crawler crawler = Crawler.getInstance();

        AtomicInteger atomicInteger = new AtomicInteger();

        for (HttpConfig httpConfig : config.getHttpConfigList()) {

            String reponse = crawler.read(httpConfig);

            if (!"0".equals(reponse)) {
                // Write file to Project root directory
                crawler.write(reponse, outputDirectoryPath + "\\" + atomicInteger.addAndGet(1) + ".txt");
            }
        }
    }
}
