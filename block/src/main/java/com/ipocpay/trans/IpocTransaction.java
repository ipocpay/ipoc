package com.ipocpay.trans;

import java.security.PublicKey;
import java.util.ArrayList;





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

  
}
