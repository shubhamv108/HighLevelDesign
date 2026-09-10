package SystemDesign.ProgrammingLanguage.Java.versions.Network;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

public class WebSocketExample {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        WebSocket webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://echo.websocket.events"), new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        System.out.println("WebSocket opened");
                        webSocket.sendText("Hello, WebSocket!", true);
                        WebSocket.Listener.super.onOpen(webSocket);
                    }

                    @Override
                    public CompletableFuture<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        System.out.println("Received message: " + data);
                        return CompletableFuture.completedFuture(null); // Explicitly return a completed future
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        System.err.println("Error occurred: " + error.getMessage());
                    }
                }).join();
    }
}

