package org.jenkinsci.plugins.secure_requester_whitelist;

import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.snakeyaml.Yaml;
import jenkins.model.Jenkins;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import java.io.StringReader;
import java.util.Map;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CasCTest {

    @Rule
    public JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();

    @Test
    @Issue("JENKINS-57616")
    @ConfiguredWithCode("CasCTest.yml")
    public void configureWhitelist() throws Exception {
        final Whitelist whitelist = Whitelist.get();
        assertTrue(whitelist.isAllowNoReferer());
        assertEquals("acme.org jenkins.io", whitelist.getDomains());

        //Check export
        whitelist.setDomains("example.com");
        whitelist.save();

        String confStr = j.exportToString(true);
        Map<String, Object> conf = new Yaml().load(confStr);
        assertThat(conf, hasEntry(equalTo("security"), instanceOf(Map.class)));
        Map<String,Object> security = (Map<String, Object>) conf.get("security");
        assertThat(security, hasEntry(equalTo("secure_requester_whitelist"), instanceOf(Map.class)));
        Map<String, Object> cw = (Map<String, Object>) security.get("secure_requester_whitelist");
        assertThat(cw, hasEntry(equalTo("allowNoReferer"), equalTo(true)));
        assertThat(cw, hasEntry(equalTo("domains"), equalTo("example.com")));
    }
}
