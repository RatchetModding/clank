package net.hashsploit.clank.cli.commands;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;

public class CLIExitCommand implements ICLICommand {

	@Override
	public void invoke(Terminal term, String[] params) {
		Clank.getInstance().shutdown();
	}
	
	@Override
	public String commandName() {
		return "exit";
	}

	@Override
	public String commandDescription() {
		return "Shutdown the server.";
	}

}
