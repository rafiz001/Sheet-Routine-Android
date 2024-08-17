package com.rafizuddin.sheetroutine;

import android.content.Context;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleWebServer extends NanoHTTPD {

    private final Context context;

    public SimpleWebServer(Context context, int port) throws IOException {
        super(port);
        this.context = context;
        start(SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if ("/".equals(uri)) {
            uri = "/index.html"; // Default file
        }

        try {
            // Adjust the URI path to access the assets folder
            String assetPath = uri.startsWith("/") ? uri.substring(1) : uri;
            InputStream inputStream = context.getAssets().open(assetPath);

            if (inputStream != null) {
                // Determine MIME type based on file extension
                String mimeType = getMimeTypeForFile(assetPath);
                return newFixedLengthResponse(Response.Status.OK, mimeType, inputStream, inputStream.available());
            } else {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Internal error");
        }
    }

    // Helper method to get MIME type based on file extension
    public static String getMimeTypeForFile(String filePath) {
        if (filePath.endsWith(".html")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        }else if (filePath.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "application/octet-stream";
        }
    }
}