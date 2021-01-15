package services

import java.io.IOException
import com.google.inject.Inject
import play.api.Configuration
import org.apache.directory.ldap.client.api.LdapConnectionConfig
import org.apache.directory.ldap.client.api.LdapNetworkConnection
import org.apache.directory.ldap.client.api.exception.InvalidConnectionException
import org.apache.directory.api.ldap.model.exception.LdapException


class LdapService @Inject()(config: Configuration) {

  /**
   * Verifies credentials with ldap service
   *
   * @param username username from credentials
   * @param password password from credentials
   * @return true if credentials are valid
   */
  def authenticate(username: String, password: String): Boolean = {
    val ldapConnectionConfig: LdapConnectionConfig = getConnectionConfig(username, password)
    var ldapConnection: LdapNetworkConnection = null

    try {
      ldapConnection = new LdapNetworkConnection(ldapConnectionConfig)
      ldapConnection.bind()
    } catch {
      case e: InvalidConnectionException =>
        return false
      // e.printStackTrace()
      // throw new Exception("ldap.noConnection")
      case e: LdapException =>
        return false
      // e.printStackTrace()
      // throw new Exception("ldap.wrongCredentials")
    }

    try {
      ldapConnection.close()
    } catch {
      case e: IOException =>
        return false
      // e.printStackTrace()
    }

    true
  }

  private def getConnectionConfig(username: String, password: String): LdapConnectionConfig = {
    val ldapServer = config.get[String]("ldap.server")
    val ldapPort = config.get[Int]("ldap.port")
    val ldapStartTls = config.get[Boolean]("ldap.startTls")
    val userRoot = config.get[String]("ldap.userRoot")
    val connectionBind = config.get[String]("ldap.connectionBind")
      .replace("%USER%", username).replace("%USER_ROOT%", userRoot)

    val connectionConfig = new LdapConnectionConfig()
    connectionConfig.setLdapHost(ldapServer)
    connectionConfig.setLdapPort(ldapPort)
    connectionConfig.setUseTls(ldapStartTls)
    connectionConfig.setUseSsl(true)
    connectionConfig.setCredentials(password)
    connectionConfig.setName(connectionBind)

    connectionConfig
  }
}