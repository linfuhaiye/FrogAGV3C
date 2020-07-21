//package test;
//
//import com.alibaba.fastjson.JSON;
//import com.sun.javafx.font.directwrite.RECT;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author chenfuqian
// */
//public class Role {
//
//    public static void main(String[] agrs) {
//        List<RECT> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            RECT rect = new RECT();
//            rect.left = 1;
//            rect.top = 2;
//            rect.right = 10;
//            rect.bottom = 10 * i;
//            list.add(rect);
//        }
//
//        RECT rect = getMax(list);
//
//        System.out.println(JSON.toJSONString(rect));
//    }
//
//    public static RECT getMax(List<RECT> list) {
//        RECT rect = null;
//        for (RECT rect1 : list) {
//            if (rect == null) {
//                rect = rect1;
//            } else {
//                rect = (getArea(rect1) - getArea(rect) > 0) ? rect1 : rect;
//            }
//        }
//        return rect;
//    }
//
//    private static int getArea(RECT rect) {
//        return (rect.right - rect.left) * (rect.bottom - rect.top);
//    }
//}
