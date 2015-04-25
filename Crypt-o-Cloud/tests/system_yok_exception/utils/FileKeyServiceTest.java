package system_yok_exception.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FileKeyServiceTest {
	IKeyService keyService;

	@Before
	public void setUp() {
		keyService = new FileKeyService();
	}

	@Test
	public void getDefaultKeyTest() {
		String key = "C0BAE23DF8B51807B3E17D21925FADF273A70181E1D81B8EDE6C76A5C1F1716E";
		byte[] serviceKey = keyService.getDefaultKey();
		assertArrayEquals(key.getBytes(), serviceKey);
	}

	@Test
	public void addKeyTest() {
		String keyName = "testKeyNedkoEpi4";
		byte[] key = new byte[] { 23, 12, 22, 54, 23 };
		keyService.addKey(keyName, key);
		byte[] readKey = keyService.getKey(keyName);
		for (Byte b : readKey) {
			System.out.print(b);
			System.out.print(' ');
		}
		System.out.println();
		System.out.println(readKey.length);
		assertArrayEquals(key, readKey);
		keyService.deleteKey(keyName);
	}

}
