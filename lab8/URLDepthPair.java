import java.net.URL;
import java.net.MalformedURLException;

class URLDepthPair {

    private URL url;
    private int depth;

    public URLDepthPair(URL url, int depth) throws MalformedURLException {
        this.url = url; 
        this.depth = depth;
    }

    public URL getUrl() {
        return url;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass())
            return false;

        if (((URLDepthPair)obj).url.toString().equals(url.toString()))
            return true;

        return false;
    }

    @Override
    public int hashCode() {
        return url.toString().hashCode();
    }
}
