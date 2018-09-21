package com.ipocpay.wallet;

/**  
* @Title: IpocWallet.java
* @Package com.ipocpay.IpocWallet
* @Description: IpocBlock Chain Wallet
* @author ipocpay@gmail.com  
* @date 2018-8-31
* @version V1.0  
*/

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ipocpay.trans.IpocTransaction;
import com.ipocpay.trans.IpocTransactionInput;
import com.ipocpay.trans.IpocTransactionOutput;
import com.ipocpay.trans.NoobChain;




/**
 * @ClassName: IpocWallet
 * @Description: IpocBlock Chain Wallet
 * @author icocpay@gmail.com
 * @date 2018-8-31
 *
 */
public class IpocWallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String,IpocTransactionOutput> UTXOs = new HashMap<String,IpocTransactionOutput>();

    public IpocWallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
    	
    	  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, IpocTransactionOutput> item: NoobChain.UTXOs.entrySet()){
            IpocTransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                total += UTXO.value ;
            }
        }
        return total;
    }

    public IpocTransaction sendFunds(PublicKey _recipient,float value ) {
        if(getBalance() < value) {
            System.out.println("#Not Enough funds to send IpocTransaction. IpocTransaction Discarded.");
            return null;
        }
        ArrayList<IpocTransactionInput> inputs = new ArrayList<IpocTransactionInput>();

        float total = 0;
        for (Map.Entry<String, IpocTransactionOutput> item: UTXOs.entrySet()){
            IpocTransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new IpocTransactionInput(UTXO.id));
            if(total > value) break;
        }

        IpocTransaction newTransaction = new IpocTransaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(IpocTransactionInput input: inputs){
            UTXOs.remove(input.ipocTransactionOutputId);
        }

        return newTransaction;
    }
    public static void main(String[] args) {
        IpocWallet IpocWallet = new IpocWallet();
        System.out.println("1----->"+IpocWallet.publicKey);
        System.out.println("2----->"+IpocWallet.privateKey);
    }
}