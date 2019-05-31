import Util.DeepCopy;
import entity.Request;
import entity.Terminal;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CopyTest {

    public static void main(String agrs[]) throws InterruptedException{
        List<Terminal> terminals=new ArrayList<>();
        Terminal terminal1=new Terminal(1,100);
        Terminal terminal2=new Terminal(2,100);
        terminals.add(terminal1);
        terminals.add(terminal2);
        List<Terminal> terminalsCopy= DeepCopy.deepCopyList(terminals);
        System.out.println("origin");
        terminals.get(0).getQueue().setSerial("hahaha");
        for(Terminal terminal:terminals){
            System.out.println(terminal.getQueue().getSerial());
        }
        System.out.println("copy");
        for(Terminal terminal:terminalsCopy){
            System.out.println(terminal.getQueue().getSerial());
        }

        Terminal terminal3=new Terminal(3,100);
        Terminal terminal4 =DeepCopy.deepCopyList(terminal3);
        terminal3.getQueue().put(new Request());
        terminal4.getQueue().put(new Request());
        terminal4.getQueue().put(new Request());
        System.out.println(terminal3.getQueue().getSize()+" "+terminal4.getQueue().getSize());
    }
}
