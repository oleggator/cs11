import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.atomic.AtomicInteger;

class Crawler {

    /* Сканирует ссылки на сайте */
    static private void getSites(URL url, int depth, int threadsCount) {

        try {
            Object mutex = new Object();
            AtomicInteger activeThreadsCount = new AtomicInteger(threadsCount);
            URLDepthPair urlDepthPair = new URLDepthPair(url, 0);
            URLPool pool = new URLPool();
            pool.addToScan(urlDepthPair);

            for (int i = 0; i < threadsCount; ++i) {
                Thread thread = new Thread(new CrawlerTask(pool, depth, mutex, activeThreadsCount, i));
                thread.start();
            }

            synchronized(mutex) {
                try {
                    mutex.wait();
                }
                catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }

                return;
            }
        }
        catch (MalformedURLException message) {
            System.out.println("Crawler <URL> <глубина поиска>");
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Crawler <URL> <глубина поиска>");

            return;
        }

        try {
            getSites(new URL(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } 
        catch (MalformedURLException message) {
            System.out.println("Crawler <URL> <глубина поиска>");
        }
    }
}
