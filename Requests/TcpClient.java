package internetofeveryone.ioe.Requests;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TcpClient {

    private static final String ROUTER_IP = "192.168.42.1"; // router ip address
    private static final int ROUTER_PORT = 42; // port

    private final String TAG = "TcpClient";
    private String mServerMessage; // message to send to the router
    private OnMessageReceived mMessageListener = null; // sends message-received notifications
    private boolean mRun = false; // while true, the server will continue running
    private PrintWriter mBufferOut; // to send messages
    private BufferedReader mBufferIn;// to read messages from the router
    private Socket socket;
    private boolean networkoffline;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from router
     */
    public TcpClient(OnMessageReceived listener) {
        Log.d(TAG, "new TcpClient created");
        mMessageListener = listener;
    }

    /**
     * Sends the message to the router
     *
     * @param message the message to be sent
     */
    public boolean sendMessage(String message) {

        Log.d(TAG, "TcpClient sends Message: " + message);

        if (mBufferOut != null && !mBufferOut.checkError()) {

            Log.d(TAG, "mBufferOut fully functional");
            mBufferOut.println(message);
            mBufferOut.flush();
        } else {
            Log.d(TAG, "mBufferOut NOT fully functional. mBufferOut is " + mBufferOut);
            if (!run()) {
                Log.e(TAG, "sending Message didn't work (network failure)");
                return false;
            }
            sendMessage(message);
        }
        return true;
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        Log.d(TAG, "client stopped");

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

    public boolean run() {
        if (networkoffline) {
            return false;
        }
        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(ROUTER_IP);

            Log.e(TAG, "C: Connecting...");

            // makes connection with the server
            if (socket == null || socket.isClosed()) {
                socket = new Socket(serverAddr, ROUTER_PORT);
            }
            Log.d(TAG, "socket = " + socket);
            socket.setSoTimeout(6 * 10000); // 6 * 10 seconds

            try {

                if (mBufferOut == null) {
                    // sends message to the router
                    mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                }
                // receives message which the router sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // in this while the client listens for the messages sent by the router
                while (mRun) {

                    Log.d(TAG, "in while(mRun)");
                    mServerMessage = mBufferIn.readLine();
                    Log.d(TAG, "finished reading message from the router: " + mServerMessage);

                    if (mServerMessage != null && mMessageListener != null) {
                        Log.d(TAG, "mServerMessage = " + mServerMessage + ", mMessageListener " + mMessageListener);
                        mMessageListener.messageReceived(mServerMessage);
                        Log.d(TAG, "responseAnswer from presenter = " + mServerMessage);
                        mRun = false;
                    } else {
                        Log.d(TAG, "else");
                        throw (new SocketTimeoutException());
                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
                if (mServerMessage == null) {
                    if(socket.isConnected()){
                        socket.close();
                    }
                }

            } catch (SocketTimeoutException e){
                Log.d(TAG,"Timeout");
                if(socket.isConnected()){
                    socket.close();
                }
                run();

            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);

            } finally {
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
            networkoffline = true;
            return false;
        }
    return true;
    }

    public interface OnMessageReceived {
        void messageReceived(String message);
    }

}