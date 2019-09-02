# Bookish-Preference

A smart wrapper for SharedPreferences with androidx.PreferenceDataStore support

#### Example

Inject or create instance of BookishPreferenceStorage, then assign it to PreferenceManager

    @Inject
    lateinit var bookishPreferenceStorage: BookishPreferenceStorage  
        
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // inject or create
        preferenceManager.preferenceDataStore = bookishPreferenceStorage
    }
    
If you are not using Singleton pattern, make sure to always use this same name to obtain SharedPreferences

    private const val SHARED_PREFERENCE_NAME = "BookishPreferences"

    fun createBookishPreferenceStorage(appContext: Context): BookishPreferenceStorage {
       return BookishPreferenceStorage(appContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE))
    }    

It is preferred to store keys in resources, because you can bind them in preference.xml and use in code

#### preference.xml
   
      <SwitchPreference
               app:iconSpaceReserved="false"
               app:key="@string/key_auto_heat"
               app:summary="@string/auto_heat_summary"
               app:title="@string/auto_heat_title" />
    
#### class
    
    @Inject
    lateinit var bookishPreference: BookishStorage

    private val keyAutoHeat: IntKey(getString(R.string.key_auto_heat, true)) 

    private fun turnOnCoffeeMaker() {
        val autoHeat = bookishStorage.get(keyAutoHeat)
        if (autoHeat)
        // run auto heat
    } 
    
    private fun disableAutoHeat() {
        bookishStorage.put(heatOffsetKey, false) 
        // disable auto heat if coffee maker is on
    }

# License

    Copyright 2019 Jakub Tyrka

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
