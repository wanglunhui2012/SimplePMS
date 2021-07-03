@echo off
rem @不显示这一行的命令行（只能影响当前行）、echo off此语句后所有运行的命令都不显示命令行本身，二者结合使用则所有命令行本身都不显示

@echo 开始执行

git add --all
@echo 输入提交信息：
set /p message=
git commit --message "%message%"

git push origin master

@echo 完成执行

pause