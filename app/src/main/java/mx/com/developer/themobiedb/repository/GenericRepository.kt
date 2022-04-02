package mx.com.developer.themobiedb.repository


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

open class GenericRepository(private val dispatcher: CoroutineDispatcher): CoroutineScope {

    companion object { const val TAG = "GenericRepository" }

    override val coroutineContext: CoroutineContext
        get() =  dispatcher

}