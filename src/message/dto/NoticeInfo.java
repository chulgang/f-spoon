package message.dto;

public class NoticeInfo {

	private String senderName;
	long senderNo;
	long receiverNo;
	int count;
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public long getSenderNo() {
		return senderNo;
	}
	public void setSenderNo(long senderNo) {
		this.senderNo = senderNo;
	}
	public long getReceiverNo() {
		return receiverNo;
	}
	public void setReceiverNo(long receiverNo) {
		this.receiverNo = receiverNo;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
