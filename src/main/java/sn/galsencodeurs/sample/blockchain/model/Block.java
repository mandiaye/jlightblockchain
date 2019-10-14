package sn.galsencodeurs.sample.blockchain.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Block {

    private long index;
    private Timestamp timeStamp;
    private List<Transaction> transactions;
    private String previousHash;
    private int nonce;
    private String hash;

    private static final int MAX_TRANSACTION = 500;

    public Block(long index, String previousHash) {
        this.index = index;
        this.timeStamp = Timestamp.valueOf(LocalDateTime.now());
        this.transactions = new ArrayList<>();
        this.previousHash = previousHash;
        this.hash = this.calculateHash();
    }

    public String calculateHash() {
        return DigestUtils.sha256Hex(String.valueOf(this.index) + this.timeStamp + this.transactions + this.previousHash + this.nonce);
    }

    public boolean solveProofOfWork(int difficulty) {
        this.nonce = 0;
        final String expectedPrefix = StringUtils.leftPad("", difficulty, "0");

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Block {} start to solve proof of work at {}", this.getIndex(), LocalDateTime.now());

        while (true) {
            this.hash = this.calculateHash();
            String prefix = this.hash.substring(0, difficulty);

            if (expectedPrefix.equals(prefix)) {
                watch.stop();
                log.info("Block {} success to solve proof of work ({}) at {}", this.getIndex(), prefix, LocalDateTime.now());
                log.info("resolved in {} ms", watch.getTime(TimeUnit.MILLISECONDS));
                return true;
            }
            this.nonce++;
        }
    }

    public void addTransaction(Transaction transaction) {
        if(this.transactions.size() >= MAX_TRANSACTION){
            log.error("{} not added, max transaction {} reached", transaction, MAX_TRANSACTION);
        }
        else{
            this.transactions.add(transaction);
        }
    }

    @Override
    public String toString() {
        return "Block{" +
            "index=" + index +
            ", timeStamp=" + timeStamp +
            ", transactions='" + transactions + '\'' +
            ", previousHash='" + previousHash + '\'' +
            ", nonce=" + nonce +
            ", hash='" + hash + '\'' +
            '}';
    }
}
