package org.jenkinsci.plugins.secure_requester_whitelist;

import io.jenkins.plugins.casc.misc.junit.jupiter.AbstractRoundTripTest;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Issue("JENKINS-57616")
@WithJenkins
class CasCTest extends AbstractRoundTripTest {

    @Override
    protected String configResource() {
        return "CasCTest.yml";
    }

    @Override
    protected void assertConfiguredAsExpected(JenkinsRule restartableJenkinsRule, String s) {
        final Whitelist whitelist = Whitelist.get();
        assertTrue(whitelist.isAllowNoReferer());
        assertEquals("acme.org jenkins.io", whitelist.getDomains());
    }

    @Override
    protected String stringInLogExpected() {
        return "Setting org.jenkinsci.plugins.secure_requester_whitelist.Whitelist";
    }
}
