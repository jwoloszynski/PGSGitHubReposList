package pgssoft.com.githubreposlist.services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import pgssoft.com.githubreposlist.data.Event
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.di.DaggerServiceComponent
import pgssoft.com.githubreposlist.di.ServiceModule
import pgssoft.com.githubreposlist.utils.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReposFetchingService : IntentService("fetchrepos") {

    companion object {
        var statusEvent = MutableLiveData<Event<RepoDownloadStatus>>()
    }
    @Inject
    lateinit var repoRepository: RepoRepository
    @Inject
    lateinit var networkUtils: NetworkUtils

    override fun onCreate() {
        DaggerServiceComponent.builder().serviceModule(ServiceModule(applicationContext)).build().inject(this)
        Log.d("INTENTSERVICE", "service onCreate")
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("INTENTSERVICE", "service onHandleIntent")

        startForeground(1, createNotification())
        try {
            if (networkUtils.isConnection()) {
                val status = repoRepository.fetchAll()

                status.subscribe { _status ->
                    statusEvent.postValue(Event(_status))}.dispose()
            } else {
                statusEvent.postValue(Event(RepoDownloadStatus.ErrorNoConnection))
            }

        } finally {
            Log.d("INTENTSERVICE", "finished")
            stopForeground(true)
        }
    }

    private fun createNotification(): Notification {

        val channelId = "channelId"
        android.os.Debug.waitForDebugger()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "fetchingService"
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }
        val builder = NotificationCompat.Builder(baseContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_rotate)
            .setContentTitle("PGS") // use something from something from
            .setContentText("downloading repos") // use something from something from
            .setProgress(0, 0, true) // display indeterminate progress

        return builder.build()
    }
}
