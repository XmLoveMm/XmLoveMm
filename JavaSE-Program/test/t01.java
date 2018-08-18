import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 反射练习
 */
public class t01 {
}

class methodToUse {
    //读取配置文件
    public Map<Object, Object> getfileData(String path) {
        Map<Object, Object> dataMap = null;

        Properties properties=new Properties();
        BufferedInputStream bis =null;
        try {
            bis =new BufferedInputStream(new FileInputStream("F:\\DailyProgram\\JavaSE-Program\\src\\resource\\personInfo.properties"));
            properties.load(bis);
            for(String key : properties.stringPropertyNames()){
                dataMap.put(key,properties.get(key));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return dataMap;
    }

    //根据配置文件的内容进行动态创建对象

}
