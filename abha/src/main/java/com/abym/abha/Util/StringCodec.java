package com.abym.abha.Util;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class StringCodec {

    /**
     * RSA encrypt function (RSA / ECB / PKCS1-Padding)
     *
     * @param original
     * @param key
     * @return
     */
    public static byte[] rsaEncrypt(byte[] original, PublicKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(original);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }
        return null;
    }

    public static String getEncryptedString(String data, String publickey) {
        String base64encrypt = "";
        byte[] publicBytes = Base64.decode(publickey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = null;

            pubKey = keyFactory.generatePublic(keySpec);

            byte[] deccard = StringCodec.rsaEncrypt(data.getBytes(), pubKey);
            base64encrypt = Base64.encodeToString(deccard, Base64.DEFAULT);
            LogUtil.showErrorLog("base64encrypt",base64encrypt);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return base64encrypt.replace("\n","");
    }
}