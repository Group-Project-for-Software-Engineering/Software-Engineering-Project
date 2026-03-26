package classes;

import java.io.*;
import java.net.Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Socket s = socket;
             DataInputStream in = new DataInputStream(s.getInputStream());
             DataOutputStream out = new DataOutputStream(s.getOutputStream())) {

            //Receive job/vehicle in string form
            String data = in.readUTF();
            System.out.println("Received from client: " + data);

            // ACK
            out.writeUTF("ACK");

            // Wrap into Request
            Request req = new Request();
            //req object has the string version of the object, the deicison by the admin, and the out itself
            req.request = data;
            req.out = out;

            //Add to pending list
            synchronized (VCServer.pending) {
                VCServer.pending.add(req);
                VCServer.pending.notifyAll();   // wake AdminPending
            }

            //Wait for admin decision
            synchronized (req) {
                req.wait();
            }

            //Send decision back
            out.writeUTF(req.decision);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
