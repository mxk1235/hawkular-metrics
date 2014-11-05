#!/bin/sh


VERSION=`mvn  help:evaluate -Dexpression=project.version | grep -vF "[INFO]"`
MVN_REPO=`mvn help:evaluate -Dexpression=settings.localRepository | grep -vF "[INFO]"`

echo "(Re)building the commons library"
cd ../common
mvn install
cd -

echo "(Re)building ptrans"
mvn install

java -Djava.net.preferIPv4Stack=true \
   -cp ${MVN_REPO}/io/netty/netty-all/4.0.24.Final/netty-all-4.0.24.Final.jar\
:target/ptrans-$VERSION.jar\
:../common/target/clients-common-$VERSION.jar\
:${HOME}/.m2/repository/org/slf4j/slf4j-log4j12/1.7.7/slf4j-log4j12-1.7.7.jar\
:${HOME}/.m2/repository/org/slf4j/slf4j-api/1.7.7/slf4j-api-1.7.7.jar\
:${HOME}/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar\
:${HOME}/.m2/repository/org/acplt/oncrpc/1.0.7/oncrpc-1.0.7.jar\
 org.rhq.metrics.clients.ptrans.Main $*
