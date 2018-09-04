package springioc;

import java.util.Map;
import java.util.Map.Entry;

import bean.Address;
import bean.User;
import config.Bean;
import config.XmlConfig;
import core.BeanFactory;
import core.ClassPathXmlApplicationContext;

public class Test {

    public static void main(String[] args) {
        testIOC();
        //testConfig();
    }
    /**
     * ≤‚ ‘IOC»›∆˜
     */
    private static void testIOC(){

        BeanFactory bf = new ClassPathXmlApplicationContext("/ApplicationContext.xml");

        User user = (User) bf.getBean("user");
        System.out.println(user);
        System.out.println("address hashcode:"+user.getAddress().hashCode());

        Address address = (Address) bf.getBean("address");
        System.out.println(address);
        System.out.println("address hashcode:"+address.hashCode());
    }
    /**
     * ≤‚ ‘∂¡»°≈‰÷√Œƒº˛
     */
    private static void testConfig(){
        Map<String,Bean> map = XmlConfig.getConfig("/ApplicationContext.xml");
        for (Entry<String, Bean> entry : map.entrySet()) {
            System.out.println(entry.getKey()+"==="+entry.getValue());
        }
    }

}