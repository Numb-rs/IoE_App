package internetofeveryone.ioe.Requests;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {

    public static final String ROUTER_IP = "45.154.32.208"; // router ip address
    public static final int ROUTER_PORT = 42;

    private String mServerMessage; // message to send to the router
    private OnMessageReceived mMessageListener = null; // sends message-received notifications
    private boolean mRun = false; // while true, the server will continue running
    private PrintWriter mBufferOut; // to send messages
    private BufferedReader mBufferIn;// to read messages from the router

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from router
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message to the router
     *
     * @param message
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(ROUTER_IP);

            Log.e("TCP Client", "C: Connecting...");

            // makes connection with the server
            Socket socket = new Socket(serverAddr, ROUTER_PORT);

            try {

                // sends message to the router
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                // receives message which the router sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // in this while the client listens for the messages sent by the router
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {

                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}