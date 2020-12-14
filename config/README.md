## Clank general configuration

- The **mode** is a *string* and must be either `MEDIUS_UNIVERSE_INFORMATION_SERVER`, `MEDIUS_AUTHENTICATION_SERVER`, `MEDIUS_LOBBY_SERVER`, `NAT_SERVER` or `DME_SERVER`.
- The **log_level** is a *string* and must be one of the following: `FINEST` > `FINER` > `FINE` > `INFO` > `WARN` > `SEVERE`.
- The **address** is a *string* and can be set to a valid IPv4 address or `null` to use the default interface's address. This is the address in which the server will bind to and listen on.
- The **port** is an *integer* and must be valid port from 1025 to 65535, this is the port in which the server will bind to and listen on.

### MAS configuration (config/mas.json.example)

The component is an authentication server for handling player logins.

| Name                     | Type    | Description                                                                    |
|--------------------------|---------|--------------------------------------------------------------------------------|
| encryption               | boolean | Enabled if this server should enable and enforce/require SCE-RT encryption.    |
| capacity                 | integer | Maximum number of concurrent clients this server can support.                  |
| timeout                  | integer | Time in milliseconds of how long a client can idle not sending a echo heartbeat before being automatically disconnected.|
| parent_threads           | integer | Number of threads this server should use for accepting connections into the event-loop.|
| child_threads            | integer | Number of threads to use for processing requests in the event-loop.            |
| max_login_attempts       | integer | Maximum number of failed login attempts from a single IP address before getting blocked for a set number of time.|
| application_id           | integer | This is the App Id of the game to support.                                     |
| mls                      | object  | See **MLS object** below.                                                      |
| nat                      | object  | See **NAT Object** below.                                                      |
| whitelist                | object  | See **Whitelist Object** below.                                                |
| policy                   | string  | The Policy/EULA to send back to the player.                                    |
| announcements            | array   | An array of *string* announcements to send to the player.                      |
| database                 | object  | See **Database Object** below.                                                 |
| system_message           | object  | See **System Message Object** below.                                           |


**MLS Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the MLS server, or `null` to use the current public IPv4 Address.|
| port                     | integer | Port of the MLS server.                                                          |


**NAT Object:**

| Name                     | Type    | Description                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------|
| address                  | string  | IPv4 address of the NAT server, or `null` to use the current public IPv4 Address.|
| port                     | integer | Port of the NAT server.                                                          |


**Whitelist Object:**

| Name                     | Type    | Description                                                                         |
|--------------------------|---------|-------------------------------------------------------------------------------------|
| enabled                  | boolean | Enable if you want to only allow the following whitelisted usernames in the server. |
| players                  | array   | An array of *string* usernames that are allowed to authenticate. All other players will not be allowed to authenticate if **enabled** is set to `true`.|


**Database Object:**

| Name     | Type    | Description                                           |
|----------|---------|-------------------------------------------------------|
| host     | string  | The hostname or address of the MySQL/MariaDB server.  |
| database | string  | The database name to use within the DBMS.             |
| username | string  | The username to use to authenticate with the DBMS.    |
| password | string  | The password to use to authenticate with the DBMS.    |


**System Message Object:**

| Name     | Type    | Description                                                                          |
|----------|---------|--------------------------------------------------------------------------------------|
| enabled  | boolean | If set to true, this will send an automatic system message to the player on connect. |
| severity | integer | The message severity. Valid values are `0` to `255`.                                 |
| message  | string  | The message to send. The message may not exceed `1000` characters.                   |
