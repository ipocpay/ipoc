/**  
* @Title: JsonUtil.java
* @Package com.ipocpay.block.utils
* @Description: 
* @author  ipocpay@gmail.com
* @version V1.0  
*/
package com.ipocpay.block.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @ClassName: JsonUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ipocpay@gmail.com
 *
 */
public class JsonUtil {
    public static String toJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
