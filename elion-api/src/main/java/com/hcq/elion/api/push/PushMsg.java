
package com.hcq.elion.api.push;


/**
 * msgId、msgType 必填
 * msgType=1 :nofication,提醒。
 * 必填:title，content。没有title，则为应用名称。
 * 非必填。nid 通知id,主要用于聚合通知。
 * content 为push  message。附加的一些业务属性，都在里边。json格式
 * msgType=2 :非通知消息。不在通知栏展示。
 * 必填：content。
 * msgType=3 :消息+提醒
 * 作为一个push消息过去。和jpush不一样。jpush的消息和提醒是分开发送的。
 */
public final class PushMsg {
    private final MsgType msgType; //type
    private String msgId; //返回使用
    private String content; //content

    public PushMsg(MsgType msgType) {
        this.msgType = msgType;
    }

    public static PushMsg build(MsgType msgType, String content) {
        PushMsg pushMessage = new PushMsg(msgType);
        pushMessage.setContent(content);
        return pushMessage;
    }

    public String getMsgId() {
        return msgId;
    }

    public int getMsgType() {
        return msgType.getValue();
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}