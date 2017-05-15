import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

class Crawler {

    /* Сканирует ссылки на сайте */
    static private int scanSite(URL url, int depth) {
        LinkedList<URLDepthPair> scannedUrls = new LinkedList<URLDepthPair>();
        LinkedList<URL> urlsToScan = new LinkedList<URL>();
        urlsToScan.add(url);

        int counter = 0;
        for (int i = 1; i <= depth; ++i) {
            LinkedList<URL> currentUrlsToScan = urlsToScan;
            urlsToScan = new LinkedList<URL>();

            for (URL currentUrl: currentUrlsToScan)
                counter += scanPage(currentUrl, i, scannedUrls, urlsToScan);
        }

        return counter;
    }

    /* Сканирует ссылки на странице */
    static private int scanPage(URL url, int depth,
                                LinkedList<URLDepthPair> scannedUrls,
                                LinkedList<URL> urlsToScan) {
        Socket socket;

        try {
            socket = new Socket(InetAddress.getByName(url.getHost()), url.getDefaultPort());
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            return 0;
        }

        try {
            socket.setSoTimeout(5000);
        }
        catch (SocketException e) {
            System.err.println(e.getMessage());
            return 0;
        }

        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
        }
        catch (IOException e) {
            System.err.println("e.getMessage()");
            return 0;
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        OutputStream outputStream;
        try {
            outputStream = socket.getOutputStream();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            return 0;
        }

        PrintWriter printWriter = new PrintWriter(outputStream, true);

        printWriter.println("GET /" + url.getPath() + " HTTP/1.1");
        printWriter.println("Host: " + url.getHost());
        printWriter.println("Connection: close");
        printWriter.println("");

        String responseLine = null;
        try {
            int counter = 0;

            while ((responseLine = bufferedReader.readLine()) != null) {
                Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(responseLine);

                if (matcher.find()) {
                    String urlString = matcher.group(1);

                    URL scannedUrl = new URL(url, urlString);
                    URLDepthPair urlDepthPair = new URLDepthPair(scannedUrl.toString(), depth);

                    if (scannedUrl.getProtocol().equals("http")
                        && !scannedUrls.contains(urlDepthPair)) {

                        urlsToScan.add(scannedUrl);
                        scannedUrls.add(urlDepthPair);

                        System.out.println(scannedUrl.toString());

                        ++counter;
                    }
                }
            }

            return counter;
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Crawler <URL> <глубина поиска>");

            return;
        }

        try {
            scanSite(new URL(args[0]), Integer.parseInt(args[1]));
        } 
        catch (MalformedURLException message) {
            System.out.println("Crawler <URL> <глубина поиска>");
        }
    }
}
