package com.nms.snmp.ninteface.commandline;

public class CommandLineConfig {
	private Integer size = 10;
	
	public String parsingCommand(String command,Boolean page){
		System.out.println(page);
		String result = "error";
		if(command.contains("site")){
			CommandSite commandSite = new CommandSite();
			result = commandSite.siteCommand(command, page, size);
		}else if(command.contains("port")){
			CommandPort commandPort = new CommandPort();
			result = commandPort.portCommand(command, page,size);
		}else if(command.contains("tunnel")){
			CommandTunnel commandTunnel = new CommandTunnel();
			result = commandTunnel.tunnelCommand(command, page,size);
		}else if(command.contains("protect")){
			CommandProetct commandProetct = new CommandProetct();
			result = commandProetct.proetctCommand(command, page,size);
		}else if(command.contains("pw")){
			CommandPw commandPw = new CommandPw();
			result = commandPw.pwCommand(command, page,size);
		}
		return result;
	}
	
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	
	
}
