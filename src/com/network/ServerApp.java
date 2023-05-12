package com.network;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ServerApp {
	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;

	/** flux d'entrée (pour la reception) assoicé à la socket associé au client */
	private ObjectInputStream in;

	/** flux de sortie (pour l'envoi) assoicé à la socket associé au client */
	private ObjectOutputStream out;

	// constructor with port
	public ServerApp(int port) throws Exception {

		boolean noProblem = true;

		server = new ServerSocket(port);
		System.out.println("Server started");

		do {
			System.out.println("Waiting for a client ...");

			socket = server.accept();
			System.out.println("Client accepted !");

			// takes input from the client socket
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			noProblem = true;

			while (noProblem) {

				try {

					String receivedData;

					// reads message from client
					receivedData = (String) in.readObject();

					//if it wants to close connection
					if (receivedData == "end_connection") {

						System.out.println("Closing connection");

						// close connection
						in.close();
						out.close();
						socket.close();
						break;
					} else {

						//print the message received
						System.out.println("Message received :" + receivedData);
						out.writeObject("J'ai recu ton message !!");
					}

		

					out.flush();
				}

				catch (Exception e) {
					System.out.println("Connection  with client is lost. Waiting for reconnection...");
					// e.printStackTrace();
					try {
						// close connection
						in.close();

						out.close();
						socket.close();
					} catch (Exception ex) {
						System.err.println("Cant close connection...");
					}

					noProblem = false;
				}
			}
			try {
				// close connection
				in.close();
				out.close();
				socket.close();
			} catch (Exception ex) {
				System.err.println("Cant close connection...");
			}
		} while (true);

	}

	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("please entrer the network port number (Example: 5000)");
		int port  = sc.nextInt();
		ServerApp server = new ServerApp(port);
	}
}
