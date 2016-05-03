# Jenkins `FilePath` Download Test

I'm having difficulty with the [`FilePath#installIfNecessary`][0] API in Jenkins.

This is a pretty minimal reproduction attempt, which fails outside of Jenkins in the same way that it does from my [plugin][1].

## What this repo does
It implements a tool which uses the `FilePath` API to download a `.tar.gz` file and extract it to a temporary directory.  This is essentially what a [tool installer][1] does in Jenkins.

## Problem
Using this API seems to fail with a certain set of `.tar.gz` files, regardless of where the file is downloaded from (the official Google distribution server, a personal server of mine, or from localhost).

The error message is usually `Failed to unpack <url> (153358 bytes read of total 76218682)`, where the number of bytes read can vary between multiple runs with the same URL.

These files can be downloaded (and subsequently extracted) successfully with `wget`, but fail when downloaded using `FilePath`.  

The following URLs of Go distributions have been tested with this program:

Go version | Status
-----------|-------
[1.6.2](https://storage.googleapis.com/golang/go1.6.2.darwin-amd64.tar.gz) | Fails
[1.6.1](https://storage.googleapis.com/golang/go1.6.1.darwin-amd64.tar.gz) | Fails
[1.6](https://storage.googleapis.com/golang/go1.6.darwin-amd64.tar.gz) | Fails
[1.5.4](https://storage.googleapis.com/golang/go1.5.4.darwin-amd64.tar.gz) | Fails
[1.5.3](https://storage.googleapis.com/golang/go1.5.3.darwin-amd64.tar.gz) | Fails
[1.5.2](https://storage.googleapis.com/golang/go1.5.2.darwin-amd64.tar.gz) | Fails
[1.5.1](https://storage.googleapis.com/golang/go1.5.1.darwin-amd64.tar.gz) | Succeeds
[1.5](https://storage.googleapis.com/golang/go1.5.darwin-amd64.tar.gz) | Succeeds
[1.4.3](https://storage.googleapis.com/golang/go1.4.3.darwin-amd64.tar.gz) | Fails
[1.4.2](https://storage.googleapis.com/golang/go1.4.2.darwin-amd64-osx10.8.tar.gz) | Succeeds
[1.4.1](https://storage.googleapis.com/golang/go1.4.1.darwin-amd64-osx10.8.tar.gz) | Succeeds
[1.4](https://storage.googleapis.com/golang/go1.4.darwin-amd64-osx10.8.tar.gz) | Succeeds

The same results have been achieved on both OS X 10.11.4 and Ubuntu 14.04.3.

## Usage

To run with a default URL (Go 1.6.2):  
`./gradlew run`

To run with a custom URL:  
`./gradlew run -Pargs=https://example.com/some-file.tgz`

[0]:https://github.com/jenkinsci/jenkins/blob/jenkins-2.0/core/src/main/java/hudson/FilePath.java#L757
[1]:https://wiki.jenkins-ci.org/display/JENKINS/Go+Plugin
[2]:https://wiki.jenkins-ci.org/display/JENKINS/Tool+Auto-Installation