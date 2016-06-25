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

public class ActionbarManager {
    @SuppressWarnings("unused")
    private static final String TAG = "ActionbarManager";
    private boolean mIsResumed = false;

    private Stack<Fragment> mStack = new Stack<Fragment>();
    private FragmentManager mFragmentManager = null;

    private static ActionbarManager instance = null;
    private MainCardActivity mMainCardActivity = null; 

    public ActionbarManager(){
    	
    }
    
    public static ActionbarManager getInstance() {
		if (instance == null) {
			instance = new ActionbarManager();
		}
		return instance;
    }
    
    private void setMainCardActivity(MainCardActivity mMainCardActivity){
    	this.mMainCardActivity = mMainCardActivity;
    }

}
