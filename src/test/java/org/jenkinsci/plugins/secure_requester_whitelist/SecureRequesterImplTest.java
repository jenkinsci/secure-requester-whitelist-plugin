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

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebRequest;
import java.net.URL;
import net.sf.json.JSONObject;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.PresetData;

public class SecureRequesterImplTest {

    @Rule public JenkinsRule r = new JenkinsRule();

    @PresetData(PresetData.DataSet.NO_ANONYMOUS_READACCESS)
    @Test
    public void authorizing() throws Exception {
        assertJSONP(null, 403);
        assertJSONP("http://apache.org/", 403);

        Whitelist.get().configure(null, new JSONObject().accumulate("allowNoReferer", true).accumulate("domains", "apache.org jenkins-ci.org"));

        assertJSONP(null, 200);
        assertJSONP("http://apache.org/", 200);
        assertJSONP("http://jenkins-ci.org/", 200);
        assertJSONP("https://ci.jenkins-ci.org/some/stuff", 200);
        assertJSONP("http://nowhere.net/", 403);
        assertJSONP("huh?", 403);
    }

    private void assertJSONP(final String referer, final int expectedStatusCode) throws Exception {
        final JenkinsRule.WebClient wc = r.createWebClient();
        wc.login("alice");

        final WebRequest req = new WebRequest(new URL(wc.getContextPath() + "api/json?jsonp"));
        if (referer != null) {
            req.setAdditionalHeader("Referer", referer);
        }

        try {
            wc.getPage(req);
            assertEquals(expectedStatusCode, 200);
        } catch (FailingHttpStatusCodeException x) {
            assertEquals(expectedStatusCode, x.getStatusCode());
        }
    }

}
