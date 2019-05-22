package config;

import entity.EdgeNode;
import entity.FogBlockQueue;
import entity.Global;
import entity.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.xml.bind.annotation.XmlElementDecl;

@Configuration
@ComponentScan(basePackages = "entity")
public class Config {

    @Scope("prototype")
    @Bean("queue")
    public FogBlockQueue<Request> getFogBlockQueue(){
        return new FogBlockQueue<Request>("test",Global.EDGNODEQUEUECAPACITY,false);
    }

}
