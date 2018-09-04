package core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import config.Bean;
import config.Property;
import config.XmlConfig;
import utils.BeanUtil;

public class ClassPathXmlApplicationContext implements BeanFactory{

    //����һ��IOC����
    private Map<String, Object> ioc;

    private Map<String, Bean> config;

    /**
     * ���캯��
     * 1. ��ʼ��IOC����
     * 2. ���������ļ�������bean�������IOC����
     * @param path
     */
    public ClassPathXmlApplicationContext(String path){
        //��ʼ��IOC����
        ioc = new HashMap<String, Object>();
        //��ȡ�����ļ�
        config = XmlConfig.getConfig(path);
        if(config!=null){
            for(Entry<String, Bean> entry : config.entrySet()){
                String beanId = entry.getKey();
                Bean bean = entry.getValue();

                //����bean������Ӧ�Ķ���
                Object object = createBean(bean);
                ioc.put(beanId, object);
            }
        }
    }
    /**
     * ����bean���ɶ���ʵ��
     * @param bean
     * @return
     */
    private Object createBean(Bean bean) {
        String beanId = bean.getId();
        String className = bean.getClassName();

        Class c = null;
        Object object = null;

        try {
            //����bean��calss�������ɶ���
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("�����õ�class���Բ��Ϸ���"+className);
        }

        try {
            //�÷������õ�������޲ι��췽��
            object = c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("����ȱ��һ���޲ι��췽����"+className);
        } 
        //��bean�����Է�װ��������
        if(bean.getProperties() != null){
            for(Property p : bean.getProperties()){
                //���һ�������ļ���ʹ�õ���value����ע��
                if(p.getValue() != null){
                    //��ȡ���Զ�Ӧ��setter����
                    Method getMethod = BeanUtil.getSetterMethod(object,p.getName());
                    try {
                        //����set����ע��
                        getMethod.invoke(object, p.getValue());
                    } catch (Exception e) {
                        throw new RuntimeException("�������Ʋ��Ϸ�����û����Ӧ��getter������"+p.getName());
                    } 
                }
                //������������ļ���ʹ�õ���ref����ע��
                if(p.getRef() != null){
                    //��ȡ���Զ�Ӧ��setter����
                    Method getMethod = BeanUtil.getSetterMethod(object,p.getName());
                    //���������ҵ������Ķ���
                    Object obj = ioc.get(p.getRef());
                    if(obj == null){
                        throw new RuntimeException("û���ҵ������Ķ���"+p.getRef());
                    }else{
                        //����set����ע��
                        try {
                            getMethod.invoke(object, obj);
                        } catch (Exception e) {
                            throw new RuntimeException("�������Ʋ��Ϸ�����û����Ӧ��getter������"+p.getName());
                        }
                    }
                }
            }
        }
        return object;
    }

    @Override
    public Object getBean(String beanName) {
        return ioc.get(beanName);
    }

}