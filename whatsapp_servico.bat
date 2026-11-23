@echo off
rem ======================================================================
rem  Kitanda Comercial - servico de envio dos relatorios por WhatsApp
rem
rem  Envia os relatorios nas horas agendadas SEM o sistema aberto.
rem  Copiar este ficheiro para a pasta do programa (onde estao as pastas
rem  "credencial", "relatorios" e "lib") e agendar no Agendador de Tarefas
rem  do Windows: "Ao iniciar o computador" (ver instrucoes no fim).
rem
rem  Registo: anexos\whatsapp\servico.log
rem  So corre uma copia por computador (as outras terminam sozinhas).
rem ======================================================================
setlocal
cd /d "%~dp0"

rem --- o .jar do sistema (qualquer versao), aqui ou em dist\ ---
set "JAR="
for %%f in ("%~dp0SYS_KITANDA_*.jar") do set "JAR=%%~ff"
if not defined JAR for %%f in ("%~dp0dist\SYS_KITANDA_*.jar") do set "JAR=%%~ff"
if not defined JAR (
  echo Nao encontrei o SYS_KITANDA_*.jar nesta pasta nem em dist\
  exit /b 1
)
for %%d in ("%JAR%") do set "PASTA_JAR=%%~dpd"

rem --- Java: JAVA_HOME se existir, senao o do sistema ---
set "JAVAW=javaw"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javaw.exe" set "JAVAW=%JAVA_HOME%\bin\javaw.exe"

start "Kitanda WhatsApp" /min "%JAVAW%" -Djava.awt.headless=true -Dfile.encoding=UTF-8 -cp "%JAR%;%PASTA_JAR%lib\*" comercial.whatsapp.ServicoWhatsApp
exit /b 0

rem ======================================================================
rem  AGENDADOR DE TAREFAS (uma vez, no servidor, como administrador):
rem
rem    schtasks /Create /TN "Kitanda WhatsApp" /SC ONSTART /DELAY 0002:00 ^
rem      /TR "\"C:\Kitanda\whatsapp_servico.bat\"" /RU SYSTEM /RL HIGHEST /F
rem
rem  (trocar C:\Kitanda pela pasta do programa). Com /RU SYSTEM corre mesmo
rem  sem ninguem com sessao iniciada. Para arrancar ja, sem reiniciar:
rem    schtasks /Run /TN "Kitanda WhatsApp"
rem ======================================================================
