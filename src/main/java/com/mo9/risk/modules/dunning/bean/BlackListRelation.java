package com.mo9.risk.modules.dunning.bean;

/**
 * Created by jxli on 2017/12/4.
 */
public class BlackListRelation {
	private int num;
	private int numFromMo9;
	private int numFromThird;

	public BlackListRelation(int num, int numFromMo9, int numFromThird) {
		this.num = num;
		this.numFromMo9 = numFromMo9;
		this.numFromThird = numFromThird;
	}

	public BlackListRelation() {
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNumFromMo9() {
		return numFromMo9;
	}

	public void setNumFromMo9(int numFromMo9) {
		this.numFromMo9 = numFromMo9;
	}

	public int getNumFromThird() {
		return numFromThird;
	}

	public void setNumFromThird(int numFromThird) {
		this.numFromThird = numFromThird;
	}
}
