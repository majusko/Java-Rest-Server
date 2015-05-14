package sk.rxjrest.dao.transfer;

public class PerzistentCount {

	private int allCount;
	private int allPages;
	
	public PerzistentCount(int allCount, int allPages) {
		super();
		this.allCount = allCount;
		this.allPages = allPages;
	}
	
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	public int getAllPages() {
		return allPages;
	}
	public void setAllPages(int allPages) {
		this.allPages = allPages;
	}
	
	
	
}
