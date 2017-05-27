import java.util.*;

class URLPool {

    private LinkedList<URLDepthPair> scannedUrls = new LinkedList<URLDepthPair>();
    private LinkedList<URLDepthPair> urlsToScan = new LinkedList<URLDepthPair>();

    public synchronized boolean addToScan(URLDepthPair urlDepthPair) {
        if (scannedUrls.contains(urlDepthPair))
            return false;
        else
            return urlsToScan.add(urlDepthPair);
    }

    public synchronized boolean addScanned(URLDepthPair urlDepthPair) {
        if (scannedUrls.contains(urlDepthPair))
            return false;
        else
            return scannedUrls.add(urlDepthPair);
    }

    public synchronized URLDepthPair get() {
        return urlsToScan.pollFirst();
    }

    public synchronized int getSize() {
        return urlsToScan.size();
    }

};
