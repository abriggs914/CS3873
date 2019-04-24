/*
 CS3873 Lab 4
 Mar.29/19
 Avery Briggs
 3471065

 % I warrant that this is my own work.
 --- Avery Briggs 3471065 %


 Simple program to crack the Vigenere cypher
 on a given text file.
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class Vigenere{

  public static String vigenere;
  public static String decryptedText;
  public static String fileName;
  public static int keyLength;
  public static int[] keyShiftValues;
  // public static char[][] textBlock;
  public static double[][] sampleICValues;
  public static double[] alphabetFrequencies;

  public static void main(String[] args){
    String line = "";
    // keyPeriod = 0;
    fileName = "./VigenereCipher.txt";
    if(!loadVigenere()){
      System.out.println("Error failed to load file.");
      return;
    }
    String x = "Altd hlbe tg lrncmwxpo kpxs evl ztrsuicp qptspf. Ivplyprr th pw clhoic pozc. :-)";
    x = x.toUpperCase();
    // vigenere = x;
    initAlphabetFrequencies();
    // computeKeyLength();
    // keyLength = 3;
    double z = getIC();
    keyLength = lookUpIC(z);
    computeKeyShift();
    decryptVignere();
    System.out.println(vigenere);
    System.out.println("\tThe decrypted message is:\n" + decryptedText);
  }

  public static void decryptVignere(){
    decryptedText = "";
    for(int i = 0; i < vigenere.length(); i++){
      if(vigenere.charAt(i) > 'Z' || vigenere.charAt(i) < 'A'){
        decryptedText += Character.toString(vigenere.charAt(i));
      }
      else{
        int x = i % keyLength;
        char c = (char) ((int) vigenere.charAt(i) - keyShiftValues[x]);
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
    for(int i = 0; i < keyLength; i++){
      keyShiftValues[i] = computeShiftValue(temp[i]);
    }
    for(int i = 0; i < keyShiftValues.length; i++){
      System.out.println("keyShiftValues["+i+"] + 'A': " + (char)(keyShiftValues[i] + 'A'));
    }
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
    // for(int i = 0; i < letterCensus.length; i++){
    //   for(int j = 0; j < letterCensus[i].length; j++){
    //     System.out.println("letterCensus["+i+"]["+j+"]: " + letterCensus[i][j]);
    //   }
    // }
    double[][] table = new double[len + 1][26];
    for(int i = 0; i < table.length; i++){
      for(int j = 0, offset = 0; j < table[i].length; j++, offset++){
        if(i == 0){
          table[i][j] = alphabetFrequencies[j];
        }
        else{
          // j + y = x
          int x = (j + i) % 26;
          // while(x < 26 && x > -1){
          //   x -= 26;
          // }
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

  // public static void computeKeyLength(){
  //   int len = vigenere.length();
  //   System.out.println("len: " + len);
  //   StringBuilder sb = new StringBuilder(vigenere);
  //   int[][] table = new int[len][len + 1];
  //   for(int i = 0; i < len; i++){
  //     for(int j = i, c = 0; j < len; j++, c++){
  //       table[i][j] = sb.toString().charAt(c);
  //     }
  //     sb.deleteCharAt(sb.length() - 1);
  //   }
  //   int[] coincedences = new int[len];
  //   for(int i = 1; i < len; i++){
  //     for(int j = i, c = 0; j < len + 1; j++, c++){
  //       int target = table[0][j];
  //       if(target == table[i][j]){
  //         table[i][len]++;
  //       }
  //       if(j == len){
  //         coincedences[c] = table[i][j];
  //         System.out.println("table["+i+"]["+j+"]: " + table[i][j]);
  //       }
  //     }
  //   }
  //   keyLength = findCoincedencesPattern(coincedences);
  // }

  // public static int findCoincedencesPattern(int[] arr){
  //   if(arr.length < 2){
  //     return 1;
  //   }
  //   int res = 1;
  //   for(int i = 1; i < arr.length - 1; i++){
  //     int x = (int) res / 2;
  //     double localSum = 0;
  //     int counted = 0;
  //     for(int j = i - 1; j >= i - x; j--){
  //       localSum += arr[j];
  //       counted++;
  //     }
  //     for(int j = i + 1; j <= i + x; j++){
  //       localSum += arr[j];
  //       counted++;
  //     }
  //     localSum /= counted;
  //     // System.out.println("localSum: " + localSum);
  //     if(localSum <= arr[i]){
  //       res++;
  //       System.out.println("res: " + res);
  //     }
  //   }
  //   System.out.println("res: " + res);
  //   return res;
  // }
    // int res = 1;
    // int c = 1;
    // int lastLocalMax = arr[0];
    // for(int i = 1; i < arr.length - 1; i++){
    //   if(arr[i] > arr[i - 1] || arr[i] > arr[i + 1]){
    //     c++;
    //   }
    //   else{
    //     if(res < c){
    //       res = c;
    //     }
    //     c = 0;
    //   }
    // }


  public static int lookUpIC(double ic){
    final double TOLERANCE = 0.00225;
    System.out.println("ic: " + ic);
    for(int i = 0; i < sampleICValues.length; i++){
      System.out.println("sample["+i+"][1]: " + sampleICValues[i][1]);
      if(sampleICValues[i][1] + TOLERANCE >= ic && sampleICValues[i][1] - TOLERANCE <= ic){
        System.out.println("Returning i: " + i);
        return i + 1;
      }
    }
    System.out.println("Error got to the end of sampleICValues with no assignment.");
    return -1;
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
    showAlphabetFrequencies();
  }

  public static void showAlphabetFrequencies(){
    for(int i = 0; i < alphabetFrequencies.length; i++){
        System.out.print(alphabetFrequencies[i] + " ");
    }
  }

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

  // public static void blockText(){
  //   int cols = vigenere.length() / keyPeriod;
  //   int index = 0;
  //   textBlock = new char[keyPeriod][cols];
  //   for(int r = 0; r < keyPeriod; r++){
  //     System.out.println("r: " + r);
  //     for(int c = 0; c < cols; c++){
  //       textBlock[r][c] = vigenere.charAt(index++);
  //     }
  //   }
  //   showTextBlock();
  // }
  //
  // public static void showTextBlock(){
  //   for(int i = 0; i < textBlock.length; i++){
  //     for(int j = 0; j < textBlock[i].length; j++){
  //       System.out.print(textBlock[i][j] + " ");
  //     }
  //     System.out.println("\n");
  //   }
  // }
  //
  //
  public static boolean loadVigenere(){
    boolean loaded = false;
    BufferedReader br;
    String message = "";
    try{
      br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      while((vigenere = br.readLine()) != null){
        System.out.println(vigenere);
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

  public static double getIC(){
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
    System.out.println(temp);
    len = len * (len - 1);
    System.out.println(len);
    return temp / len;
  }
}
