package com.jjim4963tw.library.layout.library

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Exception
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class RetrofitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseRetrofitFunction()
        rxJavaAdapterRetrofitFunction()
        coroutineAdapterRetrofitFunction()
        setLogRetrofitFunction();
    }

    private fun baseRetrofitFunction() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(ExampleService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = service.listRepos("jjim4963tw", "private").execute()
                if (response.isSuccessful) {
                    val list = response.body()
                    Log.e("", "");
                } else {
                    // failed
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        service.listRepos("jjim4963tw", "private").enqueue(object : Callback<List<ExampleData>> {
            override fun onResponse(call: Call<List<ExampleData>>, response: Response<List<ExampleData>>) {
                if (response.isSuccessful) {
                    val list = response.body()
                    Log.e("", "");
                } else {
                    // failed
                }
            }

            override fun onFailure(call: Call<List<ExampleData>>, t: Throwable) {
                // network error handle
            }
        })
    }

    private fun rxJavaAdapterRetrofitFunction() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

        val service = retrofit.create(ExampleService::class.java)

        service.listReposUsedRxJava("jjim4963tw", "private")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // success
                    Log.e("", "");
                }, {
                    // error
                })
    }

    private fun coroutineAdapterRetrofitFunction() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(ExampleService::class.java)

        lifecycleScope.launch {
            val result = service.listReposUsedCoroutine("jjim4963tw", "private")
        }
    }

    private fun setLogRetrofitFunction() {
        val logging = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(ExampleService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = service.listRepos("jjim4963tw", "private").execute()
                if (response.isSuccessful) {
                    val list = response.body()
                    Log.e("", "");
                } else {
                    // failed
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}

data class ExampleData(
        val id: String,
        val name: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("private")
        val isPrivate: Boolean
)

interface ExampleService {
    @GET("users/{userid}/repos")
    fun listRepos(
            @Path("userid") userid: String,
            @Query("type") type: String? = null,
            @Query("sort") sort: String? = null
    ): Call<List<ExampleData>>

    @GET("users/{userid}/repos")
    fun listReposUsedRxJava(
            @Path("userid") userid: String,
            @Query("type") type: String? = null,
            @Query("sort") sort: String? = null
    ): Single<List<ExampleData>>

    @GET("users/{userid}/repos")
    suspend fun listReposUsedCoroutine(
            @Path("userid") userid: String,
            @Query("type") type: String? = null,
            @Query("sort") sort: String? = null
    ): List<ExampleData>
}

class DynamicProxy {
    interface Animal {
        fun walk()
        fun eat(food: Any)
    }

    class Dog : Animal {
        override fun walk() {
            println("dog walk")
        }

        override fun eat(food: Any) {
            println("dog ear $food")
        }
    }

    class Cat : Animal {
        override fun walk() {
            println("Cat walk")
        }

        override fun eat(food: Any) {
            println("Cat ear $food")
        }
    }

    // 從中攔截事件，並在事件前多加一個探測方法
    class AnimalProxy(private val animal: Animal) : Animal {
        override fun walk() {
            println("walk detect")
            animal.walk()
        }

        override fun eat(food: Any) {
            println("eat detect")
            animal.eat(food)
        }
    }

    // 反射：從中攔截事件，並在事件前多加一個探測方法
    class AnimalReflectionProxy : InvocationHandler {
        private var animal: Animal? = null

        fun bind(animal: Animal): Animal {
            this.animal = animal

            // 建立Proxy物件，設定模擬的型別、監聽的InvocationHandler、ClassLoader，完成後再轉型為所需物件供外部使用。
            return Proxy.newProxyInstance(animal.javaClass.classLoader, animal.javaClass.interfaces, this) as Animal
        }

        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            println("invoke method $method")
            println("invoke args $method")
            return method?.invoke(animal, *(args ?: arrayOfNulls<Any>(0)))
        }
    }


    fun main() {
        val animal = listOf(Dog(), Cat())
        animal.forEach {
            it.walk()
            it.eat(Unit)

            // 多一列 ..detect
            val proxy = AnimalProxy(it)
            proxy.walk()
            proxy.eat(Unit)

            // 反射
            val reflectionProxy = AnimalReflectionProxy().bind(it)
            reflectionProxy.walk()
            reflectionProxy.eat(Unit)
        }
    }
}