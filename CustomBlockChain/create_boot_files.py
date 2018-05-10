import os

N = 3
boot_cmd =\
	"bootnode.exe -genkey bootnode{0}.key\n" +\
	"bootnode.exe -nodekey bootnode{0}.key -writeaddress > boot_addr{0}\n" 

BOOT_BAT = "run_bootnode.bat"

res_cmds = ""
for i in range (N):
	res_cmds += boot_cmd.format(i)

with open(BOOT_BAT, "w+") as f:
	f.write(res_cmds)

os.system("start cmd /C {}".format(BOOT_BAT)) #/K remains the window, /C executes and dies (popup)