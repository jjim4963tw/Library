package com.jjim4963tw.library

import android.content.Context

import org.json.JSONException

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class CommunityPHPAsyncTask(context: Context) : BaseAsyncTask<String, String>(context) {
    var asyncTaskResult: AsyncTaskResult<String>? = null

    interface AsyncTaskResult<T : Any> {
        @Throws(JSONException::class)
        fun taskFinish(result: T?)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        try {
            this.asyncTaskResult!!.taskFinish(result)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun doInBackground(vararg params: String): String? {          //同步中必須執行的動作
        val urlWebService = params[0]
        val sendData = params[1]
        try {
            val url = URL(urlWebService)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.doOutput = true
            val outputStream = httpURLConnection.outputStream
            val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
            bufferedWriter.write(sendData)
            bufferedWriter.flush()
            bufferedWriter.close()
            outputStream.close()
            val sb = StringBuilder()
            val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
            var json: String
            while (true) {
                json = bufferedReader.readLine() ?: break
                sb.append(json)
            }
            return sb.toString().trim { it <= ' ' }
        } catch (e: Exception) {
            return null
        }
    }
}