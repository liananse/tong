package com.mobilepower.tong.ui.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentsAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentsList;

	private FragmentManager mFragmentManager;

	public FragmentsAdapter(FragmentManager fm) {
		super(fm);
	}

	public FragmentsAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.mFragmentManager = fm;
		this.fragmentsList = fragments;
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	public void removeAllFragments() {
		Iterator localIterator = this.fragmentsList.iterator();
		while (localIterator.hasNext()) {
			Fragment localAccessibilityFragment = (Fragment) localIterator
					.next();
			this.mFragmentManager.beginTransaction()
					.remove(localAccessibilityFragment).commit();
		}
	}

}
