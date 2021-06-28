package com.test.lastexam_20151652;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    private static String digest = "";

    public Hash(String filepath) throws IOException, NoSuchAlgorithmException {
        digest = "";
        File file = new File(filepath);
        InputStream input = new FileInputStream(file.getPath());
        byte[] buffer = new byte[1024];
        MessageDigest messageDigest = MessageDigest.getInstance("md5");
        int numRead = 0;

        while(numRead != -1) {
            numRead = input.read(buffer);
            if(numRead > 0)
                messageDigest.update(buffer, 0, numRead);
        }
        input.close();

        byte[] md5Byte = messageDigest.digest();
        for(int i = 0; i < md5Byte.length; ++i){
            digest += Integer.toString((md5Byte[i] & 0xff) + 0x100, 16).substring(1);
        }
        digest.toUpperCase();
    }

    public String getDigest(){
        return digest;
    }
}
