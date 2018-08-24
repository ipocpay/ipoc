/**  
* @Title: IpocTransactionInput.java
* @Package com.ipocpay.trans
* @Description: TODO
* @author ipocpay@gmail.com  
* @date 2018-8-24
* @version V1.0  
*/
package com.ipocpay.trans;



/**
 * @ClassName: IpocTransactionInput
 * @Description: TODO
 * @author ipocpay@gmail.com
 * @date 2018-8-24
 *
 */
public class IpocTransactionInput {
	 public String ipocTransactionOutputId; //Reference to TransactionOutputs -> transactionId
	    public IpocTransactionOutput UTXO; //Contains the Unspent transaction output

	    public IpocTransactionInput(String ipocTransactionOutputId) {
	        this.ipocTransactionOutputId = ipocTransactionOutputId;
	    }
}
