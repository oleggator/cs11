import java.net.*;

class URLDepthPair {

    URL url;
    int depth;

    public URLDepthPair(String urlString, int depth) throws MalformedURLException {
        try {
            url = new URL(urlString);
            this.depth = depth;
        } catch (MalformedURLException e) {
            throw new MalformedURLException();
        }
    }

    public URLDepthPair(URL baseUrl, String urlString, int depth) throws MalformedURLException {
        try {
            url = new URL(baseUrl, urlString);
            this.depth = depth;
        } catch (MalformedURLException e) {
            throw new MalformedURLException();
        }
    }

    public boolean equals(Object other) {
        return this.getClass() == other.getClass()
            && ((URLDepthPair)other).url.toString().equals(url.toString());
    }
}
