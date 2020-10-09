package com.swellshinider.main;

import com.swellshinider.util.PathsAndArchives;
import com.swellshinider.util.ProgramInfos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class Cryptography {

    private static final int key = 3;

    // Converter
    @SuppressWarnings("unused")
    private static void convertVersion2_2_4_TO_2_3_0() throws IOException {
        File dirToRegisters = new File(PathsAndArchives.PATH_TO_REGISTERS);

        for(File archiveInQuestion: Objects.requireNonNull(dirToRegisters.listFiles())){

            BufferedReader reader = new BufferedReader(new FileReader(archiveInQuestion));

            String tempUsername;
            String tempEmail;
            String tempPassword;
            String tempMaster;

            try {
                String SEPARATOR = ProgramInfos.SEPARATOR;
                String[] l = reader.readLine().split(SEPARATOR);
                reader.close();

                tempUsername = decrypt(l[0]);
                tempEmail = decrypt(l[1]);
                tempPassword = decrypt(l[2]);
                tempMaster = decrypt(l[3]);

                System.out.println(tempUsername);
                System.out.println(tempEmail);
                System.out.println(tempPassword);
                System.out.println(tempMaster);
                System.out.println("===============");
            } catch (Exception ignored){
                continue;
            }

            if(archiveInQuestion.delete()){
                if(archiveInQuestion.createNewFile()){

                    BufferedWriter writer = new BufferedWriter(new FileWriter(archiveInQuestion));
                    String SEPARATOR = ProgramInfos.SEPARATOR;
                    writer.write(
                            encrypt(tempUsername) + SEPARATOR +
                                    encrypt(tempEmail) + SEPARATOR +
                                    encrypt(tempPassword) + SEPARATOR +
                                    encrypt(tempMaster));
                    writer.close();

                }
            }
        }
    }

    public static String encrypt(String text){
        try {
            byte[] encryptArray = Base64.getEncoder().encode(text.getBytes());
            String b64encrypted = new String(encryptArray, StandardCharsets.UTF_8);

            StringBuilder encryptedText = new StringBuilder();

            for(char c: b64encrypted.toCharArray()){
                c += key;
                encryptedText.append(c);
            }

            return new String(encryptedText);

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String text){
        try {
            StringBuilder decryptedCharText = new StringBuilder();

            for(char c: text.toCharArray()){
                c -= key;
                decryptedCharText.append(c);
            }

            byte[] decArray = Base64.getDecoder().decode(new String(decryptedCharText).getBytes());
            return new String(decArray, StandardCharsets.UTF_8);

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
