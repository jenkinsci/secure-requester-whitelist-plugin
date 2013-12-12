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

import hudson.Extension;
import hudson.Util;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;
import jenkins.security.SecureRequester;
import org.kohsuke.stapler.StaplerRequest;

@Extension public class SecureRequesterImpl implements SecureRequester {

    @Override public boolean permit(StaplerRequest req, Object bean) {
        Whitelist wl = Whitelist.get();
        String referer = req.getReferer();
        if (referer == null) {
            return wl.isAllowNoReferer();
        }
        URI uri;
        try {
            uri = new URI(referer);
        } catch (URISyntaxException x) {
            return false;
        }
        String host = uri.getHost();
        if (host == null) {
            return false;
        }
        StringTokenizer tok = new StringTokenizer(Util.fixNull(wl.getDomains()));
        while (tok.hasMoreTokens()) {
            String domain = tok.nextToken();
            if (("." + host).endsWith("." + domain)) {
                return true;
            }
        }
        return false;
    }

}
