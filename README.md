# Description

Allows an administrator to specify sites trusted to make JSONP or
primitive-XPath REST API requests.

# Configuration

Use *Manage Jenkins » Global Security* to make this configuration.

![](docs/images/Captura_de_pantalla_2016-11-03_a_las_08.50.53.png)

![](docs/images/Captura_de_pantalla_2016-11-03_a_las_08.51.12.png)

- Allow requests without Referer: if checked, then requests with no HTTP Referer will be allowed.
- Domains from which to allow requests: a space and/or newline-separated list of domains to allow requests from.

There is on-line help available for each option.

# Changelog

## Version 1.5 and newer

See [GitHub releases](https://github.com/jenkinsci/secure-requester-whitelist-plugin/releases).

## Version 1.4 (2019-09-05)

-   [JENKINS-57616](https://issues.jenkins-ci.org/browse/JENKINS-57616):
    Add support for JCasC.

## Version 1.3 (2019-05-31)

-   Metadata changes only.

## Version 1.2 (Oct 17 2017)

-   [JENKINS-47253](https://issues.jenkins-ci.org/browse/JENKINS-47253):
    Migration to new parent POM (2.33).

## Version 1.1 (Nov 10 2016)

-   [JENKINS-39470](https://issues.jenkins-ci.org/browse/JENKINS-39470):
    Migration to new parent POM (2.17).

## Version 1.0 (Dec 12 2013)

-   Initial release. Relies for now on Jenkins 1.537 so as to use the
    extension point described in
    [JENKINS-16936](https://issues.jenkins-ci.org/browse/JENKINS-16936).
