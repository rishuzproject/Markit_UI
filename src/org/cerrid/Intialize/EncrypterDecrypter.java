/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cerrid.Intialize;

/**
 *
 * @author Rishabh Prashar
 */
import com.qwest.esec.crypto.BasicEncryptionException;
import com.qwest.esec.crypto.BasicEncryptor;
import com.qwest.esec.crypto.CipherText;
import com.qwest.esec.exception.BasicRuntimeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.NoSuchPaddingException;

public class EncrypterDecrypter {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        EncrypterDecrypter passEnc = new EncrypterDecrypter();
        String secretData = "cerrid";
        String passwordEnc = passEnc.encryptData(secretData);
        String passwordDec = passEnc.decryptData("rO0ABXNyACBjb20ucXdlc3QuZXNlYy5jcnlwdG8uQ2lwaGVyVGV4dAAAAAABMfRXAgAJSQAHa2V5c2l6ZUwADGFsZ19wcm92aWRlcnQAEkxqYXZhL2xhbmcvU3RyaW5nO0wABWNfYWxncQB+AAFbAA1lbmNyeXB0ZWRUZXh0dAACW0JbABRpbml0aWFsaXphdGlvblZlY3RvcnEAfgACTAAHa2V5bmFtZXEAfgABTAAGbWRfYWxncQB+AAFbABRtZXNzYWdlSW50ZWdyaXR5Q29kZXEAfgACWwAFbm9uY2VxAH4AAnhwAAAAEHQABlN1bkpDRXQAFEFFUy9DQkMvUEtDUzVQYWRkaW5ndXIAAltCrPMX+AYIVOACAAB4cAAAABAVi4ZeE1KtprV0ibVyVyEDdXEAfgAGAAAAEKcAVPd88L6PEuxjmMBIzbtwdAAFU0hBLTF1cQB+AAYAAAAUuFfrwX5nZn9Wwnq9xdNcXdokWxR1cQB+AAYAAAAQyVUrGlq1xbFXO3TcoGqxUQ==");

        //  System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);

    }

    /*public String encrypt(String Data) throws Exception {
     Key key = generateKey();
     Cipher c = Cipher.getInstance(ALGO);
     c.init(Cipher.ENCRYPT_MODE, key);
     byte[] encVal = c.doFinal(Data.getBytes());
     String encryptedValue = new BASE64Encoder().encode(encVal);
     return encryptedValue;
     }
	
     public String decrypt(String encryptedData) throws Exception {
     Key key = generateKey();
     Cipher c = Cipher.getInstance(ALGO);
     c.init(Cipher.DECRYPT_MODE, key);
     byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
     byte[] decValue = c.doFinal(decordedValue);
     String decryptedValue = new String(decValue);
     return decryptedValue;
     }*/
    public String encryptData(String secretData) throws Exception {

        BasicEncryptor encryptor = null;
        String secretKey = "my$3cr3tD0ntT3EL";

        try {
            encryptor = new BasicEncryptor("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }

        CipherText cTxt = null;
        String encData = "";

        try {
            /* 
             * Encrypt the data. The passed items are: The data to be 
             * encrypted, the key to use for encryption, and the key size.
             *
             * NOTE 1: the key must be used by both the encryption side and the
             * decryption side. Therefore, both sides must share the key. It
             * cannot be stressed too much that this key must be kept secret.
             * It should not be shared between the two sides in an unsafe 
             * manner, and it should be stored securely. Hard-coding it in 
             * the program - as was done here - is not recommended.
             *
             * NOTE 2: The encrypt method is overloaded, so you have many
             * choices as to how to pass the data. Only one is shown here.
             */
            cTxt = encryptor.encrypt(secretData.toCharArray(),
                    secretKey.toCharArray(),
                    secretKey.length() * 8);

            /*
             * Since the data is being store (and displayed) convert it to
             * a Base64 encoded string. If this is not done non-printable
             * characters in the encrypted string could cause problems when
             * trying to display. Also, on the decrypting side, to 
             * re-constitute the CipherText, one of the constructors takes
             * a Base64 encoded string, and is easier than trying to use the
             * other constructors.
             */
            encData = cTxt.toEncodedString();

            /*
             * Display the document just to show the encrypted data. It will
             * also be put into a file (below) and can be viewed there.
             */
            /* System.out.println("The data after encryption (this is the " +
             "entire CipherText object):\n");
             System.out.println(encData);
             System.out.println("\n");*/
        } catch (BasicEncryptionException | InvalidKeyException | IOException ex) {
            System.out.println("Caught exception, message: " + ex.getMessage());
            throw ex;
        }

        return encData;
    }

    public String decryptData(String encryptedData) throws Exception {

        BasicEncryptor decryptor = null;
        /*
         * This is the secret key used to encrypt / decrypt the data. It must
         * be the same as that used on the encryption side. 
         */
        String secretKey = "my$3cr3tD0ntT3EL"; // AES 128 bit key
        CipherText cTxt = null;
        String clearTxt = "";

        /*
         * Re-constitute the CipherText object. Then instantiate the decryptor 
         * and then decrypt the encrypted data in the CipherText.
         */
        try {
            cTxt = new CipherText(encryptedData);

            decryptor = new BasicEncryptor(cTxt);
            clearTxt = decryptor.decrypt(cTxt, secretKey.toCharArray(),
                    secretKey.length() * 8);
        } catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | BasicEncryptionException | BasicRuntimeException | KeyException e) {
            throw e;
        }

        return clearTxt;
    }

}
