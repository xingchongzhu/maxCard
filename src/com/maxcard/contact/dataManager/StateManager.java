/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* ----------|---------------------|----------------------|-------------------------------------------*/
/* 15/04/2015|    qiang.ding1       |      PR-968733       | [Android5.0][Gallery_v5.1.9.1.0110.0]It will appear */
/*                                                           two prompt boxes when clicking share and delete icons at the same time*/
/* ----------|--------------------- |----------------------|----------------------------------------------*/
/* 12/10/2015 |dongliang.feng       |PR1097061               |[Monkey][Crash] com.tct.gallery3d */
/*------------|---------------------|------------------------|---------------------------------*/

package com.maxcard.contact.dataManager;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import java.util.Stack;

import com.maxcard.contact.R;
import com.maxcard.contact.app.MainCardActivity;
import com.maxcard.contact.fragment.AllCardfragment;
import com.maxcard.contact.fragment.LocalTemplateCardFragment;
import com.maxcard.contact.fragment.MyCardFragment;
import com.maxcard.contact.fragment.OnlineTemplateCardFragment;

public class StateManager {
    @SuppressWarnings("unused")
    private static final String TAG = "StateManager";
    private boolean mIsResumed = false;

    private Stack<Fragment> mStack = new Stack<Fragment>();
    private FragmentManager mFragmentManager = null;

    private static StateManager instance = null;
    private MainCardActivity mMainCardActivity = null; 

    public StateManager(){
    	
    }
    
    public static StateManager getInstance() {
		if (instance == null) {
			instance = new StateManager();
		}
		return instance;
    }

    public void setMainCardActivity(MainCardActivity mMainCardActivity){
    	this.mMainCardActivity = mMainCardActivity;
    }

    public int getFragementCount(){
    	if(mStack == null){
    		return 0;
    	}
    	return mStack.size();
    }
    
    public void startState(Fragment mFragment){
    	if (mFragmentManager == null && mMainCardActivity != null) {
			mFragmentManager = mMainCardActivity.getFragmentManager();
		}
    	FragmentTransaction ft = mFragmentManager.beginTransaction();
    	for(int i = 0;i < mStack.size();i++){
    		Fragment tmp= mStack.get(i);
    		if(tmp instanceof AllCardfragment || tmp instanceof LocalTemplateCardFragment ||
        			tmp instanceof OnlineTemplateCardFragment || tmp instanceof MyCardFragment){
    			continue;
        	}
    		ft.remove(tmp);
			tmp.onDestroy();
    	}
    	ft.commitAllowingStateLoss();
    	mStack.clear();
    	mStack.push(mFragment);
    	showFragment();
    }

    public void showFragment(){
    	for(int i = 0;i < mStack.size();i++){
    		Fragment tmp = mStack.get(i);
    		Log.d(TAG, " showFragment tmp"+i+" = "+tmp);
    	}
    }
    public void startStateForResult(Fragment mFragment) {
    	hideFragment();
    	mStack.push(mFragment);
        showFragment(mStack.peek());
    }

    private void hideFragment() {
    	Log.d(TAG, " hideFragment size = "+mStack.size());
		if (mFragmentManager == null && mMainCardActivity != null) {
			mFragmentManager = mMainCardActivity.getFragmentManager();
		}
		if(mFragmentManager != null){
			FragmentTransaction ft = mFragmentManager.beginTransaction();
				for (int i = 0; i < mStack.size(); i++) {
					Log.d(TAG, " hideFragment hide = "+mStack.get(i));
					ft.hide(mStack.get(i));
				}
			ft.commitAllowingStateLoss();
		}
	}
    private void showFragment(Fragment mFragment){
    	
    	if (mFragmentManager == null && mMainCardActivity != null) {
			mFragmentManager = mMainCardActivity.getFragmentManager();
		}
    	if(mFragmentManager != null){
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			ft.add(R.id.content_frame, mFragment);
			ft.show(mFragment).commitAllowingStateLoss();
			Log.d(TAG, " showFragment mFragment = "+mFragment);
		}
    }

    public void resume() {
        if (mIsResumed) return;
        mIsResumed = true;

    }

    public void pause() {
        if (!mIsResumed) return;
        mIsResumed = false;
    }

    public int getStateCount() {
        return mStack.size();
    }

    public Fragment getCurrentFragment(){
    	if(mStack != null && !mStack.empty()){
    		return mStack.peek();
    	}
    	return null;
    }
    public boolean onBackPressed() {
    	showFragment();
		// TODO Auto-generated method stub
    	if(!mStack.isEmpty()){
	    	Fragment tmp = mStack.pop();
	    	if(tmp instanceof AllCardfragment || tmp instanceof LocalTemplateCardFragment ||
	    			tmp instanceof OnlineTemplateCardFragment || tmp instanceof MyCardFragment){
	    		return true;
	    	}
	    	FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			transaction.remove(tmp);
			tmp.onDestroy();
	    	if(!mStack.isEmpty()){
	    		tmp = mStack.peek();
	    		transaction.show(tmp);
	    	}
	    	transaction.commitAllowingStateLoss();
	    	return false;
    	}
    	return true;
	}

    public void destroy() {
        Log.v(TAG, "destroy");
        while (!mStack.isEmpty()) {
            mStack.pop().onDestroy();
        }
        if(mStack != null)
        	mStack.clear();
        mFragmentManager = null;
        instance = null;
    }

}
