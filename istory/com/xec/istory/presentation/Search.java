/*
 * Copyright (C) 2006 The Android Open Source Project
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
/*
 * Revised 5/19/2010 by GORGES
 * Now supports two-dimensional view scrolling
 * http://GORGES.us
 */
 
package com.xec.istory.presentation;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
 public class Search extends Activity {

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	      //  System.out.println("s MY, earch activity triggered");
	        handleIntent(getIntent());
	   }
	    private void handleIntent(Intent intent) {

	        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            //use the query to search your data somehow
	            
	            System.out.println(query);
	        }
	    }
}