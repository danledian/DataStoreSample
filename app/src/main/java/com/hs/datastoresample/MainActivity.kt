package com.hs.datastoresample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hs.datastoresample.data.datastore.UserDataStore
import com.hs.datastoresample.data.datastore.UserPreferenceDataStore
import com.hs.datastoresample.data.datastore.UserProtoDataStore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val dataStores = arrayOf(UserPreferenceDataStore(
        this), UserProtoDataStore(this))

    private var userDataStore: UserDataStore? = dataStores[0]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initDataStore()
    }

    //1.0 临时写法，最好写在ViewModel里面
    private fun initDataStore(){

        GlobalScope.launch(Dispatchers.Main) {
            dataStores[0].getUsername()
            .collect {
                if(userDataStore is UserPreferenceDataStore){
                    tv_log.text = it
                }
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            dataStores[1].getUsername()
                .collect {
                    if(userDataStore is UserProtoDataStore){
                        tv_log.text = it
                    }
                }
        }

    }

    private fun initView(){
        rg_data_store_type.setOnCheckedChangeListener {
                _, checkedId ->
            userDataStore = if(checkedId == R.id.rb_preferences) dataStores[0] else dataStores[1]
            GlobalScope.launch {
                userDataStore?.getUsername()
                ?.take(1)
                ?.collect{
                    tv_log.text = it
                }
            }
        }
    }

    fun onViewClicked(view: View) {
        when(view.id){
            R.id.bt_set_username -> setUsername()
        }
    }

    //同注释1.0
    private fun setUsername() {
        GlobalScope.launch(Dispatchers.Main) {
            val username = "${userDataStore}danledian${Random().nextInt(1000)}"
            userDataStore?.setUsername(username)
            Toast
            .makeText(applicationContext, getString(R.string.set_success), Toast.LENGTH_SHORT)
            .show()
        }
    }

}