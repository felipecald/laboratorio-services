package com.exemplo.laboratorioservices

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // Contrato moderno para pedir permissão em tempo de execução
    private val pedirPermissaoNotificacao =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // No Android 13+ a notificação só aparece com esta permissão concedida
        if (Build.VERSION.SDK_INT >= 33) {
            pedirPermissaoNotificacao.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val intentServico = Intent(this, SincronizacaoService::class.java)

        findViewById<Button>(R.id.btnIniciarServico).setOnClickListener {
            ContextCompat.startForegroundService(this, intentServico)
        }

        findViewById<Button>(R.id.btnPararServico).setOnClickListener {
            stopService(intentServico)
        }
    }
}