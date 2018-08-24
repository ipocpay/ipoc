/**  
* @Title: IpocTransactionOutput.java
* @Package com.ipocpay.trans
* @Description: TODO
* @author ipocpay@gmail.com  
* @date 2018-8-24
* @version V1.0  
*/
package com.ipocpay.trans;

import java.security.PublicKey;

import com.ipocpay.block.utils.StringUtil;



/**
 * @ClassName: IpocTransactionOutput
 * @Description: TODO
 * @author ipocpay@gmail.com
 * @date 2018-8-24
 *
 */
public class IpocTransactionOutput {
	public String id;
    public PublicKey reciepient; //also known as the new owner of these coins.
    public float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public IpocTransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }
}
