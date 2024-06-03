import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.stream.Stream;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientPost {
    public static void main(String[] args) {

        try {
//            URI uri = new URI("http://localhost:8080");
//            URL url = uri.toURL();
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMinutes(1))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "first=Joe&last=Smith"))
                    .uri(URI.create("http://localhost:8080"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFile(Path.of("test.html")));
            if (response.statusCode() != HTTP_OK) {
                System.out.println("Error reading web page " + request.uri());
                return;
            }

            // USED WITH HttpResponse<Stream<String>> response = ...BodyHandlers.ofLines()
//            response.body()
//                    .filter(s -> s.contains("<h1>"))
//                    .map(s -> s.replaceAll("<[^>]*>", "").strip())
//                    .forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    private static void printContents(InputStream is) {
//        try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(is))
//        ) {
//            String line;
//            while ((line = inputStream.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
