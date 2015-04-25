package system_yok_exception.utils;

public interface IKeyService {
	byte[] getDefaultKey();

	byte[] getKey(String name);

	void addKey(String name, byte[] key);

	void deleteKey(String name);
}
