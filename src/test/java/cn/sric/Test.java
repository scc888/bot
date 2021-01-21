package cn.sric;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] a) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1000, 1000, 200, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        long start = System.currentTimeMillis();

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            executor.execute(() -> {

                new Thread(() -> {
                    list.add(finalI);
                }).start();


                for (int c = 0; c < 800000000; c++) {
                    list.add(c);
                }
            });
        }

        while (executor.getQueue().size() != 0 && executor.getCorePoolSize() != 0) {
            System.out.println("线程还在进行");
        }

        System.out.println("一共执行了这么长时间---->>>" + (System.currentTimeMillis() - start));
    }

    static class Test2 {
        public <T> boolean addList(List<T> list, T c) {
            boolean add;
            synchronized (this) {
                add = list.add(c);
            }
            return add;
        }
    }
}


