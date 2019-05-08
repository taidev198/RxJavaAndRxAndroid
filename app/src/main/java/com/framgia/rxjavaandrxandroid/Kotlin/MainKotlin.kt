package com.framgia.rxjavaandrxandroid.Kotlin


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.framgia.rxjavaandrxandroid.R
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

/**
 * Created by superme198 on 08,May,2019
 * in RxJavaAndRxAndroid.
 *
 */
class MainKotlin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val observable: Observable<String> = Observable.just("Hello")
//        observable
//                .take(5)
//                .subscribe(object : Observer<String> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onNext(t: String) {
//                        Toast.makeText(applicationContext, t, Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onError(e: Throwable) {
//                    }
//
//                })
//
//        //maybe
//        val maybe: Maybe<List<Int>> = Maybe.just(arrayListOf(1, 2, 3, 4, 5))
//
//        val observable1: Observable<String> = Observable.create { emitter ->
//            emitter.onNext("1")
//        }
//        observable1.take(2)
////                .subscribe { t: String? -> Toast.makeText(applicationContext, t, Toast.LENGTH_LONG).show() }
//                .subscribe(object : Observer<String> {
//                    //implement subscribe
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        Toast.makeText(applicationContext, "subscribe", Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onNext(t: String) {
//                        Toast.makeText(applicationContext, t, Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onError(e: Throwable) {
//                    }
//                })


        val textEdit: Observable<String> = Observable.create { emitter ->
            text_input.editText!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    process_loading.visibility = (View.INVISIBLE)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //process_loading.visibility = (View.VISIBLE)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }

            })
        }

        textEdit
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1000, TimeUnit.MILLISECONDS)
                .filter { filter -> filter.isNotEmpty() }
//                .subscribe({ text ->
//                    text_output.text = "Output : " + text
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({ showProgress() })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        process_loading.visibility = (View.INVISIBLE)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: String) {
                        hideProgress()
                        text_output.text = t


                    }

                    override fun onError(e: Throwable) {
                    }

                })

        arrayListOf<Int>(1, 2, 3, 4).toFlowable()
                .filter({ it -> it.absoluteValue > 3 })
                .subscribe({ t -> Toast.makeText(applicationContext, "got: " + t, Toast.LENGTH_LONG).show() })

    }

    private fun showProgress() {
        process_loading.visibility = (View.VISIBLE)
    }

    private fun hideProgress() {
        process_loading.visibility = (View.INVISIBLE)
    }

    private fun showResults(results: List<String>) {

    }

}