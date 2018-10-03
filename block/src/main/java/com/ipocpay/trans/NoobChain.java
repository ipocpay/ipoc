package com.ipocpay.trans;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import com.ipocpay.block.bean.IpocBlock;
import com.ipocpay.wallet.IpocWallet;


/**  
* @Title: NoobChain.java
* @Package com.ipocpay.trans
* @Description: IpocBlock NoobChain
* @author ipocpay@gmail.com  
* @date 2018-9-21
* @version V1.0  
*/
public class NoobChain {
    public static ArrayList<IpocBlock> blockchain = new ArrayList<IpocBlock>();
    public static HashMap<String,IpocTransactionOutput> UTXOs = new HashMap<String,IpocTransactionOutput>();

    public static int difficulty = 3;
    public static float minimumIpocTransaction = 0.1f;
    public static IpocWallet walletA;
    public static IpocWallet walletB;
    public static IpocTransaction genesisIpocTransaction;

    public static void main(String[] args) {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        walletA = new IpocWallet();
        walletB = new IpocWallet();
        IpocWallet coinbase = new IpocWallet();

        //create genesis IpocTransaction, which sends 100 NoobCoin to walletA:
        genesisIpocTransaction = new IpocTransaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisIpocTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis IpocTransaction
        genesisIpocTransaction.IpocTransactionId = "0"; //manually set the IpocTransaction id
        genesisIpocTransaction.outputs.add(new IpocTransactionOutput(genesisIpocTransaction.reciepient, genesisIpocTransaction.value, genesisIpocTransaction.IpocTransactionId)); //manually add the IpocTransactions Output
        UTXOs.put(genesisIpocTransaction.outputs.get(0).id, genesisIpocTransaction.outputs.get(0)); //its important to store our first IpocTransaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis IpocBlock... ");
        IpocBlock genesis = new IpocBlock("0");
        genesis.addIpocTransaction(genesisIpocTransaction);
        addBlock(genesis);

        //testing
        IpocBlock block1 = new IpocBlock(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addIpocTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        IpocBlock block2 = new IpocBlock(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addIpocTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        IpocBlock block3 = new IpocBlock(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addIpocTransaction(walletB.sendFunds( walletA.publicKey, 20));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        isChainValid();

    }

    public static Boolean isChainValid() {
        IpocBlock currentBlock;
        IpocBlock previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,IpocTransactionOutput> tempUTXOs = new HashMap<String,IpocTransactionOutput>(); //a temporary working list of unspent IpocTransactions at a given IpocBlock state.
        tempUTXOs.put(genesisIpocTransaction.outputs.get(0).id, genesisIpocTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This IpocBlock hasn't been mined");
                return false;
            }

            //loop thru blockchains IpocTransactions:
            IpocTransactionOutput tempOutput;
            for(int t=0; t <currentBlock.IpocTransactions.size(); t++) {
                IpocTransaction currentIpocTransaction = currentBlock.IpocTransactions.get(t);

                if(!currentIpocTransaction.verifySignature()) {
                    System.out.println("#Signature on IpocTransaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentIpocTransaction.getInputsValue() != currentIpocTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on IpocTransaction(" + t + ")");
                    return false;
                }

                for(IpocTransactionInput input: currentIpocTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.ipocTransactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on IpocTransaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input IpocTransaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.ipocTransactionOutputId);
                }

                for(IpocTransactionOutput output: currentIpocTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentIpocTransaction.outputs.get(0).reciepient != currentIpocTransaction.reciepient) {
                    System.out.println("#IpocTransaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentIpocTransaction.outputs.get(1).reciepient != currentIpocTransaction.sender) {
                    System.out.println("#IpocTransaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void addBlock(IpocBlock newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
