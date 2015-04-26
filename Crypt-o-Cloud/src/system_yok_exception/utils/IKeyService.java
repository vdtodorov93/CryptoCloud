package system_yok_exception.utils;

import java.util.List;

public interface IKeyService {
	byte[] getDefaultKey();

	byte[] getKey(String name);

	void addKey(String name, byte[] key);
	
	List<String> getKeys();

	void deleteKey(String name);
	
	void generateKey(String name);
}
