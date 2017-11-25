package com.mo9.risk.modules.dunning.bean;

import java.util.List;

/**
 * CTI分页数据响应
 */
public class CallCenterPageResponse<T> extends CallCenterBaseResponse{

	private static final long serialVersionUID = 1L;

	private CallCenterPageData<T> data;

	public CallCenterPageData<T> getData() {
		return data;
	}

	public void setData(CallCenterPageData<T> data) {
		this.data = data;
	}

    /**
     * This is equivalent to
     * <pre>
     *   this.data != 0 && !page.isEmpty()</pre>
     */
	public boolean hasData() {
		if (data == null) {
			return false;
		}
		return !data.isEmpty();
	}

	public static class CallCenterPageData<K> {
		private int total;	//总记录数

		private int page;	//当前页数

		private int pageSize;	//每页显示条数

		private List<K> results;

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public List<K> getResults() {
			return results;
		}

		public void setResults(List<K> results) {
			this.results = results;
		}

	    /**
	     * This is equivalent to
	     * <pre>
	     *   page.getTotal == 0 || page.getResults == null || page.getResults.isEmpty()</pre>
	     */
		public boolean isEmpty() {
			return this.total == 0 || this.results == null || this.results.isEmpty();
		}

	}
}
