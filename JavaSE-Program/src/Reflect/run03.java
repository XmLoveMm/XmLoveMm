package Reflect;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取配置文件信息，根据配置文件信息进行都太创建对象
 */
public class run03 {
    public static void main(String[] args) {
        Methods methods = new Methods();

        //1、调用配置文件信息
        Map<Object, Object> map = methods.getPropertiesInfo();
        for (Map.Entry map1 : map.entrySet()) {
            map.put((String) map1.getKey(), map1.getValue());
        }

        //2、根据配置文件信息进行动态反射
        Person p = methods.setObject(map, Person.class);
        System.out.println(p.toString());
    }
}

class Methods {
    /**
     * 1、读取配置配置信息
     */
    public Map<Object, Object> getPropertiesInfo() {
        Map<Object, Object> map = new HashMap<>();//存放配置信息
        InputStream inputStream = null;//读取配置文件
        Properties properties = new Properties();//解析配置文件

        try {
            inputStream = new BufferedInputStream(new FileInputStream("F:\\DailyProgram\\JavaSE-Program\\src\\resource\\personInfo.properties"));
            properties.load(inputStream);//加载文件
            Set<String> keys = properties.stringPropertyNames();//获取配置文件中的key
            for (String key : keys) {
//                System.out.print(keys + "、");//打印key
                String value = properties.getProperty(key);
                map.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 2、根据配置信息的内容来动态创建对象
     */
    public <T> T setObject(Map<Object, Object> data, Class clazz) {//这是一个泛型方法
        T obj = null;
        try {
            obj = (T) clazz.newInstance();
            for (Map.Entry<Object, Object> datas : data.entrySet()) {
                Field field = clazz.getDeclaredField((String) datas.getKey());//获取指定的Field
                field.setAccessible(true);
                //判断不同的数据类型，设置相应的属性类型
                if (field.getType().getName().equals("java.lang.String")) {
                    field.set(obj, datas.getValue());
                } else if (field.getType().getName().equals("int")) {
                    field.set(obj, isNumeric((String) datas.getValue()) ? Integer.valueOf((String) datas.getValue()).intValue() : 0);
                } else if (field.getType().getName().equals("java.lang.Integer")) {
                    field.set(obj, isNumeric((String) datas.getValue()) ? Integer.valueOf((String) datas.getValue()) : 0);
                } else if (field.getType().getName().equals("double")) {
                    field.set(obj, isNumeric((String) datas.getValue()) ? Double.valueOf((String) datas.getValue()).doubleValue() : 0.0d);
                } else if (field.getType().getName().equals("java.lang.Double")) {
                    field.set(obj, isNumeric((String) datas.getValue()) ? Double.valueOf((String) datas.getValue()) : 0.0d);
                } else if (field.getType().getName().equals("float")) {
                    field.set(obj, isNumeric((String) datas.getValue()) ? Float.valueOf((String) datas.getValue()).floatValue() : 0.0);
                } else if (field.getType().getName().equals("java.lang.Float")) {
                    field.set(obj, isNumeric((String) datas.getValue()) ? Float.valueOf((String) datas.getValue()) : 0.0);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 判断是否为数字--使用正则表达式
     */
    public boolean isNumeric(String str) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) {
            return flag;
        } else {
            flag = true;
            return flag;
        }
    }
}



