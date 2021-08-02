package me.bytebeats.pluginify

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.loader.content.AsyncTaskLoader
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<Button>(R.id.button_execute).setOnClickListener {
//            AsyncTask.setDefaultExecutor(AsyncTask.SERIAL_EXECUTOR)
            TestAsyncTask("Main").execute(10)
        }

        view.findViewById<Button>(R.id.button_execute_async).setOnClickListener {
            object : Thread() {
                override fun run() {
                    TestAsyncTask("Child").execute(10)
                }
            }.start()
        }
    }

}

class TestAsyncTask(val tag: String) : AsyncTask<Int, String, String>() {

    override fun onPreExecute() {//called on the Thread which AsyncTask.execute(...) run on
        Log.i("AAA", "$tag: onPreExecute ${Thread.currentThread()}")
    }

    override fun doInBackground(vararg params: Int?): String {//called in background Thread
        publishProgress("Sleeping started")
        try {
            Log.i("AAA", "$tag: doInBackground 1 ${Thread.currentThread()}")
            val time = params[0]?.times(1000)
            time?.toLong()?.apply {
                Thread.sleep(this / 2)
            }
            publishProgress("Half Time")
            time?.toLong()?.apply {
                Thread.sleep(this / 2)
            }
            Log.i("AAA", "$tag: doInBackground 2 ${Thread.currentThread()}")
            return "Android was sleeping for ${params[0]} seconds"
        } catch (e: InterruptedException) {
            return e.message ?: "Unknown"
        } catch (e: Exception) {
            return e.message ?: "Unknown"
        }
    }

    override fun onPostExecute(result: String?) {//called by mHandler, which is offered by constructor, main Thread if no parameters, or the thread of Looper offered by handler.
        Log.i("AAA", "$tag: onPostExecute[${result}] ${Thread.currentThread()}")
    }

    override fun onProgressUpdate(vararg values: String?) {
        Log.i("AAA", "$tag: onProgressUpdate ${values[0]} ${Thread.currentThread()}")
    }
}