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
/* ----------|----------------------|----------------------|----------------- */
/* 21/12/2014|      chengbin.du     |                      |  add gesture featrue */
/* ----------|----------------------|----------------------|----------------- */
/* 20/01/2015|    jialiang.ren      |      PR-901767       |[Android5.0][Gallery_v5.1.1.0104.0]*/
/*           |                      |                      |No menu/back button animation      */
/* ----------|----------------------|----------------------|-----------------------------------*/
/* 15/04/2015|    qiang.ding1       |      PR-968733       | [Android5.0][Gallery_v5.1.9.1.0110.0]It will appear */
/*                                                           two prompt boxes when clicking share and delete icons at the same time*/
/* ----------|--------------------- |----------------------|----------------------------------------------*/
/* 11/05/2015 |    jialiang.ren     |      PR-994366       |[Android][Gallery_v5.1.9.1.0205.0]There */
/*                                                          exsit 'Slide show' option in video album*/
/*------------|---------------------|----------------------|----------------------------------------*/
/* 26/05/2015 |chengbin.du          |PR1008429              |[Android][Gallery_v5.1.13.1.0204.0][REG]The slideshow function disappear in locations */
/*------------|---------------------|-----------------------|----------------------------------------*/

package com.maxcard.contact.dataManager;

import java.util.ArrayList;

import com.maxcard.contact.model.CardModel;



public interface DataChangeListern {  
    //在其接口中定义一个用来增加观察者的方法  
    public void add(DataWatcher watcher);  
    //再定义一个用来删除观察者权利的方法  
    public void remove(DataWatcher watcher);  
    //再定义一个可以实现行为变现并向观察者传输信息的方法  
    public void notifyWatcher(ArrayList<CardModel> allSourceDateList);  
}


