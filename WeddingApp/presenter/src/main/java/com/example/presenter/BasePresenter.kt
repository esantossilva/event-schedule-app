package com.example.presenter

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BasePresenter {
    internal abstract suspend fun onCreate()
    internal abstract suspend fun onStart()
    internal abstract suspend fun onStop()
    internal abstract suspend fun onDestroy()

    private val mutex = Mutex()

    fun create() = enqueueOnMain { onCreate() }
    fun start() = enqueueOnMain { onStart() }
    fun stop() = enqueueOnMain { onStop() }
    fun destroy() = enqueueOnMain { onDestroy() }

    protected fun enqueueOnMain(block: suspend () -> Unit): Job =
        GlobalScope.launch(Dispatchers.Main) {
            mutex.withLock {
                block()
            }
        }

    protected fun launchOnMain(block: suspend CoroutineScope.() -> Unit) =
        GlobalScope.launch(Dispatchers.Main, block = block)

    protected suspend fun <T> runInBackground(block: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            block()
        }
}