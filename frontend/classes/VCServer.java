package classes;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class VCServer implements Runnable {

    private ServerSocket serverSocket;
    private boolean running = true;

    // Requests waiting for admin review (from clients) /
    public static final ArrayList<Request> pending = new ArrayList<>();

    // Requests the admin UI should display
    public static final ArrayList<Request> adminVisible = new ArrayList<>();

    public VCServer() {
        try {
            serverSocket = new ServerSocket(9806);
            System.out.println("VCServer started on port 9806...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    //each time a new user connects via sumbitting a vehicle or job, it accepts the socket and creates a new thread for that client
    //allows multiple users to submit at the same time
    public void run() {
        while (running) {
            try {
                System.out.println("Waiting for client/owner...");
                Socket socket = serverSocket.accept();
                System.out.println("Client/Owner connected!");

                new Thread(new ClientHandler(socket)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Start this when the program is first launched
    //waits until a user submits an application. When I does it tranfers it from the pending list to the admin list to accept/reject
    //this works even if the admin logs in after a user has submitted a job. Hence the pending list 
    public static void startPendingListener() {
        new Thread(() -> {
            while (true) {
                Request req;

                synchronized (pending) {
                    while (pending.isEmpty()) {
                        try {
                            pending.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Remove from server queue
                    req = pending.remove(0);
                }

                // Add to admin-visible list
                synchronized (adminVisible) {
                    adminVisible.add(req);
                }
            }
        }, "PendingListener").start();
    }
}
