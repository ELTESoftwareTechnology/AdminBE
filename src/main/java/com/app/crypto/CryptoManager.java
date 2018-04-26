package com.app.crypto;

import com.app.security.auth.JwtUser;
import com.virgilsecurity.sdk.crypto.*;
import com.virgilsecurity.sdk.crypto.exceptions.*;
import com.virgilsecurity.sdk.utils.ConvertionUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class CryptoManager {

    private VirgilCrypto crypto = new VirgilCrypto();

    private static Map<String, VirgilPrivateKey> privateKeys = new HashMap<>();

    public static void setPrivateKey(VirgilPrivateKey key){
        JwtUser principal = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        privateKeys.put(principal.getUsername(), key);
    }

    public static VirgilPrivateKey getPrivateKey(){
        JwtUser principal = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return privateKeys.get(principal.getUsername());
    }

    public void processInputAndDecrypt(String base64EncryptedData, String privateKeyString, String publicKeyString, String password){
        try {
            byte[] encryptedData = Base64.getDecoder().decode(base64EncryptedData);
            VirgilPrivateKey privateKey = importPrivateKey(privateKeyString, null);
            VirgilPublicKey publicKey = importPublicKey(publicKeyString); // Can be used later to verify data

            String decrypted = dataDecryption(encryptedData, privateKey);
            System.out.println(String.format("Data decrypted: %s", decrypted));
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    private VirgilPrivateKey importPrivateKey(String privateKeyStr, String privateKeyPass) throws CryptoException {
        byte[] privateKeyData = ConvertionUtils.base64ToBytes(privateKeyStr);
        VirgilPrivateKey privateKey = crypto.importPrivateKey(privateKeyData, privateKeyPass);
        return privateKey;
    }

    private VirgilPublicKey importPublicKey(String publicKeyStr) throws CryptoException {
        byte[] publicKeyData = ConvertionUtils.base64ToBytes(publicKeyStr);
        VirgilPublicKey publicKey = crypto.importPublicKey(publicKeyData);
        return publicKey;
    }

    private byte[] createSignature(VirgilPrivateKey senderPrivateKey) throws SigningException {
        // prepare a message
        String messageToSign = "Hello, Bob!";
        byte[] dataToSign = ConvertionUtils.toBytes(messageToSign);

        // generate a signature
        byte[] signature = crypto.generateSignature(dataToSign, senderPrivateKey);

        return signature;
    }

    private boolean verifySignature(byte[] signature, byte[] dataToSign, VirgilPublicKey senderPublicKey)
            throws VerificationException {
        // verify a signature
        boolean verified = crypto.verifySignature(signature, dataToSign, senderPublicKey);

        return verified;
    }

    private byte[] dataEncryption(VirgilPublicKey receiverPublicKey) throws EncryptionException {
        // prepare a message
        String messageToEncrypt = "Hello, Bob!";
        byte[] dataToEncrypt = ConvertionUtils.toBytes(messageToEncrypt);

        // encrypt the message
        byte[] encryptedData = crypto.encrypt(dataToEncrypt, receiverPublicKey);

        return encryptedData;
    }

    public String dataDecryption(byte[] encryptedData, VirgilPrivateKey receiverPrivateKey)
            throws DecryptionException {
        // prepare data to be decrypted
        byte[] decryptedData = crypto.decrypt(encryptedData, receiverPrivateKey);

        // decrypt the encrypted data using a private key
        String decryptedMessage = ConvertionUtils.toString(decryptedData);

        return decryptedMessage;
    }

    private byte[] signThenEncrypt(VirgilPrivateKey senderPrivateKey, VirgilPublicKey receiverPublicKey)
            throws CryptoException {
        // prepare a message
        String messageToEncrypt = "Hello, Bob!";
        byte[] dataToEncrypt = ConvertionUtils.toBytes(messageToEncrypt);

        // use a private key to sign the message and a public key to decrypt it
        byte[] encryptedData = crypto.signThenEncrypt(dataToEncrypt, senderPrivateKey, receiverPublicKey);

        return encryptedData;
    }

    private String decryptThenVerify(byte[] encryptedData, VirgilPrivateKey receiverPrivateKey,
                                     VirgilPublicKey senderPublicKey) throws CryptoException {
        // data to be decrypted and verified
        byte[] decryptedData = crypto.decryptThenVerify(encryptedData, receiverPrivateKey,
                Arrays.asList(senderPublicKey));

        // a decrypted message
        String decryptedMessage = ConvertionUtils.toString(decryptedData);

        return decryptedMessage;
    }

    @SuppressWarnings("unused")
    private byte[] multipleEncryption() throws CryptoException {
        List<VirgilPublicKey> receiversPublicKeys = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VirgilKeyPair keyPair = crypto.generateKeys();
            receiversPublicKeys.add(keyPair.getPublicKey());
        }

        // prepare a message to be encrypted for participants
        String messageToEncrypt = "Hello, Bob!";
        byte[] dataToEncrypt = ConvertionUtils.toBytes(messageToEncrypt);

        // encrypt the message
        byte[] encryptedData = crypto.encrypt(dataToEncrypt, receiversPublicKeys);

        return encryptedData;
    }

}
