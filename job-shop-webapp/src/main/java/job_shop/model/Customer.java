package job_shop.model;

public class Customer {
	private int customerId;
	private String name;
	private String address;
	private int category;

	public Customer() {

	}

	public Customer(String name, String address, int category) {
		this.name = name;
		this.address = address;
		this.category = category;
	}

	public Customer(int customerId, String name, String address, int category) {
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.category = category;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}
}
