package Reflect;

/**
 * 获取Class的三种方式
 * 1、Object ——>getClass
 * 2、类名.class
 * 3、Class.forName(句对路径)
 */
public class run01 {
    public static void main(String []args) {
        System.out.println("1、getClass----------");
        byGetClass();
        System.out.println("2、类名.class--------");
        byClass();
        System.out.println("3、Class.forName(绝对路径)--------");
        byClassForName();
    }

    /**
     * 1、通过getClass获取Class
     */
    public static void byGetClass() {
        Student st01 = new Student();
        Class clazz = st01.getClass();
        String name = clazz.getName();
        System.out.println("    name:" + name);
    }

    /**
     * 2、类名.class
     */
    public static void byClass() {
        Class clazz = Student.class;
        String name = clazz.getName();
        System.out.println("    name:" + name);
    }

    /**
     * 3、Class.forName(绝对路径)
     */
    public static void byClassForName() {
        try {
            Class clazz = Class.forName("Reflect.Student");
            System.out.println("    name:" + clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
