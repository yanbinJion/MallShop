package com.epro.comp.im.mvp.model.bean;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;
public class DbChatMessage extends LitePalSupport implements java.io.Serializable {
    @Column(unique = true, defaultValue = "unknown")
    private String uuid;
    private String msgJson;
    private String msgType;
    private String shopId;//店铺id
    private String csId;//客服Id
    private String customerId;//客户Id
    private boolean isRead=false;
    private long sentTime;
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setMsgJson(String msgJson) {
        this.msgJson = msgJson;
    }

    public String getMsgJson() {
        return msgJson;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getShopId() {
        return shopId;
    }
}
