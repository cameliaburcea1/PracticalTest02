package ro.pub.cs.systems.eim.practical2test;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private final String url;
    private final TextView responseTextView;

    private Socket socket;

    public ClientThread(String address, int port, String url, TextView responseTextView) {
        this.address = address;
        this.port = port;
        this.url = url;
        this.responseTextView = responseTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // sends the city and information type to the server
            printWriter.println(url);
            printWriter.flush();
            String response;

            // reads the weather information from the server
            while ((response = bufferedReader.readLine()) != null) {
                final String finalizedResponse = response;

                // updates the UI with the weather information. This is done using postt() method to ensure it is executed on UI thread
                responseTextView.post(() -> responseTextView.setText(finalizedResponse));
            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}