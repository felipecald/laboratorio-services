package com.exemplo.laboratorioservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SincronizacaoService : Service() {

    companion object {
        const val CHANNEL_ID = "canal_sincronizacao_ativa"
        const val NOTIFICATION_ID = 1001
    }

    // Escopo de corrotinas em thread de I/O (não bloqueia a Main Thread)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var job: Job? = null
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)
        criarCanalDeNotificacao()
    }

    // Canal obrigatório desde o Android 8.0
    private fun criarCanalDeNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Sincronização ativa",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(canal)
        }
    }

    // Monta a notificação mostrando quantos pacotes já foram processados
    private fun construirNotificacao(pacotes: Int) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sincronização de Alta Performance")
            .setContentText("Pacotes processados: $pacotes")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Promove o serviço a Foreground logo no início
        startForeground(NOTIFICATION_ID, construirNotificacao(0))

        // Evita criar dois loops se o usuário tocar em Iniciar duas vezes
        if (job?.isActive == true) return START_STICKY

        job = serviceScope.launch {
            var pacotes = 0
            while (isActive) {
                delay(5000) // espera 5 segundos
                pacotes++
                Log.i(
                    "SERVICE_CUSTOM",
                    "Ciclo #$pacotes na thread: ${Thread.currentThread().name}"
                )
                // Mesmo ID = substitui a notificação existente (Desafio 3)
                notificationManager.notify(NOTIFICATION_ID, construirNotificacao(pacotes))
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        serviceScope.cancel() // encerra as corrotinas junto com o serviço
        Log.i("SERVICE_CUSTOM", "onDestroy chamado")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}