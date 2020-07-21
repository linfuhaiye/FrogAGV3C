package test;


import java.util.Random;

/**
 * @author chenfuqian
 */
public class Test {

    public static void main(String[] agrs) {
        System.out.println(1.0 == 1);

        Random rand = new Random();
        System.out.println(rand.nextInt(1));

        String s = "+014.60";

        System.out.println(Float.parseFloat(s));
    }
}
