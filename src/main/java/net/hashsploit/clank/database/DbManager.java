package net.hashsploit.clank.database;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;

public class DbManager {

	private final Clank clank;
	private IDatabase db;
	
	public DbManager(Clank clank, IDatabase databaseType) {
		this.clank = clank;
		this.db = databaseType;
	}

	public boolean accountExists(String username) {
		
		// Check if the whitelist is enabled
		if (clank.getConfig() instanceof MasConfig) {
			final MasConfig masConfig = (MasConfig) clank.getConfig();
			if (masConfig.isWhitelistEnabled()) {
				boolean exists = false;
				// Check if the username matches any of the usernames in the config
				for (final String s : masConfig.getWhitelistedUsernames()) {
					if (s.equalsIgnoreCase(username)) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					return false;
				}
			}
		}
		
		return db.accountExists(username);
	}

	public boolean validateAccount(String username, String password) {
		return db.validateAccount(username, password);
	}

	public int getAccountId(String username) {
		return db.getAccountId(username);
	}

	public String getMlsToken(Integer accountId) {
		return db.getMlsToken(accountId);
	}
	
	public int getAccountIdFromMlsToken(String mlsToken) {
		return db.getAccountIdFromMlsToken(mlsToken);
	}

	public String getUsername(int accountId) {
		return db.getUsername(accountId);
	}
	
	
	
}
