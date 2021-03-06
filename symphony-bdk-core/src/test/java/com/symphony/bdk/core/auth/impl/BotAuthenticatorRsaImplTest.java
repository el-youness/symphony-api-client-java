package com.symphony.bdk.core.auth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.symphony.bdk.core.api.invoker.ApiRuntimeException;
import com.symphony.bdk.core.auth.AuthSession;
import com.symphony.bdk.core.auth.exception.AuthUnauthorizedException;
import com.symphony.bdk.core.test.BdkMockServer;
import com.symphony.bdk.core.test.BdkMockServerExtension;
import com.symphony.bdk.core.test.RsaTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Test class for the {@link BotAuthenticatorRsaImpl}.
 */
@ExtendWith(BdkMockServerExtension.class)
class BotAuthenticatorRsaImplTest {

  private BotAuthenticatorRsaImpl authenticator;

  @BeforeEach
  void init(final BdkMockServer mockServer) {

    this.authenticator = new BotAuthenticatorRsaImpl(
        "username",
        RsaTestHelper.generateKeyPair().getPrivate(),
        mockServer.newApiClient("/login"),
        mockServer.newApiClient("/relay")
    );
  }

  @Test
  void testAuthenticateBot() {
    final AuthSession session = this.authenticator.authenticateBot();
    assertNotNull(session);
    assertEquals(AuthSessionImpl.class, session.getClass());
    assertEquals(this.authenticator, ((AuthSessionImpl) session).getAuthenticator());
  }

  @Test
  void testRetrieveSessionToken(final BdkMockServer mockServer) throws AuthUnauthorizedException {

    mockServer.onPost("/login/pubkey/authenticate", res -> res.withBody("{ \"token\": \"1234\", \"name\": \"sessionToken\" }"));

    final String sessionToken = this.authenticator.retrieveSessionToken();
    assertEquals("1234", sessionToken);
  }

  @Test
  void testRetrieveKeyManagerToken(final BdkMockServer mockServer) throws AuthUnauthorizedException {

    mockServer.onPost("/relay/pubkey/authenticate", res -> res.withBody("{ \"token\": \"1234\", \"name\": \"sessionToken\" }"));

    final String sessionToken = this.authenticator.retrieveKeyManagerToken();
    assertEquals("1234", sessionToken);
  }

  @Test
  void testAuthUnauthorizedException(final BdkMockServer mockServer) {

    mockServer.onPost("/login/pubkey/authenticate", res -> res.withStatusCode(401));

    assertThrows(AuthUnauthorizedException.class, () -> this.authenticator.retrieveSessionToken());
  }

  @Test
  void testUnknownApiException(final BdkMockServer mockServer) {

    mockServer.onPost("/login/pubkey/authenticate", res -> res.withStatusCode(503));

    assertThrows(ApiRuntimeException.class, () -> this.authenticator.retrieveSessionToken());
  }
}
