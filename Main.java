package com.cs.haofan;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        int key_int;
        String key_str;
        String pt;
        String ct;

        //choose mode
        System.out.println("Ciphers: ");
        System.out.println("1. Caesar cipher - encrypting");
        System.out.println("2. Caesar cipher - decrypting");
        System.out.println("3. Vigenere cipher - encrypting");
        System.out.println("4. Vigenere cipher - decrypting");
        System.out.println("5. Matrix transposition cipher - encrypting");
        System.out.println("6. Matrix transposition cipher - decrypting\n");
        System.out.print("Please choose encrypting/decrypting mode: ");
        char mode = (char) System.in.read();

        //define scanner to capture keyboard typing
        Scanner sc = new Scanner(System.in);



        switch (mode){
            case '1':
                System.out.print("Plaintext: ");
                pt  = sc.next();
                System.out.print("Key: ");
                key_int  = sc.nextInt();
                System.out.println("Ciphertext: " + caesar_encrypt(pt, key_int));
                break;

            case '2':
                System.out.print("Ciphertext: ");
                ct  = sc.next();
                System.out.print("Key: ");
                key_int  = sc.nextInt();
                System.out.println("Plaintext: " + caesar_decrypt(ct, key_int));
                break;

            case '3':
                System.out.print("Plaintext: ");
                pt  = sc.next();
                System.out.print("Key: ");
                key_str  = sc.next();
                System.out.println("Ciphertext: " + vigenere_encrypt(pt, key_str));
                break;

            case '4':
                System.out.print("Ciphertext: ");
                ct  = sc.next();
                System.out.print("Key: ");
                key_str  = sc.next();
                System.out.println("Plaintext: " + vigenere_decrypt(ct, key_str));
                break;

            case '5':
                pt  = sc.nextLine();  //digest the last Enter
                System.out.print("Plaintext: ");
                pt  = sc.nextLine();
                System.out.print("Key (split with comma): ");
                key_str  = sc.next();
                System.out.println("Ciphertext: " + matrix_encrypt(pt, key_str));
                break;

            case '6':
                pt  = sc.nextLine();  //digest the last Enter
                System.out.print("Ciphertext: ");
                ct  = sc.nextLine();
                System.out.print("Key (split with comma): ");
                key_str  = sc.next();
                System.out.println("Plaintext: " + matrix_decrypt(ct, key_str));
                break;

            default:
                System.out.print("Error: please input the correct number!");
                break;

        }
    }

    private static String caesar_encrypt(String plaintext, int key){
        StringBuilder ct = new StringBuilder();

        for (int i=0; i<plaintext.length(); i++){
            ct.append((char) (plaintext.charAt(i) + key));
        }

        return ct.toString();
    }

    private static String caesar_decrypt(String ciphertext, int key){
        StringBuilder pt = new StringBuilder();

        for (int i=0; i<ciphertext.length(); i++){
            pt.append((char) (ciphertext.charAt(i) - key));
        }

        return pt.toString();
    }

    private static String vigenere_encrypt(String plaintext, String key){
        StringBuilder ct = new StringBuilder();

        int length_diff = plaintext.length() - key.length();

        if (length_diff > 0){
            for (int i = 0; i < key.length(); i++){
                if (length_diff == 0) break;
                key = String.format("%s%s", key, key.charAt(i));
                length_diff--;
                if (i == key.length()-1)i = 0;
            }
        }else if (length_diff < 0){
            key = key.substring(0, plaintext.length());
        }

        for (int i=0; i<plaintext.length(); i++){
            ct.append((char)('A' + (((int)(key.charAt(i)-'A') + (int)(plaintext.charAt(i)-'A')) % 26)));
        }

        return ct.toString();
    }

    private static String vigenere_decrypt(String ciphertext, String key){
        StringBuilder pt = new StringBuilder();

        int length_diff = ciphertext.length() - key.length();

        if (length_diff > 0){
            for (int i = 0; i < key.length(); i++){
                if (length_diff == 0) break;
                key = String.format("%s%s", key, key.charAt(i));
                length_diff--;
                if (i == key.length()-1)i = 0;
            }
        }else if (length_diff < 0){
            key = key.substring(0, ciphertext.length());
        }

        for (int i=0; i<ciphertext.length(); i++){
            pt.append((char)('A' + (((int)('Z'-key.charAt(i)) + (int)(ciphertext.charAt(i)-'A')) % 26) + 1));
        }

        return pt.toString();
    }

    private static String matrix_encrypt(String plaintext, String key){
        StringBuilder ct = new StringBuilder();

        String[] key_split = key.split(",");

        int[] real_key = new int[key_split.length];

        for (int i = 0; i < key_split.length; i++){
            real_key[i] = Integer.parseInt(key_split[i]);
        }

        plaintext = plaintext.replace(" ", "%");

        int row = (int)Math.ceil((double)plaintext.length()/(double)real_key.length);
        int column = real_key.length;
        char[][] matrix = new char[row][column];

        int index = 0;

        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                if (index < plaintext.length()){
                    matrix[i][j] = plaintext.charAt(index);
                }else {
                    matrix[i][j] = '%';
                }
                index++;
            }
        }

        for (int i=0; i<real_key.length; i++){
            for (int j=0; j<row; j++){
                ct.append(matrix[j][real_key[i]-1]);
            }
        }

        return ct.toString();
    }

    private static String matrix_decrypt(String ciphertext, String key){
        StringBuilder pt = new StringBuilder();

        String[] key_split = key.split(",");

        int[] real_key = new int[key_split.length];

        for (int i = 0; i < key_split.length; i++){
            real_key[i] = Integer.parseInt(key_split[i]);
        }

        ciphertext = ciphertext.replace("%", " ");

        int row = (int)Math.ceil((double)ciphertext.length()/(double)real_key.length);
        int column = real_key.length;
        char[][] matrix = new char[row][column];

        int index = 0;

        for (int i=0; i<real_key.length; i++){
            for (int j=0; j<row; j++){
                matrix[j][real_key[i]-1] = ciphertext.charAt(index);
                index++;
            }
        }

        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                pt.append(matrix[i][j]);
            }
        }

        return pt.toString();
    }
}
