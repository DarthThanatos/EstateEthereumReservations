import os

N = 3
START_PORT = 30300
BOOT_BAT = "run_bootnode.bat"

BOOT_IP="192.168.0.100"
# BOOT_IP = "127.0.0.1"


boot_cmd =\
	"start cmd /K bootnode.exe -nodekey bootnode{0}.key -addr \"{2}:{1}\" -verbosity 9\n\n"

res_cmds = ""
for i in range (N):
	res_cmds += boot_cmd.format(i, START_PORT - i, BOOT_IP)

with open(BOOT_BAT, "w+") as f:
	f.write(res_cmds)

os.system("start cmd /C {}".format(BOOT_BAT)) #/K remains the window, /C executes and dies (popup)

