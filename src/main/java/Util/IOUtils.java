package Util;

import entity.Global;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class IOUtils {
    private static HashMap<String,PrintWriter> printMap;

    static {
        printMap=new HashMap<>();
        try{
            for(int i=1;i<= Global.TERMINLNUM;i++){
                Path path= Paths.get(Global.resultFilePath+"\\terminal"+i+".txt");
                checkPath(path);
                OutputStream out=Files.newOutputStream(path, StandardOpenOption.CREATE);
                PrintWriter writer=new PrintWriter(out);
                printMap.put("terminal"+i,writer);
            }
            Path SDNpath= Paths.get(Global.resultFilePath+"\\SND.txt");
            checkPath(SDNpath);
            OutputStream out=Files.newOutputStream(SDNpath, StandardOpenOption.CREATE);
            PrintWriter writer=new PrintWriter(out);
            printMap.put("SDN",writer);
            for(int i=1;i<=Global.EDGENODENUM;i++){
                Path path= Paths.get(Global.resultFilePath+"\\edgeNode"+i+".txt");
                checkPath(path);
                OutputStream edgeOut=Files.newOutputStream(path, StandardOpenOption.CREATE);
                PrintWriter edgeWriter=new PrintWriter(edgeOut);
                printMap.put("edgeNode"+i,edgeWriter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void println(String origin,String content){
        printMap.get(origin).println(content);
        printMap.get(origin).flush();
    }


    private static void checkPath(Path path){
        try{
            if(Files.exists(path)){
                Files.delete(path);
                Files.createFile(path);
            }else{
                Files.createFile(path);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
