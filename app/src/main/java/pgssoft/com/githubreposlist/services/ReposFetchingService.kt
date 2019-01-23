package pgssoft.com.githubreposlist.services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import pgssoft.com.githubreposlist.data.RepoDownloadStatus
import pgssoft.com.githubreposlist.data.RepoRepository
import pgssoft.com.githubreposlist.di.DaggerServiceComponent
import pgssoft.com.githubreposlist.di.ServiceModule
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReposFetchingService : IntentService("fetchrepos") {


    @Inject
    lateinit var repoRepository: RepoRepository

    override fun onCreate() {

        DaggerServiceComponent.builder().serviceModule(ServiceModule(applicationContext)).build().inject(this)

        Log.d("INTENTSERVICE", "service onCreate")
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("INTENTSERVICE", "service onHandleIntent")

        val channelId = "channelId"
        android.os.Debug.waitForDebugger()
        var builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "fetchingService"
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }
        builder = NotificationCompat.Builder(baseContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_rotate)
            .setContentTitle("PGS") // use something from something from
            .setContentText("downloading repos") // use something from something from
            .setProgress(0, 0, true) // display indeterminate progress

        startForeground(1, builder.build())
        try {

            val status = repoRepository.fetchAll()
            status.subscribe { _status ->

                when (_status) {
                    is RepoDownloadStatus.DataOk -> {

                        Log.d("INTENTSERVICE", "Dataok")
                    }
                    is RepoDownloadStatus.ErrorMessage ->
                        Log.d("INTENTSERVICE", _status.message)
                    is RepoDownloadStatus.Forbidden ->
                        Log.d("INTENTSERVICE", "Forbidden")
                }

            }.dispose()

        } finally {
            Log.d("INTENTSERVICE", "finished")
            stopForeground(true)
        }

    }


}
