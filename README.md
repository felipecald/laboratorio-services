\# Laboratório de Services - Android



\*\*Aluno:\*\* FELIPE CALDERARO

\*\*Curso:\*\* CIÊNCIA DA COMPUTAÇÃO - UNINORTE

\*\*Disciplina:\*\* DESENVOLVIMENTO MOBILE

\*\*Atividade:\*\* Módulo 4 - Desafio 3



\## Sobre



Neste trabalho criei um app Android em Kotlin com um Foreground Service. O serviço roda em segundo plano e, a cada 5 segundos, atualiza o texto da própria notificação mostrando quantos pacotes já foram processados (Desafio 3).



\## Como funciona



\- Dois botões na tela: \*\*Iniciar Serviço\*\* e \*\*Parar Serviço\*\*.

\- Ao iniciar, o serviço mostra uma notificação fixa e começa uma corrotina em `Dispatchers.IO`, para não travar a tela.

\- A cada 5 segundos o contador aumenta e a notificação é atualizada com `notify()` usando o mesmo ID, o que substitui a notificação em vez de criar outra.

\- Ao parar, o `onDestroy()` cancela a corrotina.

\- No Android 13 ou superior, o app pede a permissão de notificações ao abrir.



\## Evidências



| Notificação (início) | Notificação (depois) | Logcat |

|:---:|:---:|:---:|

| !\[Notificação 1](prints/notificacao-1.png) | !\[Notificação 2](prints/notificacao-2.png) | !\[Logcat](prints/logcat-ciclos.png) |



No Logcat (filtro `SERVICE\_CUSTOM`) dá para ver os ciclos rodando na thread `DefaultDispatcher-worker` e o `onDestroy chamado` ao parar o serviço.



\## Como executar



1\. Abrir o projeto no Android Studio.

2\. Rodar em um emulador ou celular com Android 13 ou superior.

3\. Permitir as notificações e tocar em \*\*Iniciar Serviço\*\*.

