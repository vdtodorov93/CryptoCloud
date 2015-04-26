package system_yok_exception.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class FileKeyService implements IKeyService {

	@Override
	public byte[] getDefaultKey() {
		//String keyString = "C0BAE23DF8B51807B3E17D21925FADF273A70181E1D81B8EDE6C76A5C1F1716E";
		String keyString = "C0BAE23DF8B51807B3E17D21925FADF2";
		byte[] keyValue = DatatypeConverter.parseHexBinary(keyString);
		return keyValue;
	}

	@Override
	public byte[] getKey(String name) {
		byte[] key = null;
		try {
			key = Files.readAllBytes(Paths.get("keys/" + name));
		} catch (IOException e) {
			System.out.println(e);
		}
		return key;
	}

	@Override
	public void addKey(String name, byte[] key) {
		try {
			FileOutputStream fos = new FileOutputStream(
					new File("keys/" + name));
			fos.write(key, 0, key.length);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@Override
	public void deleteKey(String name) {
		try {
			Files.delete(Paths.get("keys/" + name));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@Override
	public List<String> getKeys() {
		List<String> result = new ArrayList<>();
		File directoryKeys = new File("keys/");
		File[] keys = directoryKeys.listFiles();
		for(File key: keys) {
			result.add(key.getName());
		}
		
		return result;
	}

	@Override
	public void generateKey(String name) {
		byte[] key = new byte[16];
		Random r = new Random();
		r.nextBytes(key);
		addKey(name, key);
	}

}
