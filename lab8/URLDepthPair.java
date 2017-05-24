import java.net.*;

class URLDepthPair extends Object {

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
        System.out.println("equals");
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        if (((URLDepthPair)obj).url.toString().equals(url.toString())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        System.out.println("hash");
        return url.toString().hashCode();
    }
}
