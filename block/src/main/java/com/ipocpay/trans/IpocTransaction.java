package com.ipocpay.trans;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import com.ipocpay.block.utils.StringUtil;






/**
 * @ClassName: IpocTransaction
 * @Description: TODO
 * @author icocpay@gmail.com
 * @date 2018-10-03
 *
 */
public class IpocTransaction {

    public String IpocTransactionId; // hash of transaction 
    public PublicKey sender; //receiver wallet address
    public PublicKey reciepient; //receiver wallet address
    public float value; //ipoc price or num
    public byte[] signature; //    set ipoc signature

    public ArrayList<IpocTransactionInput> inputs = new ArrayList<IpocTransactionInput>();// transaction input
    public ArrayList<IpocTransactionOutput> outputs = new ArrayList<IpocTransactionOutput>(); // transaction output
   

    // Constructor:
    public IpocTransaction(PublicKey from, PublicKey to, float value,  ArrayList<IpocTransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = com.ipocpay.block.utils.StringUtil.getStringFromKey(sender) + com.ipocpay.block.utils.StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);//gernerate siganature
    }
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);//validate siganature
    }
    
    public float getInputsValue() {
        float total = 0;
        for(IpocTransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if IpocTransaction can't be found skip it, This behavior may not be optimal.
            total += i.UTXO.value;
        }
        return total;
    }
}
