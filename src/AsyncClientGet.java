import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncClientGet {
    public static void main(String[] args) {

        try {
            URI uri = new URI("http://localhost:8080");
            URL url = uri.toURL();
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url.toURI())
                    .header("User-Agent", "Chrome")
                    .headers("Accept", "application/json", "Accept", "text/html")
                    .timeout(Duration.ofSeconds(30))
                    .build();

//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("User-Agent", "Chrome");
//            connection.setRequestProperty("Accept", "application/json, text/html");
//            connection.setReadTimeout(30000); // within 30 seconds before timeout

            HttpResponse<Stream<String>> response;
            CompletableFuture<HttpResponse<Stream<String>>> responseFuture =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofLines());

//            while ((response = responseFuture.getNow(null)) == null) {
//                System.out.print(". ");
//                TimeUnit.SECONDS.sleep(1);
//            }
            while (true) {
                try {
                    response = responseFuture.get(1, TimeUnit.SECONDS);
                    if (response != null) break;
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    System.out.print(". ");
                }
            }
            System.out.println();
//            response = responseFuture.join();
            handleResponse(response);

//            int responseCode = connection.getResponseCode();
//            System.out.printf("Response code: %d%n", responseCode);
//            if (responseCode != HTTP_OK) {
//                System.out.println("Error reading web page " + url);
//                System.out.printf("Error: %s%n", connection.getResponseMessage());
//                return;
//            }
//            System.out.println(response.body());
//            printContents(connection.getInputStream());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleResponse(HttpResponse<Stream<String>> response) {

        if (response.statusCode() == HTTP_OK) {
            response.body()
                    .filter(s -> s.contains("<h1>"))
                    .map(s -> s.replaceAll("<[^>]*>", "").strip())
                    .forEach(System.out::println);
        } else {
            System.out.println("Error reading response " + response.uri());
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
