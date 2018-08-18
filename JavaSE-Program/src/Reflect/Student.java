package Reflect;

public class Student {
    private String name;
    private int age;
    public char sex;

    public Student(String name, int age, char sex) {
        System.out.println("name:" + name + "、age：" + age + "、sex：" + sex);
    }

    public Student() {
        System.out.println("调用了无参构造方法");
    }

    public Student(int age) {
        System.out.println("age：" + age);
    }

    private Student(String name) {
        System.out.println("name：" + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }
}
