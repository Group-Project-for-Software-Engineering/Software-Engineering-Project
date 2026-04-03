
 /* Project: Vehicular Cloud Real Time System (VCRTS)
  * Class: ClientHandler.java
  * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
  * Date: March 2026
  * This class implements the ClientHandler that runs in a separate thread for each client connection to the server. 
  * It listens for incoming data from the client, wraps it into a Request object, and adds it to the pending list for admin review. 
  * It then waits for the admin's decision and sends the decision back to the client.
  */
package classes;

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
