package id.ac.its.alpro.customer.component;

/**
 * Created by Luffi on 27/02/2016.
 */
public class Transaksi {
    String transferType, name, username, description, transactionDate, amount, transactionNumber, status;

    public Transaksi(String transferType, String name, String username, String description, String transactionDate, String amount, String transactionNumber, String status) {
        this.transferType = transferType;
        this.name = name;
        this.username = username;
        this.description = description;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionNumber = transactionNumber;
        this.status = status;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
