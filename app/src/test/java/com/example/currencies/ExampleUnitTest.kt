package com.example.currencies

import kotlinx.coroutines.*
import org.junit.Test

import kotlin.coroutines.CoroutineContext

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val outerHandler = CoroutineExceptionHandler { _, exception ->
            println("OUTER Handling exception: $exception")
            println("Thread: ${Thread.currentThread().name}")
        }

        val innerHandler = CoroutineExceptionHandler { _, exception ->
            println("INNER Handling exception: $exception")
            println("Thread: ${Thread.currentThread().name}")
        }

        runBlocking {

            println("Main Thread: ${Thread.currentThread().name}")

            val outerJob = Job()
            val otherJob = CoroutineScope(outerJob + outerHandler).launch(outerHandler) {

                println("Before launch")
                println("Thread: ${Thread.currentThread().name}")

                val job = launch {
                        println("In launch")
                        println("Thread: ${Thread.currentThread().name}")
                        delay(1000)
                        println("delay completed")
                }

                println("bef delay?")
                delay(200)
                println("Cancelling!")
                job.cancel()
            }
            otherJob.join()
        }
        println("after runblocking")
    }

    @Test
    fun otherCoroutineTest() {
        var mainJob = Job()
        val scope = object: CoroutineScope {
            override val coroutineContext: CoroutineContext
                get() = mainJob
        }
        runBlocking {
            val job1 = scope.launch {
                try {
                    println("Job 1 started")
                    delay(1000)
                    println("Job 1 finished")
                } catch (e: CancellationException) {
                    println("Job 1 cancelled")
                }
            }
            delay(50)
            mainJob.cancel()
            mainJob = Job()
            val job2 = scope.launch {
                println("This is Job 2")
            }
            job2.join()
            println("Job 2 finished, is cancelled? ${job2.isCancelled}")
        }
    }


}
