package com.cloud.tao.bean.etc;

import java.util.Date;

public class ChatMessage {

	private String name;
	private String msg;
	private Type type;
	private Date date;
	private String voicePath;
	private String headImageUrl;
	private float voiceSeconds;

	public enum Type {
		INCOMING, OUTCOMEING ,OUTVOICE
	}
	
	
	public ChatMessage(){}

	public ChatMessage(String name, String msg, Type type, Date date) {
		super();
		this.name = name;
		this.msg = msg;
		this.type = type;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public float getVoiceSeconds() {
		return voiceSeconds;
	}

	public void setVoiceSeconds(float voiceSeconds) {
		this.voiceSeconds = voiceSeconds;
	}

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}
}
