/* TCPServer.java from Kurose-Ross */

/*
* CS3873 Lab 2
* Feb.10/19
*	Avery Briggs
* 3471065
*
*	% I warrant that this is my own work.
* --- Avery Briggs (3471065) %
*
* Simple Socket program to act as a remote server to a client using a
* TCP connection. Program recieves a message and sends it back to the
*	client line-by-line in a formatted string.
*
*/

import java.io.*;
import java.net.*;

class TCPFileServer {
	public static void main(String args[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		Socket connectionSocket = null;
		BufferedReader inFromClient = null;
		DataOutputStream outToClient = null;
		System.out.println ("Waiting for connection.....");

		while (true) {
			connectionSocket = welcomeSocket.accept();
			System.out.println ("Connection successful");
		  System.out.println ("Waiting for input.....");
			try{
				// buffer for reading in from client
				inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				// buffer for writing to client
				outToClient = new DataOutputStream(
						connectionSocket.getOutputStream());
				// while connection is open read data
				while(true){
					clientSentence = "";
					// early exit check if nothing was read over the connection
					if((clientSentence = inFromClient.readLine()) == null || clientSentence == ""){
						System.out.println("CONNECTION TERMINATED");
						return;
					}
					// printing to console window of the server
					System.out.println("From client at " + connectionSocket.getInetAddress()
						+ ": " + clientSentence);
					capitalizedSentence = clientSentence.toUpperCase() + '\n';
					// write the message back to the sender in capitals
					outToClient.writeBytes(capitalizedSentence);
				}
			}
			catch(Exception e){
				System.out.println("Fail");
				e.printStackTrace();
			}
			finally{
				inFromClient.close();
				outToClient.close();
			}
		}
	}
}
