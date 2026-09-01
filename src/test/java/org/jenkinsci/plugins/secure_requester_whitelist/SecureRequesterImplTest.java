/*
 * The MIT License
 *
 * Copyright 2013 Jesse Glick.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.secure_requester_whitelist;

import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.WebRequest;
import java.net.URL;

import hudson.security.FullControlOnceLoggedInAuthorizationStrategy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class SecureRequesterImplTest {

    private JenkinsRule r;

    @BeforeEach
    void beforeEach(JenkinsRule rule) {
        r = rule;
    }

    @Test
    void authorizing() throws Exception {
        r.jenkins.setSecurityRealm(r.createDummySecurityRealm());
        final FullControlOnceLoggedInAuthorizationStrategy a = new FullControlOnceLoggedInAuthorizationStrategy();
        a.setAllowAnonymousRead(false);
        r.jenkins.setAuthorizationStrategy(a);

        assertJSONP(null, 403);
        assertJSONP("http://apache.org/", 403);
        final Whitelist whitelist = Whitelist.get();
        whitelist.setAllowNoReferer(true);
        whitelist.setDomains("apache.org jenkins-ci.org");
        whitelist.save();
        assertJSONP(null, 200);
        assertJSONP("http://apache.org/", 200);
        assertJSONP("http://jenkins-ci.org/", 200);
        assertJSONP("https://ci.jenkins-ci.org/some/stuff", 200);
        assertJSONP("http://nowhere.net/", 403);
        assertJSONP("huh?", 403);
    }

    private void assertJSONP(final String referer, final int expectedStatusCode) throws Exception {
        try (JenkinsRule.WebClient wc = r.createWebClient()) {
            wc.login("alice");

            final WebRequest req = new WebRequest(new URL(wc.getContextPath() + "api/json?jsonp"));
            if (referer != null) {
                req.setAdditionalHeader("Referer", referer);
            }

            wc.getPage(req);
            assertEquals(200, expectedStatusCode);
        } catch (FailingHttpStatusCodeException x) {
            assertEquals(expectedStatusCode, x.getStatusCode());
        }
    }
}
