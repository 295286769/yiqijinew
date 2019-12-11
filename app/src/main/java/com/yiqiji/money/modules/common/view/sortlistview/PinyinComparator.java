package com.yiqiji.money.modules.common.view.sortlistview;

import java.util.Comparator;

import com.yiqiji.money.modules.common.entity.Card;

/**
 * 
 * @author xiaanming
 * 
 */
public class PinyinComparator implements Comparator<Card.Data> {

	public int compare(Card.Data o1, Card.Data o2) {
		if (o1.getLetters().equals("@") || o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#") || o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
