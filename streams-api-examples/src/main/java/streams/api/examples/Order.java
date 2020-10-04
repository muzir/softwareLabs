package streams.api.examples;

import java.math.BigDecimal;

public class Order {
	private final String email;
	private final Long orderId;
	private final BigDecimal amount;

	public Order(String email, Long orderId, BigDecimal amount) {
		this.email = email;
		this.orderId = orderId;
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public Long getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}
