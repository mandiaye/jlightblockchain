package sn.galsencodeurs.sample.blockchain.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.galsencodeurs.sample.blockchain.service.BlockChain;

@SpringBootApplication
@ComponentScan(basePackages = "sn.galsencodeurs.sample.blockchain.service")
@RequiredArgsConstructor
@Slf4j
public class BlockchainApplication implements CommandLineRunner {

	private final BlockChain blockChain;


	public static void main(String[] args) {
		SpringApplication.run(BlockchainApplication.class, args);
	}

	@Override
	public void run(String... args) {
		blockChain.startSimulation();
	}
}
