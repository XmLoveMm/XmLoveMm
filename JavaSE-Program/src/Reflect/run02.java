package Reflect;

import java.lang.reflect.Field;

/**
 * 获取属性
 * <p>
 * 1、批量获取：
 * 1）Field[] getFields():获取所有的共有字段（父类中的共有字段也可以获取到）
 * 2）Field[] getDeclaredFields():获取所有字段包括：public、private、protected
 * 2、获取单个的：
 * 1）Field getField(String fieldName)
 * 2)Field getDeclaredField(String fieldName)
 * 3、设置字段的值
 * public void set(Object obj , Object value):
 * 1、obj：要设置的字段所在的对象。
 * 2、value：要为字段设置的值。
 */
public class run02 {
    public static void main(String[] args) {
        System.out.println("1、获取所有公有字段：Field[] getFields()------");
        getAllPublicField();
        System.out.println("2、获取所有属性：Field[] getDeclaredFields()--");
        getAllFileds();
        System.out.println("3、获取指定的字段：Field getField()/getDeclaredField()--");
        getSingleField();
        System.out.println("4、设置字段的值 set(Object obj , Object value)----------");
        setValue();
    }

    /**
     * 1、获取所有公有字段：Field[] getFields()
     */
    public static void getAllPublicField() {
        try {
            Class clazz = Class.forName("Reflect.practise01.Student");
            Field[] fields = clazz.getFields();
            for (Field fs : fields) {
                System.out.println(fs);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2、获取所有属性：Field[] getDeclaredFields()
     */
    public static void getAllFileds() {
        try {
            Class clazz = Class.forName("Reflect.practise01.Student");
            Field[] fields = clazz.getDeclaredFields();
            for (Field fs : fields) {
                System.out.println(fs);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 3、获取指定的字段
     */
    public static void getSingleField() {
        try {
            Class clazz = Class.forName("Reflect.practise01.Student");
            Field field = clazz.getField("sex");//只能拿到public修饰的变量(若不是则会报错)
            System.out.println("getField:" + field.getName());
            Field field1 = clazz.getDeclaredField("name");
            System.out.println("getDeclaredField:" + field1.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4、设置字段的值 set(Object obj , Object value)
     */
    public static void setValue() {
        Object[] arr = {"xiaoman",26, '男'};
        int i = 0;
        try {
            Class clazz = Class.forName("Reflect.practise01.Student");
            //1）Field的常用方法
            Field[] fields = clazz.getDeclaredFields();
            /**
             for (Field fs : fields) {
             System.out.println("Field:"+fs);
             //#获取属性名称
             System.out.println("FieldName:"+fs.getName());
             //#获取属性的数据类型
             System.out.println("FieldType:"+fs.getType().getName());
             }*/
            //2）为属性进行赋值
            Student stu = (Student) clazz.newInstance();
            for (Field fs : fields) {
                fs.setAccessible(true);//若不加这个，则不能为private类型的变量进行赋值，会报错
                fs.set(stu, arr[i++]);
            }

            System.out.println("name:" + stu.getName());
            System.out.println("age:" + stu.getAge());
            System.out.println("sex:" + stu.getSex());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
