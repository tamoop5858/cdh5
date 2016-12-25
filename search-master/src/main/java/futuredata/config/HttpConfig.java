package futuredata.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "httpUrl", "httpMethod", "paraList" })
public class HttpConfig {

    @XmlElement(name = "http_url")
    private String httpUrl;

    @XmlElement(name = "http_method")
    private String httpMethod;

    @XmlElementWrapper(name = "http_paras")
    @XmlElement(name = "http_para")
    private List<HttpPara> paraList;

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<HttpPara> getParaList() {
        return paraList;
    }

    public void setParaList(List<HttpPara> paraList) {
        this.paraList = paraList;
    }
}