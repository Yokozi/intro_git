package jp.co.btc.spring.user_model;

public class PaginationModel {

	// 現在のページ
	private int page;
	// 最後のページ
	private int lastPage;
	// 1ページの表示件数
	private int maxDisp;
	// 総record数
	private long resultsSum;


	public PaginationModel(int maxDisp) {
		this.page = 1;
		this.maxDisp = maxDisp;
	}

	public void setResultsSum(long resultsSum) {
		lastPage = (int) ((resultsSum % (long) maxDisp) == 0 ? resultsSum / (long) maxDisp
				: resultsSum / (long) maxDisp + 1);

		if (lastPage < page) {
			page = lastPage;
		}

		this.resultsSum = resultsSum;
	}

	public Integer getNextPage() {
		if (lastPage <= page) {
			return null;
		}
		return page + 1;
	}

	public Integer getPastPage() {
		if (page <= 1) {
			return null;
		}
		return page - 1;
	}

	public Integer getLastPage() {
		if (lastPage <= page) {
			return null;
		}
		return lastPage;
	}

	public Integer getStartPage() {
		if (page <= 1) {
			return null;
		}
		return 1;
	}

	// ?/? 表示用
	public Integer getMaxPage() {
		return lastPage;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getMaxDisp() {
		return maxDisp;
	}
}
