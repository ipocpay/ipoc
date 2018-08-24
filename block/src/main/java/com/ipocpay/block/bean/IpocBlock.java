package com.ipocpay.block.bean;

import java.util.ArrayList;
import java.util.Date;

import com.ipocpay.block.utils.StringUtil;
import com.ipocpay.trans.IpocTransaction;


/**
 * @ClassName: IpocBlock
 * @Description: ipoc bean
 * @author ipocpay@gmail.com
 * @date 2018-08-10
 *
 */
public class IpocBlock {
	public String hash;
	public String previousHash;
	public String merkleRoot;
	public ArrayList<IpocTransaction> IpocTransactions = new ArrayList<IpocTransaction>(); // our
																				// data
																				// will
																				// be
																				// a
																				// simple
																				// message.
	public long timeStamp; // as number of milliseconds since 1/1/1970.
	public int nonce;

	// Block Constructor.
	public IpocBlock(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();

		this.hash = calculateHash(); // Making sure we do this after we set the
										// other values.
	}

	// Calculate new hash based on blocks contents
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256(previousHash
				+ Long.toString(timeStamp) + Integer.toString(nonce)
				+ merkleRoot);
		return calculatedhash;
	}

	// Increases nonce value until hash target is reached.
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(IpocTransactions);
		String target = StringUtil.getDificultyString(difficulty); // Create a
																	// string
																	// with
																	// difficulty
																	// * "0"
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	// Add IpocTransactions to this block
	public boolean addIpocTransaction(IpocTransaction IpocTransaction) {
		// process IpocTransaction and check if valid, unless block is genesis block
		// then ignore.
		if (IpocTransaction == null)
			return false;
		if ((previousHash != "0")) {
			if ((IpocTransaction.processIpocTransaction() != true)) {
				System.out.println("IpocTransaction failed to process. Discarded.");
				return false;
			}
		}

		IpocTransactions.add(IpocTransaction);
		System.out.println("IpocTransaction Successfully added to Block");
		return true;
	}
}
