package test;


import com.furongsoft.base.misc.UUIDUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author chenfuqian
 */
public class Test {

    public static void main(String[] agrs) {
//        System.out.println(1.0 == 1);
//
//        Random rand = new Random();
//        System.out.println(rand.nextInt(1));
//
//        String s = "+014.60";
//
//        System.out.println(Float.parseFloat(s));
        List test = new ArrayList();
        for (int i = 0; i < 10; i++) {
            test.add(i);
        }
        test.forEach(count -> {
            System.out.println(UUIDUtils.getUUID());
        });
    }

    public void printTest() {
    }


}
