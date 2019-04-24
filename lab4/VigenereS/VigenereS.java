//package com.testing;

import java.util.Scanner;

/**
 * This program asks the user to enter a message to encrypt and a keyword. Based
 * on these it will then use a CipherKey and a VigenereSquare. These are then
 * used to encrypt the message using a VigenereCipherEncrypter.
 *
 * Decryption is also performed using a VigenereCipherDecrypter.
 */
public class VigenereS {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter message to encrypt (A-Z characters only): ");
        String message = in.nextLine().toLowerCase();

        System.out.println("Enter the keyword: ");
        String keyword = in.nextLine().toLowerCase();

        CipherKey cipherKey = new CipherKey(message, keyword);

        String cipherText = VigenereCipherEncrypter.encrypt(message, cipherKey);
        System.out.println("Encrypted message: " + cipherText);

        String decryptedMessage = VigenereCipherDecrypter.decrypt(cipherText,
                cipherKey);
        System.out.println("Decrypted message: " + decryptedMessage);
    }
}
