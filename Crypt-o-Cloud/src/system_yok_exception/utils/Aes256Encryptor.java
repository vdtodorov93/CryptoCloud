package system_yok_exception.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class Aes256Encryptor implements IEncryptor {
	//private static final String algName = "AES/ECB/PKCS5PADDING";
	private static final String algName = "AES";

	@Override
	public byte[] encrypt(byte[] data, byte[] aesKey) {
		Key key = generateKey(aesKey);
		byte[] result = null;
		try {
			Cipher c = Cipher.getInstance(algName);
			c.init(Cipher.ENCRYPT_MODE, key);
			result = c.doFinal(data);
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public byte[] decrypt(byte[] encryptedData, byte[] aesKey) {
		Key key = generateKey(aesKey);
		Cipher c;
		byte[] decValue = null;
		try {
			c = Cipher.getInstance(algName);
			c.init(Cipher.DECRYPT_MODE, key);

			decValue = c.doFinal(encryptedData);
		} catch (Exception e) {
			System.out.println(e);
		}
		return decValue;
	}

	public String encrypt(String Data, byte[] aesKey) {
        Key key = generateKey(aesKey);
        Cipher c;
		try {
			c = Cipher.getInstance(algName);
	        c.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encVal = c.doFinal(Data.getBytes());
	        String encryptedValue = new BASE64Encoder().encode(encVal);
	        return encryptedValue;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
    }

    public String decrypt(String encryptedData, byte[] aesKey) {
        Key key = generateKey(aesKey);
        Cipher c;
		try {
			c = Cipher.getInstance(algName);
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
	        byte[] decValue = c.doFinal(decordedValue);
	        String decryptedValue = new String(decValue);
	        return decryptedValue;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
    }

	private Key generateKey(byte[] aesKey) {
		Key key = new SecretKeySpec(aesKey, algName);
		return key;
	}

}
