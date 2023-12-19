package br.com.dio.work.manager.ui.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.dio.work.manager.R
import br.com.dio.work.manager.data.model.Video
import com.squareup.picasso.Picasso

private const val NOTIFICATION_ID = 1
private const val CHANNEL_ID = "new_channel_video"

//função pública - notificação a apertir do vídeo json
fun Context.showBigPictureNotification(
    video: Video
) {
    //pegar instância do picasso
    val bitmapBigPicture = Picasso.get()
        .load(video.thumbnail).get()

    showNotification(
        video = video,
        bitmapBigPicture = bitmapBigPicture
    )
}

private fun Context.showNotification(video: Video, bitmapBigPicture: Bitmap? = null) {
    createNotificationChannel()

    val notification = getNotification(
        video = video,
        bitmapBigPicture = bitmapBigPicture
    )

    NotificationManagerCompat
        .from(this)
        .notify(
            NOTIFICATION_ID,
            notification
        )
}

//definir os channel!! importante pois o usuário pode querer nao receber
//certas notificações
private fun Context.createNotificationChannel() {
    val name = getString(
        R.string.notification_verbose_name
    )
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(CHANNEL_ID, name, importance)

    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .createNotificationChannel(channel)

}

private fun Context.getNotification(
    video: Video,
    bitmapBigPicture: Bitmap? = null,
): Notification {

    val notification = NotificationCompat
        .Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_play)
        .setContentTitle(getString(R.string.notification_verbose_name))
        .setContentText(video.name)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
        .setContentIntent(getPendingIntent(video.url))
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)

    //veirificsar se a imagem nã otá nula pra settar nossa imagem
    if (bitmapBigPicture != null) {
        notification
            .setLargeIcon(bitmapBigPicture)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(bitmapBigPicture)
                    .bigLargeIcon(null)
            )
    }

    return notification.build()
}

//abrir o youtube pra mostrar o vídeo
private fun Context.getPendingIntent(videoUrl: String): PendingIntent {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))

    return PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}