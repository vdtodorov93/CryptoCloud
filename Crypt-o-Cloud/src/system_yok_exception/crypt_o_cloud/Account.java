package system_yok_exception.crypt_o_cloud;

public class Account {

	private String name;
	private String cloud;

	public Account() {
	}

	public Account(String name, String cloud) {
		this.name = name;
		this.cloud = cloud;
	}

	@Override
	public String toString() {
		return name + (cloud == null ? "" : "(" + cloud + ")");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
	}
}
