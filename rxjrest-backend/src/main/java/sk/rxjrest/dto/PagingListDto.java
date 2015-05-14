package sk.rxjrest.dto;

import java.io.Serializable;
import java.util.List;

public class PagingListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<? extends Object> items;

	private int totalPages;
	private int totalCount;

	public List<? extends Object> getItems() {
		return items;
	}

	public void setItems(List<? extends Object> items) {
		this.items = items;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
