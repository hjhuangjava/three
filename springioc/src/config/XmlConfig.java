package config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * ��ȡxml�����ļ�����
 * @author �ܾ�
 */
public class XmlConfig {

    /**
     * ��ȡ�����ļ�
     * @param path �����ļ�·��
     * @return
     */
    public static Map<String, Bean> getConfig(String path){

        Map<String, Bean> configMap = new HashMap<String, Bean>();
        //ʹ��dom4j��xpath��ȡxml�ļ�
        Document doc = null;
        SAXReader reader = new SAXReader();
        InputStream in = XmlConfig.class.getResourceAsStream(path);
        try {
            doc = reader.read(in);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("��������xml�����ļ�·���Ƿ���ȷ��");
        }
        //����xpath��ȡ�����е�bean
        String xpath = "//bean";
        //��bean���б���
        List<Element> list = doc.selectNodes(xpath);
        if(list!=null){
            for (Element beanEle : list) {
                Bean bean = new Bean();
                //bean�ڵ��id
                String id = beanEle.attributeValue("id");
                //bean�ڵ��class����
                String className = beanEle.attributeValue("class");
                //��װ��bean������
                bean.setId(id);
                bean.setClassName(className);

                //��ȡbean�ڵ������е�property�ڵ�
                List<Element> proList = beanEle.elements("property");
                if(proList != null){
                    for (Element proEle : proList) {
                        Property prop = new Property();
                        String propName = proEle.attributeValue("name");
                        String propValue = proEle.attributeValue("value");
                        String propRef = proEle.attributeValue("ref");
                        //��װ��property������
                        prop.setName(propName);
                        prop.setValue(propValue);
                        prop.setRef(propRef);

                        bean.getProperties().add(prop);
                    }
                }
                //id�ǲ�Ӧ�ظ���
                if(configMap.containsKey(id)){
                    throw new RuntimeException("bean�ڵ�ID�ظ���" + id);
                }
                //��bean��װ��map��
                configMap.put(id, bean);
            }
        }
        return configMap;
    }
}