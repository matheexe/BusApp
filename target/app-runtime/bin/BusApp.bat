@echo off
set JLINK_VM_OPTIONS=--add-modules=javafx.controls,javafx.fxml,javafx.graphics
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m com.seuprojeto.avaliacao/app.BusApp %*
