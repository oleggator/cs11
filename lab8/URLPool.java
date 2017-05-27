import java.util.*;

class URLPool {

    private LinkedList<URLDepthPair> scannedUrls = new LinkedList<URLDepthPair>();
    private LinkedList<URLDepthPair> urlsToScan = new LinkedList<URLDepthPair>();

    /* Добавление ссылки в список для сканирования */
    public synchronized boolean addToScan(URLDepthPair urlDepthPair) {
        if (scannedUrls.contains(urlDepthPair))
            return false;
        else
            return urlsToScan.add(urlDepthPair);
    }

    /* Добавление ссылки в список отсканированных ссылок */
    public synchronized boolean addScanned(URLDepthPair urlDepthPair) {
        if (scannedUrls.contains(urlDepthPair))
            return false;
        else
            return scannedUrls.add(urlDepthPair);
    }

    /* Получение следующей ссылки для сканирования и удаление её из списка */
    public synchronized URLDepthPair get() {
        return urlsToScan.pollFirst();
    }

    public synchronized int getSize() {
        return urlsToScan.size();
    }

};
