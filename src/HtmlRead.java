import java.net.*;
import java.io.*;

public class HtmlRead {
    public static void main(String[] args) {
        new HtmlRead();
    }

    public HtmlRead() {
        String target = "https://en.wikipedia.org/wiki/Soap";
        try {
            URL url = new URL(target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Pretend to be a normal browser
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int status = conn.getResponseCode();
            if (status >= 300 && status < 400) {
                // Follow manual redirects if needed
                String loc = conn.getHeaderField("Location");
                if (loc != null) {
                    conn.disconnect();
                    url = new URL(loc);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                                    + "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
                }
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("HTTP error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.contains("href")) {
                        int v = 0;
                        while (v < line.length()){
                            v = line.indexOf("https", v + 1);
                            if (v == -1) break; //I had to search online for this break line
                            // though because there was a bug and I wasn't sure why
                            int a = line.indexOf('"', v);
                            if (line.contains("https")) {
                                System.out.println(line.substring(v, a));
                            }
                        }
                    }
                }

            } finally {
                conn.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}