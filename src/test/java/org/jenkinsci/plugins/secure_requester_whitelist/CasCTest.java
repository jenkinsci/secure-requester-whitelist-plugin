package org.jenkinsci.plugins.secure_requester_whitelist;

import io.jenkins.plugins.casc.misc.RoundTripAbstractTest;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Issue("JENKINS-57616")
public class CasCTest extends RoundTripAbstractTest {

    @Override
    protected String configResource() {
        return "CasCTest.yml";
    }

    @Override
    protected void assertConfiguredAsExpected(RestartableJenkinsRule restartableJenkinsRule, String s) {
        final Whitelist whitelist = Whitelist.get();
        assertTrue(whitelist.isAllowNoReferer());
        assertEquals("acme.org jenkins.io", whitelist.getDomains());
    }

    @Override
    protected String stringInLogExpected() {
        return "Setting org.jenkinsci.plugins.secure_requester_whitelist.Whitelist";
    }
}
