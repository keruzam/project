package pl.keruzam.model;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bank_transaction")
public class BankTransaction {


	private Long id;

	@Column(name = "operation_date", columnDefinition = "DateTime", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date operationDate;

	@Column(name = "order_date", columnDefinition = "DateTime", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date orderDate;

	@Column(name = "note", columnDefinition = "VarChar255", nullable = false)
	private String note;

	@Column(name = "quota", columnDefinition = "Decimal", nullable = false)
	private BigDecimal quota;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_transaction_id_seq")
	@SequenceGenerator(name = "bank_transaction_id_seq", sequenceName = "bank_transaction_id_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition ="Id")
	@Access(AccessType.PROPERTY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getQuota() {
		return quota;
	}

	public void setQuota(BigDecimal quota) {
		this.quota = quota;
	}
}

