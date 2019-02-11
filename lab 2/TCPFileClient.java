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
* connection
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
		//String file = "./" + args[1];
		//System.out.println(file);
		//file = "./a.txt";
		//FileReader fr  = null;
		BufferedReader inFromUser = null;
		Socket clientSocket = null;
		long timeStart = System.currentTimeMillis();
		try{
			//fr = new FileReader(file);
			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			clientSocket = new Socket(args[0], 6789);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			modifiedSentence = "";
			//System.out.print("Enter message: ");
			while((sentence = inFromUser.readLine()) != null){
				//modifiedSentence += sentence + "\n";
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + modifiedSentence);
				bytesSent += sentence.length();
			}
			timeStart = System.currentTimeMillis() - timeStart;
			System.out.println("Connection Duration: " + timeStart + "ms");
			System.out.println("Bytes Sent: " + bytesSent + "B");
		}
		catch(Exception e){
			System.out.println("Failed");
			e.printStackTrace();
		}
		finally{
			inFromUser.close();
			clientSocket.close();
		}
	}
}
