import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.atomic.AtomicInteger;

class CrawlerTask implements Runnable {

    private final URLPool urlPool;
    private int depth;
    private Object mutex;
    private final AtomicInteger activeThreadsCount;
    private int threadNumber;

    CrawlerTask(URLPool initURLPool,
                int initDepth,
                Object mutex,
                AtomicInteger initActiveThreadsCount,
                int initThreadNumber) {
        urlPool = initURLPool;
        depth = initDepth;
        activeThreadsCount = initActiveThreadsCount;
        threadNumber = initThreadNumber;

        System.out.println(threadNumber);
    }

    public void run() {
        while (true) {
            if (urlPool.getSize() == 0) {
                try {
                    if (activeThreadsCount.get() == 0) {
                        synchronized(mutex) {
                            mutex.notify();
                        }
                    }

                    synchronized(activeThreadsCount) {
                        activeThreadsCount.addAndGet(-1);
                    }
                    synchronized(urlPool) {
                        urlPool.wait();
                    }
                }
                catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
            }
            
            URLDepthPair urlDepthPair = urlPool.get();
            if (urlDepthPair.getDepth() + 1 > depth) {
                return;
            }

            Socket socket;

            try {
                socket = new Socket(InetAddress.getByName(urlDepthPair.getUrl().getHost()),
                                                          urlDepthPair.getUrl().getDefaultPort());
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                continue;
            }

            try {
                socket.setSoTimeout(5000);
            }
            catch (SocketException e) {
                System.err.println(e.getMessage());
                continue;
            }

            InputStream inputStream;
            try {
                inputStream = socket.getInputStream();
            }
            catch (IOException e) {
                System.err.println("e.getMessage()");
                continue;
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            OutputStream outputStream;
            try {
                outputStream = socket.getOutputStream();
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                continue;
            }

            PrintWriter printWriter = new PrintWriter(outputStream, true);

            printWriter.println("GET /" + urlDepthPair.getUrl().getPath() + " HTTP/1.1");
            printWriter.println("Host: " + urlDepthPair.getUrl().getHost());
            printWriter.println("Connection: close");
            printWriter.println("");

            String responseLine = null;
            try {
                while ((responseLine = bufferedReader.readLine()) != null) {
                    Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(responseLine);

                    if (matcher.find()) {
                        String urlString = matcher.group(1);
                        URL scannedUrl = new URL(urlDepthPair.getUrl(), urlString);

                        if (scannedUrl.getProtocol().equals("http")) {
                            if (urlPool.addToScan(new URLDepthPair(scannedUrl,
                                                                   urlDepthPair.getDepth() + 1))) {

                                System.out.println((urlDepthPair.getDepth() + 1) + ": " +
                                                  scannedUrl.toString());
                                                  
                                synchronized(urlPool) {
                                    urlPool.notifyAll();
                                }
                                
                            }
                        }
                    }
                }
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
                continue;
            }
        }
    }
};
