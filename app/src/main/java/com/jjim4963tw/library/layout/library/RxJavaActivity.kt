package com.jjim4963tw.library.layout.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

class RxJavaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val observable = this.createCustomObservable()
        val observableUseArray = this.createObservableUsedFromArray()
        val observableUseJust = this.createObservableUsedJust()

        // 可透過此停止subscribe
        var disposable: Disposable? = null
        disposable?.dispose()

        // 控管多個Observer，並可一次停止所有subscribe
        val compositeDisposable = CompositeDisposable()
        observable.subscribe().also(compositeDisposable::add)
        compositeDisposable.dispose()

        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onNext(t: String) {
                TODO("Not yet implemented")
            }

            override fun onComplete() {
                TODO("Not yet implemented")
            }
        }

        observable.subscribe()
        observable.subscribe(observer)
        observable.subscribe {
            // onNext()
        }
        observable.subscribe({
            // onNext()
        }, {
            // onError()
        })
        observable.subscribe({
            // onNext()
        }, {
            // onError()
        }, {
            // onComplete()
        })

        // result = 2、6
        Observable
                .just(1, 2, 3)
                .filter { it % 2 == 1 }
                .map { it * 2 }
                .subscribe {
                    println(it)
                }
    }

    private fun createObservableUsedJust(): Observable<String> {
        return Observable.just("test1", "test2", "test3")
    }

    private fun createObservableUsedFromArray(): Observable<List<String>> {
        return Observable.fromArray(listOf("test1", "test2", "test3"))
    }

    private fun createCustomObservable(): Observable<String> {
        return Observable.create<String> {
            try {
                it.onNext("test1")
                it.onNext("test2")
                it.onNext("test3")
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
            it.onComplete()
        }
    }

    private fun doSubjectFunction() {
        val errorSubject = PublishSubject.create<Unit>()

        // 例子假設為多個API，可用Subject統一處理錯誤
        errorSubject.onNext(Unit)

        // 監聽錯誤
        errorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // do error handle
                }
    }
}