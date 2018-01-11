package com.money.game.core.web.view;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Pages<T> {
	List<T> rows = new ArrayList<T>();

	public void add(T e) {
		rows.add(e);
	}

	long total;
}