package futuredata.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "http_configs")
@XmlType(propOrder = { "httpConfigList" })
public class Config {

    @XmlElement(name = "http_config")
    private List<HttpConfig> httpConfigList;

    public List<HttpConfig> getHttpConfigList() {
        return httpConfigList;
    }

    public void setHttpConfigList(List<HttpConfig> httpConfigList) {
        this.httpConfigList = httpConfigList;
    }
}