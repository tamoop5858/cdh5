package futuredata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import futuredata.config.HttpConfig;
import futuredata.search.EdinetSearcher;
import futuredata.search.NegaSearcher;

public class Crawler {

    private static volatile Crawler instance = null;

    public static Crawler getInstance() {

        if (instance == null) {

            synchronized (Crawler.class) {

                if (instance == null) {

                    instance = new Crawler();
                }
            }
        }
        return instance;
    }

    public String read(HttpConfig httpConfig) {

        String returnMsg = "";

        EdinetSearcher edinetSearcher = new EdinetSearcher();

        NegaSearcher negaSearcher = new NegaSearcher();

        if ("get".equals(httpConfig.getHttpMethod())) {

            returnMsg = edinetSearcher.search(httpConfig);

        } else if ("post".equals(httpConfig.getHttpMethod())) {

            returnMsg = negaSearcher.search(httpConfig);
        }

        return returnMsg;
    }

    public void write(String reponse, String filePath) {

        try {

            File file = new File(filePath);

            @SuppressWarnings("resource")
            PrintStream ps = new PrintStream(new FileOutputStream(file));

            ps.append(reponse);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }
}
