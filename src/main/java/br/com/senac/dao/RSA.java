package br.com.senac.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
 
public class RSA {
  
  public static final String ALGORITHM = "RSA"; 
	/* Chave privada */
  public static final String PATH_PRIVATE_KEY = "C:/chaves/private_lucas.key";
 
	/* Chave publica */
  public static final String PATH_PUBLIC_KEY = "C:/chaves/public_lucas.key";
 
  public static void generateKey() {
    try {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(1024);
        final KeyPair keys = keyGen.generateKeyPair();

        File privateKeyFile = new File(PATH_PRIVATE_KEY);
        File publickeyFile = new File(PATH_PUBLIC_KEY);

        
        if (privateKeyFile.getParentFile() != null) {
          privateKeyFile.getParentFile().mkdirs();
        }

        privateKeyFile.createNewFile();

        if (publickeyFile.getParentFile() != null) {
          publickeyFile.getParentFile().mkdirs();
        }

        publickeyFile.createNewFile();

        // Save public key content into a File
        ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publickeyFile));
        publicKeyOS.writeObject(keys.getPublic());
        publicKeyOS.close();

        // Save private key content into a File
        ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
        privateKeyOS.writeObject(keys.getPrivate());
        privateKeyOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  
  }
  
  public static boolean thereIsKeyPaarConfigured() {
  
    File privateKey = new File(PATH_PRIVATE_KEY);
    File publicKey = new File(PATH_PUBLIC_KEY);
  
    if (privateKey.exists() && publicKey.exists()) {
      return true;
    }
     
    return false;
  }
  
  public static byte[] encrypt(byte[] content, PublicKey key) {
    byte[] cipherText = null;
     
    try {
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(content);
    } catch (Exception e) {
      e.printStackTrace();
    }
     
    return cipherText;
  }
  
  public static byte[] decrypt(byte[] content, PrivateKey key) {
    byte[] dectyptedText = null;
     
    try {
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(content);
  
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  
    return dectyptedText;
  }
  public static void main(String[] args) { 
    try {
      if (!thereIsKeyPaarConfigured()) {
        generateKey();
      }
  
      final String message = "Courage doesn???t mean you don???t have afraid. It means you don???t let fear stop you.";
      ObjectInputStream inputStream = null;
  
      // Encrypt using the public key
      inputStream = new ObjectInputStream(new FileInputStream(PATH_PUBLIC_KEY));
      final PublicKey publicKey = (PublicKey) inputStream.readObject();
      final byte[] contentEncrypted = encrypt(message.getBytes(), publicKey);
  
      // decrypt using the private key
      inputStream = new ObjectInputStream(new FileInputStream(PATH_PRIVATE_KEY));
      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
       byte[] originalContent= decrypt(contentEncrypted, privateKey);
       final String plainText= new String(originalContent);

      System.out.println("Original: " + message);
      System.out.println("Encrypted: " +new String(contentEncrypted));
      System.out.println("Decrypted: " + plainText);
  
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}