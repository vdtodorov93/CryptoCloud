package system_yok_exception.crypt_o_cloud;

public class Account {
	public enum Cloud {
		DROPBOX;
	}

	private String name;
	private Cloud cloud;

	public Account() {
	}

	public Account(String name, Cloud cloud) {
		this.name = name;
		this.cloud = cloud;
	}

	@Override
	public String toString() {
		return name == null ? "" : name + "(" + cloud.toString() + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}
}
