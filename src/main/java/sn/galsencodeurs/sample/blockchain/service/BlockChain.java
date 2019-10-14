package sn.galsencodeurs.sample.blockchain.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sn.galsencodeurs.sample.blockchain.model.Block;
import sn.galsencodeurs.sample.blockchain.model.Transaction;
import sn.galsencodeurs.sample.blockchain.model.User;

@Service
@Slf4j
public class BlockChain {

    private final Block genesisBlock;
    private final UserService userService;
    private final int difficulty;
    private List<Block> blocks;

    public BlockChain(UserService userService, @Value("${bockchain.difficulty:4}") int difficulty) {
        this.userService = userService;
        blocks = new LinkedList<>();
        this.createGenesisBlock();
        genesisBlock = this.blocks.get(0);
        this.difficulty = difficulty;
    }

    public void startSimulation() {
        User from = new User("thierno@blockchain.com", "thier@me.fr", BigDecimal.valueOf(100000), "transfer");
        User to = new User("mandiaye@blockchain.com", "mandiaye@me.fr", BigDecimal.valueOf(10000), "receiver");

        userService.addUser(from);
        userService.addUser(to);

        userService.print();

        Transaction transaction = new Transaction(from.getId(), to.getId(), BigDecimal.valueOf(10000), "signed by thier", Transaction.Type.COINBASE);
        Block nextBlock = this.generateNextBlock(transaction);
        nextBlock.solveProofOfWork(difficulty);
        blocks.add(nextBlock);
        this.finalizeTransaction(transaction);

        log.info("Block Chain blocks \n\t");
        blocks.forEach(block -> log.info(" ---> {}\n\t ", block));

        userService.print();

    }

    private void createGenesisBlock() {
        User from = new User("satoshi@blockchain.com", "me@me.fr", BigDecimal.valueOf(100000), "transfer");
        User to = new User("me@blockchain.com", "me@me.fr", BigDecimal.valueOf(10000), "receiver");

        userService.addUser(from);
        userService.addUser(to);

        Transaction firstTransaction = new Transaction(from.getId(), to.getId(), BigDecimal.valueOf(1000), "signed by satoshi", Transaction.Type.COINBASE);
        Block firstBlock = new Block(0, null);
        firstBlock.addTransaction(firstTransaction);
        firstBlock.solveProofOfWork(difficulty);

        this.blocks.add(firstBlock);
        this.finalizeTransaction(firstTransaction);
    }

    private void finalizeTransaction(Transaction transaction){
        userService.getUsers().stream().filter(user ->user.getId() == transaction.getFrom()).findFirst().ifPresent( user -> user.setSolde(user.getSolde().subtract(transaction.getValue())));
        userService.getUsers().stream().filter(user ->user.getId() == transaction.getTo()).findFirst().ifPresent( user -> user.setSolde(user.getSolde().add(transaction.getValue())));

    }

    private Block generateNextBlock(Transaction transaction) {
        if(isValidTransaction(transaction.getFrom(), transaction.getTo())) {
            final Block previousBlock = this.getLatestBlock();

            long nextIndex = previousBlock.getIndex() + 1;
            Block block = new Block(nextIndex, previousBlock.getHash());

            block.addTransaction(transaction);
            return block;
        }
        else {
            log.error("Invalid transaction, user id:{} or/and user id:{} doesn't exist", transaction.getFrom(), transaction.getTo() );
            throw new IllegalArgumentException("Invalid transaction " + transaction);
        }

    }

    private Block getLatestBlock() {
        return this.blocks.get(this.blocks.size() - 1);
    }

    private boolean isValidNewBlock(Block newBlock, Block previousBlock) {
        if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
            return false;
        }
        if (!previousBlock.getHash().equals(newBlock.getPreviousHash())) {
            log.error("Invalid previous hash for block {}", newBlock);
            return false;
        }
        final String hash = newBlock.calculateHash();
        if (!hash.equals(newBlock.getHash())) {
            log.error("Invalid hash: {} != {}" + hash, newBlock.getHash());
            return false;
        }
        return true;
    }

    private boolean isValidGenesisBlock() {
        if (this.genesisBlock.getIndex() != 0) {
            log.error("Invalid genesis index {}", genesisBlock.getIndex());
            return false;
        }
        if (Objects.nonNull(genesisBlock.getPreviousHash())) {
            log.error("Invalid previous hash {}", genesisBlock.getPreviousHash());
            return false;
        }
        final String hash = genesisBlock.calculateHash();
        if (!hash.equals(genesisBlock.getHash())) {
            log.error("Invalid hash {} != {}", hash, genesisBlock.getHash());

        }

        return true;
    }

    private boolean isValidTransaction(int from, int to){
        return userService.getUsers().stream().filter(user -> { return user.getId() == from || user.getId() == to;}).count() == 2;
    }


    private boolean isValidChain() {
        if (!this.isValidGenesisBlock()) {
            return false;
        }


        for (int i = 1; i < this.blocks.size(); i++) {
            if (!this.isValidNewBlock(this.blocks.get(i), this.blocks.get(i - 1))) {
                return false;
            }
        }
        return true;
    }

}
