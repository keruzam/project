package pl.keruzam.model;

import java.math.BigDecimal;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import pl.keruzam.service.cmm.StreamSequenceGenerator;
import pl.keruzam.service.cmm.lib.Date;

@Entity
@Table(name = "bank_transaction")
public class BankTransaction {

	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "peration_date")) })
	private Date operationDate;

	@Column(name = "order_date", columnDefinition = "DateTime", nullable = false)
	private Date orderDate;

	@Column(name = "note", columnDefinition = "VarChar255", nullable = false)
	private String note;

	@Column(name = "quota", columnDefinition = "Decimal", nullable = false)
	private BigDecimal quota;

	@Id
	@GenericGenerator(
			name = "s_bank_transaction", type = StreamSequenceGenerator.class, parameters = { @org.hibernate.annotations.Parameter(
			name = "sequence_name", value = "s_bank_transaction") })
	@GeneratedValue(generator = "s_bank_transaction")
	@Column(name = "id")
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

