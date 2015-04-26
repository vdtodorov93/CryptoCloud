package system_yok_exception;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import system_yok_exception.utils.FileKeyService;
import system_yok_exception.utils.IKeyService;

public class FileKeyServiceTest {
	IKeyService keyService;

	@Before
	public void setUp() {
		keyService = new FileKeyService();
	}

	@Test
	public void getDefaultKeyTest() {
		String key = "C0BAE23DF8B51807B3E17D21925FADF2";		
		byte[] serviceKey = keyService.getDefaultKey();

		assertArrayEquals(DatatypeConverter.parseHexBinary(key), serviceKey);
	}

	@Test
	public void addKeyTest() {
		String keyName = "testKeyNedkoEpi4";
		byte[] key = new byte[] { 23, 12, 22, 54, 23 };
		keyService.addKey(keyName, key);
		byte[] readKey = keyService.getKey(keyName);
		assertArrayEquals(key, readKey);
		keyService.deleteKey(keyName);
	}
	
	@Test
	public void getKeysTest() {
		keyService.generateKey("BATENCE");
		keyService.generateKey("GAZ");
		List<String> result = keyService.getKeys();
		for(String key: result) {
			System.out.println(key);
		}
	}
}
