package fr.epf.moov.service

import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Adapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pixplicity.sharp.Sharp
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream


fun AppCompatActivity.fetchSvg(context: Context, url : String, imageView: ImageView) {
    val client = OkHttpClient()

    var request: Request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("ERREUR", "erreur image")
        }

        override fun onResponse(call: Call, response: Response) {
            val stream: InputStream? = response.body?.byteStream()
            Sharp.loadInputStream(stream).into(imageView);
            stream?.close()
        }
    })
}

    fun AppCompatActivity.retrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api-ratp.pierre-grimaud.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
fun Fragment.retrofit(): Retrofit {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api-ratp.pierre-grimaud.fr/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun Adapter.retrofit(): Retrofit {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api-ratp.pierre-grimaud.fr/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}



class ItemClickSupport private constructor(
    private val mRecyclerView: RecyclerView,
    private val mItemID: Int
) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        if (mOnItemClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v!!)
            mOnItemClickListener!!.onItemClicked(mRecyclerView, holder.adapterPosition, v)
        }
    }
    private val mOnLongClickListener = OnLongClickListener { v ->
            if (mOnItemLongClickListener != null) {
                val holder = mRecyclerView.getChildViewHolder(v)
                return@OnLongClickListener mOnItemLongClickListener!!.onItemLongClicked(
                    mRecyclerView,
                    holder.adapterPosition,
                    v
                )
            }
            false
        }
    private val mAttachListener: OnChildAttachStateChangeListener = object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                if (mOnItemClickListener != null) {
                    view.setOnClickListener(mOnClickListener)
                }
                if (mOnItemLongClickListener != null) {
                    view.setOnLongClickListener(mOnLongClickListener)
                }
            }

             override fun onChildViewDetachedFromWindow(view: View) {}
        }

    fun setOnItemClickListener(listener: OnItemClickListener?): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?): ItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(mItemID, null)
    }

    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView?, position: Int, v: View?)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(
            recyclerView: RecyclerView?,
            position: Int,
            v: View?
        ): Boolean
    }

    companion object {
        fun addTo(view: RecyclerView, itemID: Int): ItemClickSupport {
            var support = view.getTag(itemID) as ItemClickSupport
            if (support == null) {
                support = ItemClickSupport(view, itemID)
            }
            return support
        }

        fun removeFrom(view: RecyclerView, itemID: Int): ItemClickSupport? {
            val support = view.getTag(itemID) as ItemClickSupport
            support?.detach(view)
            return support
        }
    }

    init {
        mRecyclerView.setTag(mItemID, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }
}







