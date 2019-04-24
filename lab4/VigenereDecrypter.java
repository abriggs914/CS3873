/*
 CS3873 Lab 4
 Mar.29/19
 Avery Briggs
 3471065

 % I warrant that this is my own work.
 --- Avery Briggs 3471065 %


 Simple program to crack the Vigenere cypher
 on a given text file. Only works for files
 generated with a key of length (0 < x < 6).
 The text file "VigenereCipher.txt" should be
 a long line of characters that were the results
 of encrypting a message with the appropriate key.

 The results of decrypting the vigenere cipher
 on the current VigenereCipher.txt file will
 produce a passage from 'The Great Gadsby':

 "Naturally any man is happy at a satisfactory
  culmination of his plans and so, as Gadsby
  found that public philanthropy was but an
  affair of plain, ordinary approach, it did
  not call for much brain work to find that,
  possibly also, a way might turn up for
  putting handicraft instruction in Branton
  Hills’ schools; for schooling, according
  to him, did not consist only of books and
  black-boards. Hands, also should know how
  to construct various practical things in
  woodwork, plumbing, blacksmithing, masonry,
  and so forth; with thorough instruction in
  sanitation, and that most important of all
  youthful activity gym.";

*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;

public class VigenereDecrypter{

  public static String vigenere;
  public static String originalText;
  public static String decryptedText;
  public static String fileName;
  public static String estimateKey;
  public static int keyLength;
  public static int[] keyShiftValues;
  public static String realText;
  public static double ic;
  public static double[][] sampleICValues;
  public static double[] alphabetFrequencies;
  public static int[][] punctuation;

  public static void main(String[] args){
    realText = "Naturally any man is happy at a satisfactory culmination of his plans and so, as Gadsby found that public philanthropy was but an affair of plain, ordinary approach, it did not call for much brain work to find that, possibly also, a way might turn up for putting handicraft instruction in Branton Hills’ schools; for schooling, according to him, did not consist only of books and black-boards. Hands, also should know how to construct various practical things in woodwork, plumbing, blacksmithing, masonry, and so forth; with thorough instruction in sanitation, and that most important of all youthful activity gym.";
    fileName = "./VigenereCipher.txt";
    if(!loadVigenere()){
      System.out.println("Error failed to load file.");
      return;
    }
    format();
    initAlphabetFrequencies();
    initSampleICValues();
    getIC();
    lookUpIC();
    computeKeyShift();
    decryptVignere();
    printCipherStats();
  }

  public static void printCipherStats(){
    System.out.println("\tOriginal text entered via VigenereCipher.txt:");
    System.out.println(originalText);
    System.out.println("\nComputed IC: " + ic);
    System.out.println("Estimated key length: " + keyLength);
    System.out.println("Estimated key: " + estimateKey + "\n");
    System.out.println("\tThe decrypted message is:\n" + decryptedText);
    System.out.println("\n\tOriginal book passage:\n" + realText);
    System.out.println("\nSource: https://en.wikisource.org/wiki/Page:Gadsby.djvu/30");
  }

  public static void format(){
    ArrayList<Integer> temp = new ArrayList<Integer>();
    String res = "";
    for(int i = 0; i < vigenere.length(); i++){
      if(vigenere.charAt(i) > 'Z' || vigenere.charAt(i) < 'A'){
        temp.add(i);
      }
      else{
        res += Character.toString(vigenere.charAt(i));
      }
    }
    punctuation = new int[temp.size()][2];
    for(int i = 0; i < temp.size(); i++){
      punctuation[i][0] = temp.get(i);
      punctuation[i][1] = vigenere.charAt(temp.get(i));
    }
    originalText = vigenere;
    vigenere = res;
  }

  public static void decryptVignere(){
    decryptedText = "";
    ArrayList<Integer> puncIndicies = new ArrayList<Integer>();
    for(int i = 0; i < punctuation.length; i++){
      puncIndicies.add(punctuation[i][0]);
    }
    for(int i = 0, y = 0, p = 0; i < originalText.length(); i++){
      if(puncIndicies.contains(i)){
        decryptedText += Character.toString((char) punctuation[p++][1]);
        puncIndicies.remove(puncIndicies.indexOf(i));
      }
      else if(vigenere.charAt(y) > 'Z' || vigenere.charAt(y) < 'A'){
        decryptedText += Character.toString(vigenere.charAt(y++));
      }
      else{
        int x = i % keyLength;
        char c = (char) ((int) vigenere.charAt(y++) - keyShiftValues[x]);
        if(c < 'A'){
          c += 26;
        }
        decryptedText += Character.toString(c);
      }
    }
  }

  public static void computeKeyShift(){
    keyShiftValues = new int[keyLength];
    int ksvIndex = 0;
    int len = vigenere.length();
    int numCharPerCol = (int) Math.ceil((double) len / (double) keyLength);
    int[][] temp = new int[keyLength][numCharPerCol];
    for(int i = 0; i < keyLength; i++){
      for(int j = i; j < len; j += keyLength){
        temp[i][j / keyLength] = vigenere.charAt(j);
      }
    }
    String key = "";
    for(int i = 0; i < keyLength; i++){
      keyShiftValues[i] = computeShiftValue(temp[i]);
      char c = (char) (keyShiftValues[i] + 65);
      if(c > 'Z'){
        c -= 26;
      }
      key += Character.toString(c);

    }
    estimateKey = key;
  }

  public static int computeShiftValue(int[] arr){
    double[][] letterCensus = new double[26][3];
    int len = arr.length;
    for(int i = 0, l = 'A'; i < 26; i++, l++){
      letterCensus[i][0] = l;
      int x = countOccurences(arr, l);
      letterCensus[i][1] = x;
      letterCensus[i][2] = (double) x / (double) len;
    }
    double[][] table = new double[len + 1][26];
    for(int i = 0; i < table.length; i++){
      for(int j = 0, offset = 0; j < table[i].length; j++, offset++){
        if(i == 0){
          table[i][j] = alphabetFrequencies[j];
        }
        else{
          int x = (j + i) % 26;
          table[i][j] = letterCensus[x][2];
        }
      }
    }
    double[] products = new double[len];
    for(int i = 1; i < table.length; i++){
      double temp = 0;
      for(int j = 0; j < 26; j++){
        temp += table[0][j] * table[i][j];
        if(i == 2 || i == 1){
          // System.out.println("table["+i+"]["+j+"]: " + table[i][j]);
        }
      }
      products[i - 1] = temp;
    }
    return findMaxIndex(products) + 1;
  }

  public static int findMaxIndex(double[] arr){
    if(arr.length == 0){
      System.out.println("ERROR");
      return -1;
    }
    int maxIndex = 0;
    double maxSoFar = arr[0];
    for(int i = 1; i < arr.length; i++){
      // System.out.println("arr["+i+"]: " + arr[i]);
      if(arr[i] > maxSoFar){
        maxSoFar = arr[i];
        maxIndex = i;
      }
    }
    return maxIndex;
  }

  public static int countOccurences(int[] arr, int letter){
    int res = 0;
    for(int i = 0; i < arr.length; i++){
      if(arr[i] == letter){
        res++;
      }
    }
    return res;
  }

  public static void lookUpIC(){
    double min = Integer.MAX_VALUE;
    int index = 0;
    for(int i = 0; i < sampleICValues.length; i++){
      // System.out.println("sample["+i+"][1]: " + sampleICValues[i][1]);
      double diff = 0;
      diff = Math.abs(ic - sampleICValues[i][1]);
      // System.out.println("i: " + i + ", diff: " + diff + ", min: " + min + ", index: " + index);
      if(diff <= min){
        min = diff;
        index = i;
      }
    }
    keyLength = index + 1;
  }
  //
  public static void initAlphabetFrequencies(){
    alphabetFrequencies = new double[26];
    alphabetFrequencies[0] = 0.08167;  // A
    alphabetFrequencies[1] = 0.01492;  // B
    alphabetFrequencies[2] = 0.02782;  // C
    alphabetFrequencies[3] = 0.04253;  // D
    alphabetFrequencies[4] = 0.12702;  // E
    alphabetFrequencies[5] = 0.02228;  // F
    alphabetFrequencies[6] = 0.02015;  // G
    alphabetFrequencies[7] = 0.06094;  // H
    alphabetFrequencies[8] = 0.06966;  // I
    alphabetFrequencies[9] = 0.00153;  // J
    alphabetFrequencies[10] = 0.00772; // K
    alphabetFrequencies[11] = 0.04025; // L
    alphabetFrequencies[12] = 0.02406; // M
    alphabetFrequencies[13] = 0.06749; // N
    alphabetFrequencies[14] = 0.07507; // O
    alphabetFrequencies[15] = 0.01929; // P
    alphabetFrequencies[16] = 0.00095; // Q
    alphabetFrequencies[17] = 0.05987; // R
    alphabetFrequencies[18] = 0.06327; // S
    alphabetFrequencies[19] = 0.09056; // T
    alphabetFrequencies[20] = 0.02758; // U
    alphabetFrequencies[21] = 0.00978; // V
    alphabetFrequencies[22] = 0.0236;  // W
    alphabetFrequencies[23] = 0.0015;  // X
    alphabetFrequencies[24] = 0.01974; // Y
    alphabetFrequencies[25] = 0.00074; // Z
    // showAlphabetFrequencies();
  }

  public static void showAlphabetFrequencies(){
    for(int i = 0; i < alphabetFrequencies.length; i++){
        System.out.print(alphabetFrequencies[i] + " ");
    }
  }

  // public static double computeAverageIC(int len){
  //   double[] avg =
  //   for(int i = 0; i < vigenere.length; i += len){
  //
  //   }
  // }
  //
  public static void initSampleICValues(){
    sampleICValues = new double[5][2];
    for(int i = 0; i < 5; i++){
      sampleICValues[i][0] = (i + 1);
    }
    sampleICValues[0][1] = 0.066;
    sampleICValues[1][1] = 0.052;
    sampleICValues[2][1] = 0.047;
    sampleICValues[3][1] = 0.044;
    sampleICValues[4][1] = 0.043;
  }

  public static boolean loadVigenere(){
    boolean loaded = false;
    BufferedReader br;
    String message = "";
    try{
      br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      while((vigenere = br.readLine()) != null){
        // System.out.println(vigenere);
        message = vigenere;
      }
      vigenere = message;
      loaded = true;
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return loaded;
  }

  public static void getIC(){
    String line = vigenere;
    double len = line.length();
    line = line.toUpperCase();
    double temp = 0;
    for(int i = 'A'; i < 'Z'; i++){
      int ni = 0;
      for(int j = 0; j < len; j++){
        if(line.charAt(j) == i){
          ni++;
        }
      }
      temp += ni * (ni - 1);
    }
    // System.out.println(temp);
    len = len * (len - 1);
    // System.out.println(len);
    ic = temp / len;
  }
}
