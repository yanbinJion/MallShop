package com.epro.comp.im.mvp.model.bean;


import android.support.annotation.NonNull;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.UUID;


//@Table(name = "ChatRecord")
public class ChatRecord extends LitePalSupport implements java.io.Serializable,Comparable {
    //  @PrimaryKey
    @Column(unique = true, defaultValue = "unknown")
    private String uuid = UUID.randomUUID().toString() + "";
    private String shopId;
    private String shopLogo;//店铺logo
    private String shopName;//店铺名称
    private String customerId;//客户Id
    private long unReadCount; //未读消息数
    private String msgJson;
    private RecentChatMessage recentChatMessage;//最近一条消息


    public long getObjectId() {
        return getBaseObjId();
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setUnReadCount(long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public long getUnReadCount() {
        return unReadCount;
    }

    public void setMsgJson(String msgJson) {
        this.msgJson = msgJson;
    }

    public String getMsgJson() {
        return msgJson;
    }

    public void setRecentChatMessage(RecentChatMessage recentChatMessage) {
        this.recentChatMessage = recentChatMessage;
    }

    public RecentChatMessage getRecentChatMessage() {
        return recentChatMessage;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(shopId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChatRecord) {
            return this.shopId.equals(((ChatRecord) obj).shopId);
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
