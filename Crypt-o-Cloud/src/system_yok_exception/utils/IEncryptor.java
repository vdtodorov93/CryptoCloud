package system_yok_exception.utils;

public interface IEncryptor {
	public byte[] encrypt(byte[] data, byte[] key);
	public byte[] decrypt(byte[] encryptedData, byte[] key);
	public String encrypt(String data, byte[] key);
	public String decrypt(String data, byte[] key);
}
