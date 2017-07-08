package name.lizhe.spingcloudsample.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class HelloController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;
    
    @Value("${message1}")  
    private String message1; 
    
    @Value("${message2}")  
    private String message2; 

    @RequestMapping(value = "/hello" ,method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "showErrorMessage")
    public String add(@RequestParam String message) throws Exception {
        ServiceInstance instance = client.getLocalServiceInstance();
        String result = "111 hello" + message + " from =>>    /hello, host:" + instance.getHost() + ", service_id:" + instance.getServiceId();
        result+="<br/>";
        result+=message1;
        result+="<br/>";
        result+=message2;
        
        if("dummyerror".equals(message)){
        	throw new Exception("dummy exception");
        }
        
        return result;
    }
    
    public String showErrorMessage(String message){
    	return "trying to get:"+message+" but get something wrong...";
    }

}