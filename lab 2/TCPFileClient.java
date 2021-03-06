/* TCPClient.java from Kurose-Ross */

/*
* CS3873 Lab 2
* Feb.10/19
*	Avery Briggs
* 3471065
*
*	% I warrant that this is my own work.
* --- Avery Briggs (3471065) %
*
* Simple Socket program to act as a client in a client to server
* TCP connection. Program takes in a text file and sends it to the
*	server line-by-line in a string.
*
*	USAGE:
* program requires 1 argument given on the cmd line.
* The address of the remote server. The user will use
* command line redirection to send a file over the
* connection. (java FILE < file.txt)
* Use "PoemShakespeare.txt"
*
*/

import java.io.*;
import java.net.*;

class TCPFileClient {
	public static void main(String args[]) throws Exception {
		String sentence;
		String modifiedSentence;
		int bytesSent = 0;
		// early exit, bad input
		if(args.length < 1){
			System.out.println("ERROR not enough arguments given: " + args.length);
			return;
		}
		BufferedReader inFromUser = null;
		Socket clientSocket = null;
		long timeStart = System.currentTimeMillis();
		try{
			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			clientSocket = new Socket(args[0], 6789);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			modifiedSentence = "";
			while((sentence = inFromUser.readLine()) != null){
				outToServer.writeBytes(sentence + '\n');
				// modified sentence is sent from the server
				modifiedSentence = inFromServer.readLine();
				//System.out.println("FROM SERVER: " + modifiedSentence);
				for(int i = 0; i < sentence.length(); i++){
					bytesSent += Character.BYTES;
				}
			}
			// calculate elapsed time
			timeStart = System.currentTimeMillis() - timeStart;
			// display connection statistics
			System.out.println("Connection Duration: " + timeStart + " ms");
			System.out.println("Bytes Sent: " + bytesSent + " B");
		}
		// Exception handling
		catch(Exception e){
			System.out.println("Failed");
			e.printStackTrace();
		}
		// close buffers
		finally{
			inFromUser.close();
			clientSocket.close();
		}
	}
}
