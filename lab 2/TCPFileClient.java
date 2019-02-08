/* TCPClient.java from Kurose-Ross */

import java.io.*;
import java.net.*;

class TCPFileClient {
	public static void main(String args[]) throws Exception {
		String sentence;
		String modifiedSentence;
		if(args.length != 2){
			System.out.println("ERROR not enough arguments given: " + args.length);
			return;
		}
		for(String arg : args){
			System.out.println(arg);
		}
		String file = args[1];
		file = "./a.txt";
		FileReader fr  = null;
		BufferedReader inFromUser = null;
		fr = new FileReader(file);
		inFromUser = new BufferedReader(fr);
		//Socket clientSocket = new Socket(args[0], 6789);
		//DataOutputStream outToServer = new DataOutputStream(
		//		clientSocket.getOutputStream());
		//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
		//		clientSocket.getInputStream()));

		modifiedSentence = "";
		//System.out.print("Enter message: ");
		while((sentence = inFromUser.readLine()) != null){
			modifiedSentence += sentence + "\n";
			//outToServer.writeBytes(sentence + '\n');
			//modifiedSentence = inFromServer.readLine();
		}
		System.out.println("FROM SERVER: " + modifiedSentence);
		inFromUser.close();
		//clientSocket.close();
	}
}
