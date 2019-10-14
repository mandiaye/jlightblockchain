package sn.galsencodeurs.sample.blockchain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private int from;
    private int to;
    private BigDecimal value;
    private String signature;
    private Type type;

    public enum Type {
        COINBASE,
        PAYE
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "from=" + from +
                ", to=" + to +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
