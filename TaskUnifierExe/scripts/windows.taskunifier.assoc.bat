@echo off

assoc .tue=TaskUnifier
ftype TaskUnifier="$INSTALL_PATH\TaskUnifier.exe" "%1"
