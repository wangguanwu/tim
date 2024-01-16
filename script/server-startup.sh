#!/usr/bin/env bash
nohup java -jar  /root/work/server/tim-server-1.0.0-SNAPSHOT.jar --tim.server.port=9000  > /root/work/server/log.file 2>&1 &