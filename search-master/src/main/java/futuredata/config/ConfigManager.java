package futuredata.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class ConfigManager {

    @SuppressWarnings("unchecked")
    public static <T> T converyXmlToJavaBean(String filePath, Class<T> c) {

        T t = null;
        try {

            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new File(filePath));
        } catch (Exception e) {

            e.printStackTrace();
        }
        return t;
    }
}
